package vfyjxf.gregicprobe.mixin.gregtech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import gregtech.api.capability.impl.AbstractRecipeLogic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.llamalad7.mixinextras.sugar.Local;

import gregtech.api.metatileentity.MTETrait;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.chance.boost.ChanceBoostFunction;
import gregtech.api.recipes.chance.output.ChancedOutput;
import gregtech.api.recipes.chance.output.ChancedOutputList;
import gregtech.api.recipes.chance.output.ChancedOutputLogic;
import vfyjxf.gregicprobe.GregicProbe;
import vfyjxf.gregicprobe.mixin.helper.AccessorAbstractRecipeLogic;

/**
 * From <a href=
 * "https://github.com/Nomi-CEu/Nomi-Labs/blob/main/src/main/java/com/nomiceu/nomilabs/mixin/gregtech/AbstractRecipeLogicMixin.java">NomiLabs</a>.
 */
@Mixin(value = AbstractRecipeLogic.class, remap = false)
public abstract class AbstractRecipeLogicMixin extends MTETrait implements AccessorAbstractRecipeLogic {

    @Unique
    private static final String NON_CHANCED_ITEM_AMT = "probe$nonChancedItemAmt";

    @Unique
    private static final String CHANCED_ITEM_OUTPUTS_KEY = "probe$chancedItemOutputs";

    @Unique
    private static final String ON_CHANCED_FLUID_AMT_KEY = "probe$nonChancedFluidAmt";

    @Unique
    private static final String CHANCED_FLUID_OUTPUTS_KEY = "probe$chancedFluidOutputs";

    @Unique
    private static final String CHANCE_KEY = "chance";

    @Shadow
    protected NonNullList<ItemStack> itemOutputs;

    @Shadow
    protected List<FluidStack> fluidOutputs;

    @Shadow
    protected int recipeEUt;

    @Shadow
    @Final
    private RecipeMap<?> recipeMap;
    @Shadow
    protected int progressTime;

    @Shadow
    public abstract @Nullable RecipeMap<?> getRecipeMap();

    /**
     * List of non-chanced item outputs.The actual non-chanced item outputs are taken from the item outputs saved list,
     * taking the first n elements.
     */
    @Unique
    private int probe$nonChancedItemAmt = 0;

    /**
     * Map of chanced item outputs to their boosted chance, for this recipe.
     */
    @Unique
    private List<Pair<ItemStack, Integer>> probe$chancedItemOutputs = null;

    /**
     * Number of non-chanced fluid outputs. The actual non-chanced fluid outputs are taken from the fluid outputs saved
     * list, taking the first n elements.
     */
    @Unique
    private int probe$nonChancedFluidAmt = 0;

    /**
     * Map of chanced item outputs to their boosted chance, for this recipe.
     */
    @Unique
    private List<Pair<FluidStack, Integer>> probe$chancedFluidOutputs = null;

    /**
     * Default Ignored Constructor
     */
    private AbstractRecipeLogicMixin(@NotNull MetaTileEntity metaTileEntity) {
        super(metaTileEntity);
    }

    @Unique
    @Override
    public boolean probe$isValidForOutputTOP() {
        return probe$getEUt() >= 0 && getRecipeMap() != null;
    }

    @Unique
    @Override
    public List<ItemStack> probe$getOutputs() {
        if (itemOutputs == null) {
            GregicProbe.logger.error("Item Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return itemOutputs;
    }

    @Unique
    @Override
    public List<FluidStack> probe$getFluidOutputs() {
        if (fluidOutputs == null) {
            GregicProbe.logger.error("Fluid Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return fluidOutputs;
    }

    @Unique
    @Override
    public int probe$getEUt() {
        return recipeEUt;
    }

    @Unique
    @Override
    public int probe$getNonChancedItemAmt() {
        return probe$nonChancedItemAmt;
    }

    @Unique
    @Override
    public List<Pair<ItemStack, Integer>> probe$getChancedItemOutputs() {
        if (probe$chancedItemOutputs == null) {
            GregicProbe.logger.error("Chanced Item Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return probe$chancedItemOutputs;
    }

    @Unique
    @Override
    public int probe$getNonChancedFluidAmt() {
        return probe$nonChancedFluidAmt;
    }

    @Unique
    @Override
    public List<Pair<FluidStack, Integer>> probe$getChancedFluidOutputs() {
        if (probe$chancedFluidOutputs == null) {
            GregicProbe.logger.error("Chanced Fluid Outputs List for Recipe Logic {} of Recipe Map {} is null!",
                    getClass().getName(), getRecipeMap().getUnlocalizedName());
            return new ArrayList<>();
        }
        return probe$chancedFluidOutputs;
    }

    @Inject(method = "completeRecipe", at = @At("TAIL"))
    private void clearprobeValues(CallbackInfo ci) {
        probe$nonChancedItemAmt = 0;
        probe$chancedItemOutputs = null;
        probe$nonChancedFluidAmt = 0;
        probe$chancedFluidOutputs = null;
    }

    @Inject(method = "setupRecipe", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void setupValues(Recipe recipe, CallbackInfo ci, @Local(ordinal = 0) int recipeTier,
                                 @Local(ordinal = 1) int machineTier) {
        // At this point, recipe outputs are already trimmed (in prepareRecipe), so we can use the actual values
        probe$nonChancedItemAmt = recipe.getOutputs().size();
        probe$nonChancedFluidAmt = recipe.getFluidOutputs().size();

        probe$chancedItemOutputs = probe$fillChancedOutputsMap(recipe.getChancedOutputs(), getRecipeMap().getChanceFunction(),
                recipeTier, machineTier);
        probe$chancedFluidOutputs = probe$fillChancedOutputsMap(recipe.getChancedFluidOutputs(),
                getRecipeMap().getChanceFunction(),
                recipeTier, machineTier);
    }

    @Unique
    private <T> List<Pair<T, Integer>> probe$fillChancedOutputsMap(ChancedOutputList<T, ? extends ChancedOutput<T>> list,
                                                                  ChanceBoostFunction function, int recipeTier,
                                                                  int machineTier) {
        List<Pair<T, Integer>> result = new ArrayList<>();
        if (list.getChancedEntries().isEmpty()) return result;

        for (var entry : list.getChancedEntries()) {
            result.add(Pair.of(entry.getIngredient(), Math.min(ChancedOutputLogic.getMaxChancedValue(),
                    ChancedOutputLogic.getChance(entry, function, recipeTier, machineTier))));
        }
        return result;
    }

    @Inject(method = "deserializeNBT", at = @At("TAIL"))
    private void loadValues(NBTTagCompound compound, CallbackInfo ci) {
        if (progressTime <= 0) return;

        probe$nonChancedItemAmt = compound.getInteger(NON_CHANCED_ITEM_AMT);
        probe$nonChancedFluidAmt = compound.getInteger(ON_CHANCED_FLUID_AMT_KEY);

        NBTTagList items = compound.getTagList(CHANCED_ITEM_OUTPUTS_KEY, Constants.NBT.TAG_COMPOUND);
        probe$chancedItemOutputs = new ArrayList<>();
        for (var item : items) {
            var tag = (NBTTagCompound) item;
            probe$chancedItemOutputs.add(Pair.of(new ItemStack(tag), tag.getInteger(CHANCE_KEY)));
        }

        NBTTagList fluids = compound.getTagList(CHANCED_FLUID_OUTPUTS_KEY, Constants.NBT.TAG_COMPOUND);
        probe$chancedFluidOutputs = new ArrayList<>();
        for (var fluid : fluids) {
            var tag = (NBTTagCompound) fluid;
            probe$chancedFluidOutputs
                    .add(Pair.of(FluidStack.loadFluidStackFromNBT(tag), tag.getInteger(CHANCE_KEY)));
        }
    }

    @Inject(method = "serializeNBT", at = @At("RETURN"))
    private void saveValues(CallbackInfoReturnable<NBTTagCompound> cir) {
        if (progressTime <= 0) return;
        NBTTagCompound nbt = cir.getReturnValue();
        nbt.setInteger(NON_CHANCED_ITEM_AMT, probe$nonChancedItemAmt);
        nbt.setInteger(ON_CHANCED_FLUID_AMT_KEY, probe$nonChancedFluidAmt);

        probe$addChancedToTag(nbt, CHANCED_ITEM_OUTPUTS_KEY, probe$chancedItemOutputs,
                (entry) -> entry.getKey().writeToNBT(new NBTTagCompound()), Pair::getValue);
        probe$addChancedToTag(nbt, CHANCED_FLUID_OUTPUTS_KEY, probe$chancedFluidOutputs,
                (entry) -> entry.getKey().writeToNBT(new NBTTagCompound()), Pair::getValue);
    }

    @Unique
    private <T> void probe$addChancedToTag(NBTTagCompound nbt, String key, List<T> list,
                                           Function<T, NBTTagCompound> createTag, Function<T, Integer> getChance) {
        if (list == null) return;

        var entries = new NBTTagList();
        for (var entry : list) {
            NBTTagCompound tag = createTag.apply(entry);
            tag.setInteger(CHANCE_KEY, getChance.apply(entry));
            entries.appendTag(tag);
        }
        nbt.setTag(key, entries);
    }
}

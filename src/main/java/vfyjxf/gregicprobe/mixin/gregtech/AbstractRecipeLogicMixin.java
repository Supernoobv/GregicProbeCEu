package vfyjxf.gregicprobe.mixin.gregtech;

import gregtech.api.capability.impl.AbstractRecipeLogic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import vfyjxf.gregicprobe.mixin.helper.AccessorAbstractRecipeLogic;

import java.util.List;

@Mixin(value = AbstractRecipeLogic.class, remap = false)
public class AbstractRecipeLogicMixin implements AccessorAbstractRecipeLogic {

    @Shadow
    protected NonNullList<ItemStack> itemOutputs;

    @Shadow
    protected List<FluidStack> fluidOutputs;

    @Shadow
    protected int recipeEUt;

    @Unique
    @Override
    public List<ItemStack> probe$getOutputs() {
        return itemOutputs;
    }

    @Unique
    @Override
    public List<FluidStack> probe$getFluidOutputs() {
        return fluidOutputs;
    }

    @Unique
    @Override
    public int probe$getEUt() {
        return recipeEUt;
    }
}

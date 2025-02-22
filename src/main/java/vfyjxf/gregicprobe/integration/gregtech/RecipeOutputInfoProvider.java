package vfyjxf.gregicprobe.integration.gregtech;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IWorkable;
import gregtech.integration.theoneprobe.provider.CapabilityInfoProvider;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.apiimpl.elements.ElementItemStack;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import vfyjxf.gregicprobe.config.GregicProbeConfig;
import vfyjxf.gregicprobe.element.*;
import vfyjxf.gregicprobe.mixin.helper.AccessorAbstractRecipeLogic;
import vfyjxf.gregicprobe.util.ItemMeta;
import vfyjxf.gregicprobe.util.TranslationUtils;



/**
 * From <a href=
 * "https://github.com/Nomi-CEu/Nomi-Labs/blob/main/src/main/java/com/nomiceu/nomilabs/integration/top/RecipeOutputsProvider.java">NomiLabs</a>.
 */
public class RecipeOutputInfoProvider extends CapabilityInfoProvider<IWorkable> {
    private static final int AMOUNT_IN_ROW = 10;
    private static final DecimalFormat format = new DecimalFormat("#.#");

    public RecipeOutputInfoProvider() {

    }
    @Override
    protected Capability<IWorkable> getCapability() {
        return GregtechTileCapabilities.CAPABILITY_WORKABLE;
    }

    @Override
    protected void addProbeInfo(IWorkable capability, IProbeInfo probeInfo, EntityPlayer player, TileEntity tileEntity, IProbeHitData iProbeHitDat) {
        if (capability.getProgress() <= 0 || (!(capability instanceof AccessorAbstractRecipeLogic recipe))) return;

        // Ignore generators
        if (recipe.probe$getEUt() < 0) return;

        var itemFluidLists = createItemFluidElementLists(recipe);
        var items = itemFluidLists.getLeft();
        var fluids = itemFluidLists.getRight();

        if (items.isEmpty() && fluids.isEmpty()) return;

        boolean showDetailed = fluids.size() + items.size() <= GregicProbeConfig.itemFluidDetailLimit && !player.isSneaking();
        IProbeInfo mainPanel = probeInfo.vertical()
                .text(TranslationUtils.topTranslate("gregicprobe.top.outputs"))
                .vertical(probeInfo.defaultLayoutStyle().spacing(2));


        if (showDetailed) {
            for (var entry : items) {
                mainPanel.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .element(entry.getValue())
                        .text(TextStyleClass.INFO + entry.getKey());
            }

            for (var entry : fluids) {
                mainPanel.horizontal(new LayoutStyle().spacing(10).alignment(ElementAlignment.ALIGN_CENTER))
                        .element(entry.getValue())
                        .element(entry.getKey());
            }
            return;
        }

        boolean condense = items.size() == 1 && fluids.size() == 1;
        IProbeInfo sharedHorizontal = null;

        if (condense)
            sharedHorizontal = createHorizontalLayout(mainPanel);

        if (!items.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(items, panel, Pair::getValue);
        }

        if (!fluids.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(fluids, panel, Pair::getValue);
        }

    }
    public static String formatChance(int chance) {
        return format.format(chance / 100.0) + "%";
    }

    private <T> void addOutputs(List<T> list, IProbeInfo panel, Function<T, IElement> getElement) {
        int idx = 0;

        for (var entry : list) {
            if (idx >= AMOUNT_IN_ROW) break;

            panel.element(getElement.apply(entry));
            idx++;
        }
    }

    private Pair<List<Pair<String, ElementItemStack>>, List<Pair<FluidNameElement, FluidStackElement>>> createItemFluidElementLists(AccessorAbstractRecipeLogic recipe) {
        // Items
        var outputs = getUniqueItems(recipe.probe$getOutputs().subList(0, recipe.probe$getNonChancedItemAmt()));
        IItemStyle style = new ItemStyle().width(16).height(16);
        List<Pair<String, ElementItemStack>> items = new ArrayList<>();

        for (var output : outputs.entrySet()) {
            ItemStack stack = output.getKey().toStack(output.getValue());
            items.add(Pair.of(stack.getDisplayName(), new ElementItemStack(stack, style)));
        }

        for (var chanced : recipe.probe$getChancedItemOutputs()) {
            String display = chanced.getKey().getDisplayName() + " (" + formatChance(chanced.getValue()) + ")";
            items.add(Pair.of(display, new ChancedItemStackElement(chanced.getKey(), chanced.getValue(), style)));
        }

        // Fluids
        var fluidOutputs = getUniqueFluids(
                recipe.probe$getFluidOutputs().subList(0, recipe.probe$getNonChancedFluidAmt()));
        List<Pair<FluidNameElement, FluidStackElement>> fluids = new ArrayList<>();

        for (var output : fluidOutputs.entrySet()) {
            FluidStack stack = new FluidStack(output.getKey(), output.getValue());
            fluids.add(Pair.of(new FluidNameElement(stack, false), new FluidStackElement(stack)));
        }

        for (var chanced : recipe.probe$getChancedFluidOutputs()) {
            fluids.add(Pair.of(new ChancedFluidNameElement(chanced.getKey(), chanced.getValue(), false),
                    new ChancedFluidStackElement(chanced.getKey(), chanced.getValue())));
        }
        return Pair.of(items, fluids);
    }

    private Map<ItemMeta, Integer> getUniqueItems(List<ItemStack> stacks) {
        Map<ItemMeta, Integer> map = new Object2ObjectLinkedOpenHashMap<>();

        for (var stack : stacks) {
            if (stack.isEmpty()) continue;

            map.compute(new ItemMeta(stack), (meta, count) -> {
                if (count == null) count = 0;
                return count + stack.getCount();
            });
        }

        return map;
    }

    private Map<Fluid, Integer> getUniqueFluids(List<FluidStack> stacks) {
        Map<Fluid, Integer> map = new Object2ObjectLinkedOpenHashMap<>();

        for (var stack : stacks) {
            if (stack.amount == 0) continue;

            map.compute(stack.getFluid(), (meta, amount) -> {
                if (amount == null) amount = 0;
                return amount + stack.amount;
            });
        }

        return map;
    }

    @SideOnly(Side.CLIENT)
    public static void renderChance(int chance, int x, int y) {
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 1);
        Minecraft mc = Minecraft.getMinecraft();

        String chanceTxt = formatChance(chance);
        mc.fontRenderer.drawStringWithShadow(chanceTxt, (x + 17) * 2 - 1 - mc.fontRenderer.getStringWidth(chanceTxt),
                y * 2, 0xffffffff);

        GlStateManager.popMatrix();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();
    }


    private IProbeInfo createHorizontalLayout(IProbeInfo mainPanel) {
        return mainPanel.horizontal(new LayoutStyle().spacing(2));
    }

    @Override
    public String getID() {
        return "gregicprobe:recipe_info";
    }
}

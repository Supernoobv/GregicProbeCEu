package vfyjxf.gregicprobe.integration.gregtech;

import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IWorkable;
import gregtech.integration.theoneprobe.provider.CapabilityInfoProvider;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.styles.ItemStyle;
import mcjty.theoneprobe.apiimpl.styles.LayoutStyle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import vfyjxf.gregicprobe.config.GregicProbeConfig;
import vfyjxf.gregicprobe.element.ElementSync;
import vfyjxf.gregicprobe.element.FluidNameElement;
import vfyjxf.gregicprobe.element.FluidStackElement;
import vfyjxf.gregicprobe.mixin.helper.AccessorAbstractRecipeLogic;
import vfyjxf.gregicprobe.util.TranslationUtils;

import java.util.List;
import java.util.function.Consumer;

public class RecipeOutputInfoProvider extends CapabilityInfoProvider<IWorkable> {
    private static final int AMOUNT_IN_ROW = 10;

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

        var outputs = recipe.probe$getOutputs();
        var fluidOutputs = recipe.probe$getFluidOutputs();

        if (outputs.isEmpty() && fluidOutputs.isEmpty()) return;

        boolean showDetailed = fluidOutputs.size() + outputs.size() <= GregicProbeConfig.itemFluidDetailLimit && !player.isSneaking();
        IProbeInfo mainPanel = probeInfo.vertical()
                .text(TranslationUtils.topTranslate("gregicprobe.top.outputs"))
                .vertical(probeInfo.defaultLayoutStyle().spacing(2));

        if (showDetailed) {
            for (var item : outputs) {
                mainPanel.horizontal(new LayoutStyle().spacing(4).alignment(ElementAlignment.ALIGN_CENTER)).item(item, new ItemStyle().width(16).height(16))
                        .text(TextStyleClass.INFO + item.getDisplayName());
            }

            for (var fluid : fluidOutputs) {

                mainPanel.horizontal(new LayoutStyle().spacing(4).alignment(ElementAlignment.ALIGN_CENTER))
                        .element(new FluidStackElement(ElementSync.getElementId("fluid_stack"), fluid))
                        .element(new FluidNameElement(fluid, false, ElementSync.getElementId("fluid_name")));
            }

            return;
        }

        boolean condense = outputs.size() == 1 && fluidOutputs.size() == 1;
        IProbeInfo sharedHorizontal = null;

        if (condense)
            sharedHorizontal = createHorizontalLayout(mainPanel);

        if (!outputs.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(outputs, (stack) -> panel.item(stack, new ItemStyle().width(16).height(16)));
        }

        if (!fluidOutputs.isEmpty()) {
            IProbeInfo panel;
            if (condense)
                panel = sharedHorizontal;
            else
                panel = createHorizontalLayout(mainPanel);

            addOutputs(fluidOutputs, (stack) -> panel.element(new FluidStackElement(ElementSync.getElementId("fluid_stack"), stack)));
        }
    }

    private <T> void addOutputs(List<T> outputs, Consumer<T> addToPanel) {
        int index = 0;

        for (var output : outputs) {
            if (index >= AMOUNT_IN_ROW) break;

            addToPanel.accept(output);
            index++;
        }
    }


    private IProbeInfo createHorizontalLayout(IProbeInfo mainPanel) {
        return mainPanel.horizontal(new LayoutStyle().spacing(0));
    }

    @Override
    public String getID() {
        return "gregicprobe:recipe_info";
    }
}

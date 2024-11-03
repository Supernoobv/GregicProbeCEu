package vfyjxf.gregicprobe.integration.gregtech;

import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IWorkable;
import gregtech.api.capability.impl.AbstractRecipeLogic;
import gregtech.api.fluids.GTFluid;
import gregtech.api.util.GTUtility;
import gregtech.api.util.LocalizationUtils;
import gregtech.integration.theoneprobe.provider.CapabilityInfoProvider;
import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;
import vfyjxf.gregicprobe.config.GregicProbeConfig;
import vfyjxf.gregicprobe.element.ElementSync;
import vfyjxf.gregicprobe.element.FluidStackElement;

import java.util.ArrayList;
import java.util.List;

public class RecipeFluidOutputInfoProvider extends CapabilityInfoProvider<IWorkable> {
    public RecipeFluidOutputInfoProvider() {

    }


    @Override
    protected Capability<IWorkable> getCapability() {
        return GregtechTileCapabilities.CAPABILITY_WORKABLE;
    }

    @Override
    protected void addProbeInfo(IWorkable capability, IProbeInfo probeInfo, EntityPlayer player, TileEntity tileEntity, IProbeHitData iProbeHitDat) {
        if (capability.getProgress() > 0 && capability instanceof AbstractRecipeLogic) {
            IProbeInfo horizontalPane = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));
            List<FluidStack> fluidOutputs = new ArrayList<>(ObfuscationReflectionHelper.getPrivateValue(AbstractRecipeLogic.class, (AbstractRecipeLogic) capability, "fluidOutputs"));
            if (!fluidOutputs.isEmpty()) {
                horizontalPane.text(TextStyleClass.INFO + "{*gregicprobe.top.fluid.outputs*} ");

                int fluidsToDisplay = 2;
                if (player.isSneaking()) fluidsToDisplay = 6;

                int index = -1;
                for (FluidStack fluidOutput : fluidOutputs) {
                    if (fluidOutput != null && fluidOutput.amount > 0) {

                        index++;

                        if (index % 2 == 0 && fluidOutputs.size() <= fluidsToDisplay && fluidOutputs.size() >= 2) horizontalPane = probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER));

                        if (GregicProbeConfig.displayBukkit || !GregicProbeConfig.displayFluidName)
                            horizontalPane.element(new FluidStackElement(ElementSync.getElementId("fluid_stack"), fluidOutput, GregicProbeConfig.displayFluidQuantities));

                        if ((!GregicProbeConfig.displayFluidName || fluidOutputs.size() > fluidsToDisplay) && !GregicProbeConfig.displayFluidQuantities) {
                            if (fluidOutput.amount >= 1000) {
                                horizontalPane.text(TextStyleClass.INFO + " * " + (fluidOutput.amount / 1000) + "B");
                            } else {
                                horizontalPane.text(TextStyleClass.INFO + " * " + fluidOutput.amount + "mb");
                            }
                        }

                        if (GregicProbeConfig.displayFluidName && fluidOutputs.size() <= fluidsToDisplay && !GregicProbeConfig.displayFluidQuantities) {
                            horizontalPane.text(TextStyleClass.INFO + " {*" + fluidOutput.getLocalizedName() + "*}" + " * " + fluidOutput.amount + "mb ");
                        } else if (GregicProbeConfig.displayFluidQuantities && fluidOutputs.size() <= fluidsToDisplay) {
                                horizontalPane.text(TextStyleClass.INFO + " {*" + fluidOutput.getLocalizedName() + "*} ");
                        }
                    }
                }
            }

        }
    }


    @Override
    public String getID() {
        return "gregicprobe:recipe_info_fluid_output";
    }
}

package vfyjxf.gregicprobe.integration.gregtech;

import gregtech.api.capability.GregtechTileCapabilities;
import gregtech.api.capability.IWorkable;
import gregtech.api.capability.impl.AbstractRecipeLogic;
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
import vfyjxf.gregicprobe.config.GregicProbeConfig;
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

                for (FluidStack fluidOutput : fluidOutputs) {
                    if (fluidOutput != null && fluidOutput.amount > 0) {

                        if (GregicProbeConfig.displayBukkit || !GregicProbeConfig.displayFluidName)
                            horizontalPane.element(new FluidStackElement(fluidOutput, GregicProbeConfig.displayFluidQuantities));

                        if ((!GregicProbeConfig.displayFluidName || fluidOutputs.size() > 2) && !GregicProbeConfig.displayFluidQuantities) {
                            if (fluidOutput.amount >= 1000) {
                                horizontalPane.text(TextStyleClass.INFO + " * " + (fluidOutput.amount / 1000) + "B");
                            } else {
                                horizontalPane.text(TextStyleClass.INFO + " * " + fluidOutput.amount + "mb");
                            }
                        }

                        if (GregicProbeConfig.displayFluidName && fluidOutputs.size() <= 2 && !GregicProbeConfig.displayFluidQuantities) {
                            horizontalPane.text(TextStyleClass.INFO + " {*" + fluidOutput.getLocalizedName() + "*}" + " * " + fluidOutput.amount + "mb ");
                        } else if (GregicProbeConfig.displayFluidQuantities && fluidOutputs.size() <= 2) {
                            horizontalPane.text(TextStyleClass.INFO + " {*" + fluidOutput.getLocalizedName() + "*}");
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

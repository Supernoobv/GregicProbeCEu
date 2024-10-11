package vfyjxf.gregicprobe.integration.gregtech;

import gregtech.api.GTValues;
import gregtech.api.pipenet.tile.IPipeTile;
import gregtech.api.unification.material.properties.WireProperties;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TextFormattingUtil;
import gregtech.common.pipelike.cable.BlockCable;
import gregtech.common.pipelike.cable.Insulation;
import gregtech.common.pipelike.cable.net.EnergyNet;
import gregtech.common.pipelike.cable.tile.TileEntityCable;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.ref.WeakReference;

import static net.minecraft.util.text.TextFormatting.*;
public class CableTileInfoProvider implements IProbeInfoProvider {

    public CableTileInfoProvider() {}


    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData) {
        if (iBlockState.getBlock() instanceof BlockCable cable) {
            TileEntityCable tile = (TileEntityCable) cable.getPipeTileEntity(world, iProbeHitData.getPos());

            if (tile != null) {
                    
                double averageVoltage = tile.getAverageVoltage();
                double averageAmperage = tile.getAverageAmperage();

                double maxVoltage = tile.getMaxVoltage();

                if (averageVoltage <= 0 || averageAmperage <= 0) {
                    return;
                }

                String voltage = TextFormattingUtil.formatNumbers(averageVoltage);
                String amperage = TextFormattingUtil.formatNumbers(averageAmperage);

                String tier = GTValues.VNF[GTUtility.getTierByVoltage((long) maxVoltage)];
                iProbeInfo.horizontal().horizontal()
                    .text(I18n.format("gregicprobe.top.pipe.energy", voltage, tier, amperage));

            }

        }
    }

    @Override
    public String getID() {
        return "gregicprobe:cable_info";
    }

}

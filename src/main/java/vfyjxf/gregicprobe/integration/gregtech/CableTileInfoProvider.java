package vfyjxf.gregicprobe.integration.gregtech;

import gregtech.api.GTValues;
import gregtech.api.pipenet.tile.IPipeTile;
import gregtech.api.unification.material.properties.WireProperties;
import gregtech.api.util.GTUtility;
import gregtech.api.util.LocalizationUtils;
import gregtech.api.util.TextFormattingUtil;
import gregtech.common.pipelike.cable.BlockCable;
import gregtech.common.pipelike.cable.Insulation;
import gregtech.common.pipelike.cable.net.EnergyNet;
import gregtech.common.pipelike.cable.tile.AveragingPerTickCounter;
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
                double averageAmperage = Math.round(tile.getAverageAmperage());

                double maxAmperage = tile.getMaxAmperage();
                double maxVoltage = tile.getMaxVoltage();

                double actualAverageVoltage = Math.min(Math.round(averageVoltage / getActualAmperage(averageAmperage)), maxVoltage);

                String currentAmperage = TextFormattingUtil.formatNumbers(averageAmperage);
                String currentMaxAmperage = TextFormattingUtil.formatNumbers(maxAmperage);

                String currentTier = GTValues.VNF[GTUtility.getTierByVoltage((long) actualAverageVoltage)];
                String maxTier = GTValues.VNF[GTUtility.getTierByVoltage((long) maxVoltage)];

                iProbeInfo.horizontal().horizontal()
                    .text(LocalizationUtils.format("gregicprobe.top.pipe.energy", actualAverageVoltage, currentTier, maxTier, currentAmperage, currentMaxAmperage));

            }

        }
    }

    @Override
    public String getID() {
        return "gregicprobe:cable_info";
    }

    public static int getActualAmperage(double Amperage) {
        for (int i = 1; i < 9; i++) {
            if (Amperage <= (1 << (2 * i))) return i;
        }

        return 1;
    }

}

package vfyjxf.gregicprobe.integration;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbe;
import vfyjxf.gregicprobe.config.GregicProbeConfig;
import vfyjxf.gregicprobe.integration.gregtech.*;

public class GregicProbeCompatibility {

    public GregicProbeCompatibility() {
    }

    public static void registerCompatibility() {
        ITheOneProbe oneProbe = TheOneProbe.theOneProbeImp;
        if (GregicProbeConfig.displayItemOutputs) {
            oneProbe.registerProvider(new RecipeItemOutputInfoProvider());
        }
        if (GregicProbeConfig.displayFluidOutputs) {
            oneProbe.registerProvider(new RecipeFluidOutputInfoProvider());
        }
        if (GregicProbeConfig.displayCableAverage) {
            oneProbe.registerProvider(new CableTileInfoProvider());
        }
    }
}

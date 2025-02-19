package vfyjxf.gregicprobe.mixin;

import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class LateMixinLoader implements ILateMixinLoader {
    public static final List<String> mixinConfig = Arrays.asList("mixins.gregicprobe.gtceu.json");

    @Override
    public List<String> getMixinConfigs() {
        return mixinConfig;
    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        return switch(mixinConfig) {
            case "mixins.gregicprobe.gtceu.json" -> Loader.isModLoaded("gregtech");
            default -> true;
        };
    }
}

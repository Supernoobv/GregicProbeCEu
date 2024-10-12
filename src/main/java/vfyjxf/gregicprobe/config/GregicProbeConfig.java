package vfyjxf.gregicprobe.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vfyjxf.gregicprobe.GregicProbe;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class GregicProbeConfig {

    public static Configuration config;

    public static boolean displayItemOutputs = true;
    public static boolean displayFluidOutputs = true;
    public static boolean displayBukkit = true;
    public static boolean displayItemName = true;
    public static boolean displayFluidName = true;
    public static boolean displayFluidQuantities = true;
    public static boolean displayCableAverage = true;

    public static void initConfig(File configFile) {
        config = new Configuration(configFile);

        config.load();
        //general
        {
            displayItemOutputs = config.getBoolean("DisplayItemOutputs", "general", true, "If true, the item outputs of the current recipe will be displayed");
            displayFluidOutputs = config.getBoolean("DisplayFluidOutputs", "general", true, "If true, the fluid outputs of the current recipe will be displayed");
            displayItemName = config.getBoolean("ShowItemName", "general", true, "If true, the name of the item will be displayed");
            displayFluidName = config.getBoolean("ShowFluidName", "general", true, "If true, the name of the fluid will be displayed");
            displayFluidQuantities = config.getBoolean("ShowFluidQuantities", "general", true, "If true, The quantity of the fluid will be displayed below the fluid, instead of the right, this is useful for removing clutter.");
            displayCableAverage = config.getBoolean("DisplayCableAverages", "general", true, "If true, the average energy and amperage of a cable net will be shown.");
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (GregicProbe.MODID.equals(event.getModID())) {
            ConfigManager.sync(GregicProbe.MODID, Config.Type.INSTANCE);
        }
    }

    // to be used (eventually)
    public static Map<String, Integer> getValues(Configuration config) {
        Map<String, Integer> values = new HashMap<>();
        for (Map.Entry<String, Property> entry : config.getCategory("general").entrySet()) {
            Property prop = entry.getValue();
            switch (entry.getValue().getType()) {
                case BOOLEAN: {
                    values.put(entry.getKey(), prop.getBoolean() ? 1 : 0);
                }
                case INTEGER: {
                    values.put(entry.getKey(), prop.getInt());
                }
                case STRING: {
                    for (int i = 0; i < prop.getValidValues().length; i++) {
                        if (prop.getValidValues()[i].equals(prop.getString())) {
                            values.put(entry.getKey(), i);
                        }
                    }
                }
                default: {
                    values.put(entry.getKey(), entry.getValue().getInt());
                }
            }
        }

        return values;
    }

}

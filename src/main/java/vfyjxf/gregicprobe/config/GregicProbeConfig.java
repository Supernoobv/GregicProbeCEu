package vfyjxf.gregicprobe.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vfyjxf.gregicprobe.GregicProbe;
import vfyjxf.gregicprobe.Tags;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class GregicProbeConfig {

    public static Configuration config;

    // general
    public static boolean displayRecipeOutputs = true;
    public static boolean displayCableAverage = true;
    public static int itemFluidDetailLimit = 10;
    public static int maxEntriesToShowInRow = 12;

    // display
    public static int rowDistanceSeperation = 2;

    public static void initConfig(File configFile) {
        config = new Configuration(configFile);

        config.load();
        //general
        {
            displayRecipeOutputs = config.getBoolean("DisplayRecipeOutputs", "general", true, "If true, recipe fluid and outputs will be displayed.");
            displayCableAverage = config.getBoolean("DisplayCableAverages", "general", true, "If true, the average energy and amperage of a cable net will be shown.");
            itemFluidDetailLimit = config.getInt("ItemFluidDetailLimit", "general", 4, 1, 40, "How many items and fluids can be shown at once before it starts hiding names.");
            maxEntriesToShowInRow = config.getInt("MaxEntriesToShowInRow", "general", 12, 1, 40, "How many items and fluids are shown in seperate rows (when ItemFluidDetailLimit is exceeded)");

        }

        // display
        {
            rowDistanceSeperation = config.getInt("RowDistanceSeperation", "display", 2, 0, 10, "How far items and fluids are seperated when shown in rows.");
        }
        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (Tags.MODID.equals(event.getModID())) {
            ConfigManager.sync(Tags.MODID, Config.Type.INSTANCE);
        }
    }


}

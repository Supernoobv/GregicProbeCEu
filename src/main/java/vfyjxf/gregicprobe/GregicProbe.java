package vfyjxf.gregicprobe;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfyjxf.gregicprobe.config.GregicProbeConfig;
import vfyjxf.gregicprobe.element.ElementSync;
import vfyjxf.gregicprobe.element.FluidStackElement;
import vfyjxf.gregicprobe.integration.GregicProbeCompatibility;
import vfyjxf.gregicprobe.network.PacketHandler;
import vfyjxf.gregicprobe.network.capability.CapabilityEvents;
import vfyjxf.gregicprobe.network.capability.ClientCompability;
import vfyjxf.gregicprobe.network.capability.IClientCompability;

import java.io.File;


@Mod(
        modid = GregicProbe.MODID,
        name = GregicProbe.NAME,
        version = GregicProbe.VERSION,
        dependencies = "required-after:gregtech;required-after:theoneprobe",
        guiFactory = "vfyjxf.gregicprobe.config.GregicProbeGuiFactory",
        useMetadata = true
)
public class GregicProbe {
    public static final String MODID = "gregicprobe";
    public static final String NAME = "Gregic Probe";
    public static final String VERSION = "1.2";

    @CapabilityInject(IClientCompability.class)
    public static final Capability<IClientCompability> CLIENT_COMP = null;

    public static Configuration config;

    public static Logger logger = LogManager.getLogger(GregicProbe.NAME);

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        config = new Configuration(new File(event.getModConfigurationDirectory().getPath(), "gregicprobe.cfg"));
        GregicProbeConfig.initConfig(new File(event.getModConfigurationDirectory().getPath(), "gregicprobe.cfg"));

        ElementSync.registerElement("fluid_stack", FluidStackElement::new);

        MinecraftForge.EVENT_BUS.register(new CapabilityEvents());

        CapabilityManager.INSTANCE.register(IClientCompability.class, new ClientCompability.Storage(), ClientCompability.class);

        PacketHandler.init();
    }

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        logger.info("GregTech support loading");
        GregicProbeCompatibility.registerCompatibility();
    }
}

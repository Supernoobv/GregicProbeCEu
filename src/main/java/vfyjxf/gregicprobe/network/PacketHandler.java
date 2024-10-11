package vfyjxf.gregicprobe.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import vfyjxf.gregicprobe.Tags;

public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Tags.MODID);

    public static void init() {
        INSTANCE.registerMessage(MessageClientOptions.class, MessageClientOptions.class, 0, Side.CLIENT);
    }
}

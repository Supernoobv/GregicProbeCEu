package vfyjxf.gregicprobe.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import vfyjxf.gregicprobe.GregicProbe;
import vfyjxf.gregicprobe.element.ElementSync;

import java.util.HashMap;
import java.util.Map;

public class MessageClientOptions implements IMessage, IMessageHandler<MessageClientOptions, IMessage> {
    private Map<String, Integer> elementIds;
    private String playerUUID;

    public MessageClientOptions() {}

    public MessageClientOptions(Map<String, Integer> elementIds, EntityPlayer player) {
        this.elementIds = elementIds;
        this.playerUUID = player.getUniqueID().toString();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerUUID = ByteBufUtils.readUTF8String(buf);

        int size = buf.readInt();
        Map<String, Integer> ids = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            ids.put(ByteBufUtils.readUTF8String(buf), buf.readInt());
        }
        this.elementIds = ids;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.playerUUID);

        buf.writeInt(this.elementIds.size());
        this.elementIds.forEach((s, i) -> {
            ByteBufUtils.writeUTF8String(buf, s);
            buf.writeInt(i);
        });
    }

    @Override
    public IMessage onMessage(MessageClientOptions message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            ElementSync.elementIds.clear();
            ElementSync.elementIds.putAll(message.elementIds);
        });

        return null;
    }
}

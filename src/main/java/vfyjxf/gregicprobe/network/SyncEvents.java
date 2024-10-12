package vfyjxf.gregicprobe.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vfyjxf.gregicprobe.element.ElementSync;


public class SyncEvents {


    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            PacketHandler.INSTANCE.sendTo(new MessageClientOptions(ElementSync.elementIds, (EntityPlayer) event.getEntity()), (EntityPlayerMP) event.getEntity());
        }
    }

}

package vfyjxf.gregicprobe.network.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vfyjxf.gregicprobe.GregicProbe;
import vfyjxf.gregicprobe.Tags;
import vfyjxf.gregicprobe.config.GregicProbeConfig;
import vfyjxf.gregicprobe.element.ElementSync;
import vfyjxf.gregicprobe.network.MessageClientOptions;
import vfyjxf.gregicprobe.network.PacketHandler;

import static vfyjxf.gregicprobe.GregicProbe.CLIENT_COMP;

public class CapabilityEvents {

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation(Tags.MODID, "options"), new ICapabilityProvider() {
                private final IClientCompability instance = CLIENT_COMP.getDefaultInstance();

                @Override
                public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
                    return capability == CLIENT_COMP;
                }

                @Nullable
                @Override
                public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
                    return capability == CLIENT_COMP ? CLIENT_COMP.cast(this.instance) : null;
                }
            });
        }
    }

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityPlayer) {
            PacketHandler.INSTANCE.sendTo(new MessageClientOptions(ElementSync.elementIds, (EntityPlayer) event.getEntity()), (EntityPlayerMP) event.getEntity());
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        IClientCompability ogCapability = event.getOriginal().getCapability(CLIENT_COMP, null);
        event.getEntityPlayer().getCapability(CLIENT_COMP, null).setElementIds(ogCapability.getElementIds());
    }
}

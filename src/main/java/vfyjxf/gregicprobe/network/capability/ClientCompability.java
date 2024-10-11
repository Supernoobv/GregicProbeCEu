package vfyjxf.gregicprobe.network.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ClientCompability implements IClientCompability {
    private final Map<String, Integer> options = new HashMap<>();
    private final Map<String, Integer> elements = new HashMap<>();


    @Override
    public int getElementId(String name) {
        return elements.get(name);
    }

    @Override
    public Map<String, Integer> getElementIds() {
        return elements;
    }

    @Override
    public void setElementIds(@NotNull Map<String, Integer> elementIds) {
        elements.putAll(elementIds);
    }

    public static class Storage implements Capability.IStorage<IClientCompability> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IClientCompability> capability, IClientCompability instance, EnumFacing side) {
            NBTTagCompound tag = new NBTTagCompound();
            instance.getElementIds().forEach(tag::setInteger);
            return null;
        }

        @Override
        public void readNBT(Capability<IClientCompability> capability, IClientCompability instance, EnumFacing side, NBTBase nbt) {

        }
    }
}

package vfyjxf.gregicprobe.element;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IElementFactory;
import net.minecraft.entity.player.EntityPlayer;
import vfyjxf.gregicprobe.GregicProbe;

import java.util.HashMap;
import java.util.Map;

public class ElementSync {
    public static final Map<String, Integer> elementIds = new HashMap<>();

    public static void registerElement(String name, IElementFactory factory) {
        int id = TheOneProbe.theOneProbeImp.registerElementFactory(factory);
        elementIds.put(name, id);
    }

    public static int getElementId(String name) {
        return elementIds.get(name);
    }
}

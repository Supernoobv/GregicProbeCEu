package vfyjxf.gregicprobe.network.capability;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface IClientCompability {

    int getElementId(String name);

    void setElementIds(@NotNull Map<String, Integer> elementIds);

    Map<String, Integer> getElementIds();
}

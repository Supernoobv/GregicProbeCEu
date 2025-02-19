package vfyjxf.gregicprobe.mixin.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface AccessorAbstractRecipeLogic {
    List<ItemStack> probe$getOutputs();

    List<FluidStack> probe$getFluidOutputs();

    int probe$getEUt();
}

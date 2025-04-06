package vfyjxf.gregicprobe.mixin.helper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
/**
 * From <a href=
 * "https://github.com/Nomi-CEu/Nomi-Labs/blob/main/src/main/java/com/nomiceu/nomilabs/gregtech/mixinhelper/AccessibleAbstractRecipeLogic.java">NomiLabs</a>.
 */
public interface AccessorAbstractRecipeLogic {
    boolean probe$isValidForOutputTOP();

    List<ItemStack> probe$getOutputs();

    List<FluidStack> probe$getFluidOutputs();

    int probe$getEUt();

    int probe$getNonChancedItemAmt();

    List<Pair<ItemStack, Integer>> probe$getChancedItemOutputs();

    int probe$getNonChancedFluidAmt();

    List<Pair<FluidStack, Integer>> probe$getChancedFluidOutputs();

}

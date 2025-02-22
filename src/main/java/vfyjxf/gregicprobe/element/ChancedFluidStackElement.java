package vfyjxf.gregicprobe.element;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import vfyjxf.gregicprobe.integration.TOPManager;
import vfyjxf.gregicprobe.integration.gregtech.RecipeOutputInfoProvider;

public class ChancedFluidStackElement extends FluidStackElement {
    private final int chance;

    public ChancedFluidStackElement(@NotNull FluidStack stack, int chance) {
        super(stack);
        this.chance = chance;
    }

    public ChancedFluidStackElement(@NotNull ByteBuf buf) {
        super(buf);
        chance = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        RecipeOutputInfoProvider.renderChance(chance, x, y);
    }

    @Override
    public void toBytes(@NotNull ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(chance);
    }

    @Override
    public int getID() {
        return TOPManager.CHANCED_FLUID_STACK_ELEMENT;
    }
}

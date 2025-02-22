package vfyjxf.gregicprobe.element;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fluids.FluidStack;
import vfyjxf.gregicprobe.integration.TOPManager;
import vfyjxf.gregicprobe.integration.gregtech.RecipeOutputInfoProvider;

public class ChancedFluidNameElement extends FluidNameElement {
    private final int chance;

    public ChancedFluidNameElement(FluidStack fluid, int chance, boolean showLang) {
        super(fluid, showLang);
        this.chance = chance;
    }

    public ChancedFluidNameElement(ByteBuf byteBuf) {
        super(byteBuf);
        chance = byteBuf.readInt();
    }

    @Override
    public String getTranslated() {
        return super.getTranslated() + " (" + RecipeOutputInfoProvider.formatChance(chance) + ")";
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        super.toBytes(byteBuf);
        byteBuf.writeInt(chance);
    }

    @Override
    public int getID() {
        return TOPManager.CHANCED_FLUID_NAME_ELEMENT;
    }
}

package vfyjxf.gregicprobe.element;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.TextStyleClass;
import mcjty.theoneprobe.apiimpl.client.ElementTextRender;
import mcjty.theoneprobe.network.NetworkTools;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import vfyjxf.gregicprobe.GregicProbe;
import vfyjxf.gregicprobe.integration.TOPManager;
import vfyjxf.gregicprobe.util.TranslationUtils;

public class FluidNameElement implements IElement {

    private final String fluidName;
    private final int amount;
    private final boolean showLang;
    private final String translatedName;

    public FluidNameElement(FluidStack fluid, boolean showLang) {
        this.fluidName = fluid.getFluid().getName();
        this.amount = fluid.amount;
        this.showLang = showLang;

        this.translatedName = fluid.getUnlocalizedName();
    }

    public FluidNameElement(ByteBuf byteBuf) {
        this.fluidName = NetworkTools.readStringUTF8(byteBuf);
        this.amount = byteBuf.readInt();
        this.showLang = byteBuf.readBoolean();
        this.translatedName = translateFluid(fluidName, amount);
    }

    @Override
    public int getWidth() {
        return ElementTextRender.getWidth(getTranslated());
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void toBytes(ByteBuf byteBuf) {
        NetworkTools.writeStringUTF8(byteBuf, fluidName);
        byteBuf.writeInt(amount);
        byteBuf.writeBoolean(showLang);
    }

    @Override
    public void render(int x, int y) {
        ElementTextRender.render(TextStyleClass.NAME + getTranslated(), x, y);
    }

    @Override
    public int getID() {
        return TOPManager.FLUID_NAME_ELEMENT;
    }

    public String getTranslated() {
        if (showLang)
            return TranslationUtils.translate("gregicprobe.top.top_override.fluid", translatedName);
        else
            return translatedName;
    }

    public static String translateFluid(@Nullable String fluidName, int amount) {
        if (fluidName == null || fluidName.isEmpty()) return "";

        var fluid = FluidRegistry.getFluid(fluidName);

        if (fluid == null) {
            GregicProbe.logger.error("Received fluid info packet {} with unknown fluid {}!", "FluidNameElement", fluidName);
            return TranslationUtils.translate(fluidName);
        }

        return fluid.getLocalizedName(new FluidStack(fluid, amount));
    }
}

package vfyjxf.gregicprobe.element;

import gregtech.api.util.TextFormattingUtil;
import gregtech.client.utils.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IElement;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class FluidStackElement implements IElement {

    private static final int ID = TheOneProbe.theOneProbeImp.registerElementFactory(FluidStackElement::new);

    private final String location;
    private final int color;

    private final boolean showAmountText;
    private final int amount;
    private TextureAtlasSprite sprite = null;

    public FluidStackElement(@NotNull FluidStack stack) {
        this(stack, false);
    }

    public FluidStackElement(@NotNull FluidStack stack, boolean showAmountText) {
        this(stack.getFluid().getStill(stack), stack.getFluid().getColor(stack), stack.amount, showAmountText);
    }

    public FluidStackElement(@NotNull ResourceLocation location, int color, int amount) {
        this(location, color, amount, false);
    }

    public FluidStackElement(@NotNull ResourceLocation location, int color, int amount, boolean showAmountText) {
        this.location = location.toString();
        this.color = color;
        this.amount = amount;
        this.showAmountText = showAmountText;
    }

    public FluidStackElement(@NotNull ByteBuf buf) {
        byte[] bytes = new byte[buf.readInt()];
        buf.readBytes(bytes);
        this.location = new String(bytes, StandardCharsets.UTF_8);
        this.color = buf.readInt();
        this.amount = buf.readInt();
        this.showAmountText = buf.readBoolean();
    }

    @Override
    public void render(int x, int y) {
        if (sprite == null) {
            sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location);
        }

        GlStateManager.enableBlend();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        RenderUtil.setGlColorFromInt(color, 0xFF);
        RenderUtil.drawFluidTexture(x, y, sprite, 0, 0, 0);

        if (showAmountText && amount > 0) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5, 0.5, 1);
            Minecraft minecraft = Minecraft.getMinecraft();
            String format = TextFormattingUtil.formatLongToCompactString(amount) + "L";
            minecraft.fontRenderer.drawStringWithShadow(
                    format,
                    (x + (16 / 3F)) * 2 - minecraft.fontRenderer.getStringWidth(format) + 21,
                    (y + (16 / 3F) + 6) * 2,
                    0xFFFFFF
            );
            GlStateManager.popMatrix();
        }
        GlStateManager.disableBlend();
    }

    @Override
    public int getWidth() {
        return 18;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void toBytes(@NotNull ByteBuf buf) {
        byte[] bytes = location.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        buf.writeInt(color);
        buf.writeInt(amount);
        buf.writeBoolean(showAmountText);
    }

    @Override
    public int getID() {
        return ID;
    }
}

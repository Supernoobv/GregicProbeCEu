package vfyjxf.gregicprobe.element;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IItemStyle;
import mcjty.theoneprobe.apiimpl.elements.ElementItemStack;
import net.minecraft.item.ItemStack;
import vfyjxf.gregicprobe.integration.TOPManager;
import vfyjxf.gregicprobe.integration.gregtech.RecipeOutputInfoProvider;

public class ChancedItemStackElement extends ElementItemStack {

    private final int chance;

    public ChancedItemStackElement(ItemStack itemStack, int chance, IItemStyle style) {
        super(itemStack, style);
        this.chance = chance;
    }

    public ChancedItemStackElement(ByteBuf buf) {
        super(buf);
        chance = buf.readInt();
    }

    @Override
    public void render(int x, int y) {
        super.render(x, y);
        RecipeOutputInfoProvider.renderChance(chance, x, y);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(chance);
    }

    @Override
    public int getID() {
        return TOPManager.CHANCED_ITEM_STACK_ELEMENT;
    }
}

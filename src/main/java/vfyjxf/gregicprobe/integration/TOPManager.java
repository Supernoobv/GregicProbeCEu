package vfyjxf.gregicprobe.integration;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.ITheOneProbe;
import vfyjxf.gregicprobe.element.*;
import vfyjxf.gregicprobe.integration.gregtech.CableTileInfoProvider;
import vfyjxf.gregicprobe.integration.gregtech.RecipeOutputInfoProvider;

public class TOPManager {

    public static int FLUID_NAME_ELEMENT;
    public static int FLUID_STACK_ELEMENT;
    public static int CHANCED_ITEM_STACK_ELEMENT;
    public static int CHANCED_FLUID_STACK_ELEMENT;
    public static int CHANCED_FLUID_NAME_ELEMENT;


    public static void register() {
        ITheOneProbe TOP = TheOneProbe.theOneProbeImp;

        TOP.registerProvider(new RecipeOutputInfoProvider());
        TOP.registerProvider(new CableTileInfoProvider());

        FLUID_NAME_ELEMENT = TOP.registerElementFactory(FluidNameElement::new);
        FLUID_STACK_ELEMENT = TOP.registerElementFactory(FluidStackElement::new);
        CHANCED_ITEM_STACK_ELEMENT = TOP.registerElementFactory(ChancedItemStackElement::new);
        CHANCED_FLUID_STACK_ELEMENT = TOP.registerElementFactory(ChancedFluidStackElement::new);
        CHANCED_FLUID_NAME_ELEMENT = TOP.registerElementFactory(ChancedFluidNameElement::new);
    }
}

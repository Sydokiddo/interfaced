package net.sydokiddo.interfaced.registry.items;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.sydokiddo.interfaced.Interfaced;
import net.sydokiddo.interfaced.registry.items.custom_items.EnvironmentDetectorItem;

public class ModItems {

    public static final Item ENVIRONMENT_DETECTOR = registerItem("environment_detector", new EnvironmentDetectorItem(new Item.Properties()));

    // Registry

    @SuppressWarnings("all")
    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Interfaced.id(name), item);
    }

    public static void registerItems() {}
}
package net.sydokiddo.interfaced.registry.misc;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.sydokiddo.interfaced.registry.items.ModItems;

public class ModCreativeModeTabs {

    public static void registerCreativeTabs() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.addAfter(Items.CLOCK, ModItems.ENVIRONMENT_DETECTOR));
    }
}
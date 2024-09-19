package net.sydokiddo.interfaced.registry.misc;

import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.sydokiddo.interfaced.registry.items.ModItems;

public class ModLootTableModifiers {

    public static void modifyLootTables() {
        net.fabricmc.fabric.api.loot.v3.LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (BuiltInLootTables.VILLAGE_CARTOGRAPHER == key) tableBuilder.modifyPools(builder -> builder.with(LootItem.lootTableItem(ModItems.ENVIRONMENT_DETECTOR).setWeight(5).build()));
        });
    }
}
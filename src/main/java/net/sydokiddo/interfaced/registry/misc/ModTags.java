package net.sydokiddo.interfaced.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.sydokiddo.interfaced.Interfaced;

public class ModTags {

    // Tags

    public static final TagKey<Item>
        TOOLTIP_SPACE_BLACKLISTED = registerItemTag("tooltip_space_blacklisted")
    ;

    // Registry

    @SuppressWarnings("all")
    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, Interfaced.id(name));
    }
}
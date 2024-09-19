package net.sydokiddo.interfaced.registry.misc;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.sydokiddo.interfaced.Interfaced;

public class ModTags {

    // region Tags

    public static final TagKey<Item>
        FOOD_TOOLTIP_BLACKLISTED = registerItemTag("food_tooltip_blacklisted"),
        TOOLTIP_SPACE_BLACKLISTED = registerItemTag("tooltip_space_blacklisted")
    ;

    // endregion

    // region Registry

    @SuppressWarnings("all")
    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registries.ITEM, Interfaced.id(name));
    }

    // endregion
}
package net.sydokiddo.interfaced.mixin.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.Objects;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin extends Item {

    private CrossbowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void interfaced$addCrossbowTooltip(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {
        if (!tooltipFlag.isAdvanced() && itemStack.isDamaged() && itemStack.isEnchanted() && Objects.requireNonNull(itemStack.get(DataComponents.CHARGED_PROJECTILES)).isEmpty()) list.add(CommonComponents.EMPTY);
    }
}
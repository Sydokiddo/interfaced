package net.sydokiddo.interfaced.registry.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.interfaced.registry.misc.util.MapTooltipComponent;
import java.util.List;

public class ICommonMethods {

    // region Tooltips

    public static void addItemDurabilityTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (itemStack.isDamaged() && !tooltipFlag.isAdvanced()) {
            tooltip.add(Component.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.getMaxDamage()).withStyle(ChatFormatting.GRAY));
            if (!itemStack.is(ModTags.TOOLTIP_SPACE_BLACKLISTED)) ItemHelper.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
        }
    }

    // endregion

    // region Rendering

    @Environment(EnvType.CLIENT)
    public static void renderCompassOverlay(GuiGraphics guiGraphics) {

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gui.getDebugOverlay().showDebugScreen()) return;

        if (minecraft.getCameraEntity() != null) {

            Player player = minecraft.player;
            assert player != null;

            if (ItemHelper.hasItemInInventory(Items.COMPASS, player)) {

                BlockPos blockPos = minecraft.getCameraEntity().blockPosition();
                int heightOffset = 5;

                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();

                guiGraphics.drawString(minecraft.font, Component.translatable("gui.chrysalis.coordinates", blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, heightOffset, PlayerEntry.PLAYERNAME_COLOR, true);
                guiGraphics.drawString(minecraft.font, Component.translatable("gui.chrysalis.facing_direction", Component.translatable("gui.chrysalis.direction." + player.getDirection().getName())), 5, heightOffset + 10, PlayerEntry.PLAYERNAME_COLOR, true);

                poseStack.popPose();
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public static ClientTooltipComponent shouldRenderMapTooltip(TooltipComponent tooltipComponent) {
        if (tooltipComponent instanceof MapTooltipComponent mapTooltipComponent) return mapTooltipComponent;
        return null;
    }

    // endregion
}
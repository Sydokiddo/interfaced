package net.sydokiddo.interfaced.registry.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.interfaced.registry.misc.util.MapTooltipComponent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class ICommonMethods {

    // region Tooltips

    public static void addItemDurabilityTooltip(ItemStack itemStack, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (itemStack.isDamaged() && !tooltipFlag.isAdvanced()) {
            tooltip.add(Component.translatable("item.durability", itemStack.getMaxDamage() - itemStack.getDamageValue(), itemStack.getMaxDamage()).withStyle(ChatFormatting.GRAY));
            if (!itemStack.is(ModTags.TOOLTIP_SPACE_BLACKLISTED)) ItemHelper.addSpaceOnTooltipIfEnchantedOrTrimmed(itemStack, tooltip);
        }
    }

    public static void addFoodEffectTooltip(@NotNull List<Component> tooltip, List<FoodProperties.PossibleEffect> effects, float tickRate) {

        List<com.mojang.datafixers.util.Pair<Holder<Attribute>, AttributeModifier>> modifiers = Lists.newArrayList();

        MutableComponent mutableComponent;
        Holder<MobEffect> registryHolder;

        for (Iterator<FoodProperties.PossibleEffect> possibleEffects = effects.iterator(); possibleEffects.hasNext(); tooltip.add(mutableComponent.withStyle(registryHolder.value().getCategory().getTooltipFormatting()))) {

            FoodProperties.PossibleEffect entry = possibleEffects.next();
            MobEffectInstance mobEffectInstance = entry.effect();
            mutableComponent = Component.translatable(mobEffectInstance.getDescriptionId());
            registryHolder = mobEffectInstance.getEffect();

            registryHolder.value().createModifiers(mobEffectInstance.getAmplifier(), (attribute, modifier) -> modifiers.add(new Pair<>(attribute, modifier)));

            if (mobEffectInstance.getAmplifier() > 0) mutableComponent = Component.translatable("potion.withAmplifier", mutableComponent, Component.translatable("potion.potency." + mobEffectInstance.getAmplifier()));
            if (!mobEffectInstance.endsWithin(20)) mutableComponent = Component.translatable("potion.withDuration", mutableComponent, MobEffectUtil.formatDuration(mobEffectInstance, 1.0F, tickRate));
            if (entry.probability() < 1.0F) mutableComponent = Component.translatable("gui.interfaced.item.food.effect_chance", mutableComponent, Math.round(entry.probability() * 100));
        }

        if (!modifiers.isEmpty()) {

            tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

            for (Pair<Holder<Attribute>, AttributeModifier> modifier : modifiers) {

                AttributeModifier attributeModifier = modifier.getSecond();
                double amount = attributeModifier.amount();
                double format;

                if (attributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE && attributeModifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) format = amount;
                else format = amount * 100.0;

                if (amount > 0.0) {
                    tooltip.add(Component.translatable("attribute.modifier.plus." + attributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(format), Component.translatable(modifier.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (amount < 0.0) {
                    format *= -1.0;
                    tooltip.add(Component.translatable("attribute.modifier.take." + attributeModifier.operation().id(), ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(format), Component.translatable(modifier.getFirst().value().getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    // endregion

    // region Rendering

    @Environment(EnvType.CLIENT)
    public static void renderCompassOverlay(GuiGraphics guiGraphics) {

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gui.getDebugOverlay().showDebugScreen()) return;

        Player player = minecraft.player;
        assert player != null;

        if (minecraft.getCameraEntity() != null && ItemHelper.hasItemInInventory(Items.COMPASS, player)) {

            final int heightOffset = 5;
            assert ChatFormatting.WHITE.getColor() != null;
            final int white = ChatFormatting.WHITE.getColor();

            BlockPos blockPos = minecraft.getCameraEntity().blockPosition();

            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();

            guiGraphics.drawString(minecraft.font, Component.translatable("gui.chrysalis.coordinates", blockPos.getX(), blockPos.getY(), blockPos.getZ()), 5, heightOffset, white, true);
            guiGraphics.drawString(minecraft.font, Component.translatable("gui.chrysalis.facing_direction", Component.translatable("gui.chrysalis.direction." + player.getDirection().getName())), 5, heightOffset + 10, white, true);

            poseStack.popPose();
        }
    }

    @Environment(EnvType.CLIENT)
    public static ClientTooltipComponent shouldRenderMapTooltip(TooltipComponent tooltipComponent) {
        if (tooltipComponent instanceof MapTooltipComponent mapTooltipComponent) return mapTooltipComponent;
        return null;
    }

    public static boolean militaryTime = false;

    @SuppressWarnings("all")
    public static Component getClockComponent(int hourOutput, int hour, int minute, ChatFormatting chatFormatting) {

        Minecraft minecraft = Minecraft.getInstance();
        assert minecraft.level != null;
        Component hourSystem = CommonComponents.EMPTY;

        if (!militaryTime) {
            if (hour >= 12) hourSystem = Component.translatable("gui.interfaced.item.clock.hour_pm");
            else hourSystem = Component.translatable("gui.interfaced.item.clock.hour_am");
        }

        String standardTimeString = "gui.interfaced.item.clock.standard_time";
        String militaryTimeString = "gui.interfaced.item.clock.military_time";

        String clockNumberFormat = (minute < 10 ? "0" : "") + minute;

        if (!minecraft.level.dimensionType().hasFixedTime()) {
            if (militaryTime) return Component.translatable(militaryTimeString, hourOutput, clockNumberFormat).withStyle(chatFormatting);
            else return Component.translatable(standardTimeString, hourOutput, clockNumberFormat, hourSystem).withStyle(chatFormatting);
        } else {
            if (militaryTime) return Component.translatable(militaryTimeString, "§k00", "§k00").withStyle(ChatFormatting.OBFUSCATED).withStyle(chatFormatting);
            else return Component.translatable(standardTimeString, "§k00", "§k00", hourSystem.copy().withStyle(ChatFormatting.OBFUSCATED)).withStyle(chatFormatting);
        }
    }

    public static Component getDayComponent(ChatFormatting chatFormatting) {
        Minecraft minecraft = Minecraft.getInstance();
        assert minecraft.level != null;
        return Component.translatable("gui.interfaced.item.clock.day", minecraft.level.getDayTime() / 24000L).withStyle(chatFormatting);
    }

    // endregion
}
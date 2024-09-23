package net.sydokiddo.interfaced.mixin.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
import net.sydokiddo.interfaced.misc.config.ModConfig;
import net.sydokiddo.interfaced.registry.items.ModItems;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;
import net.sydokiddo.interfaced.registry.misc.ModTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void interfaced$addItemTooltipsBeforeEnchantments(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo info) {

        // region Durability Tooltip

        ICommonMethods.addItemDurabilityTooltip(itemStack, list, tooltipFlag);

        // endregion

        // region Food Properties Tooltip

        if (!FabricLoader.getInstance().isModLoaded("appleskin") && !itemStack.is(ModTags.FOOD_TOOLTIP_BLACKLISTED)) {

            String nutritionString = "gui.interfaced.item.food.nutrition_points";
            String saturationString = "gui.interfaced.item.food.saturation_points";

            if (itemStack.is(Items.CAKE)) {
                list.add(CommonComponents.EMPTY);
                list.add(Component.translatable("gui.interfaced.item.cake.for_each_slice").withStyle(ChatFormatting.GRAY));
                list.add(Component.translatable(nutritionString, 2).withStyle(ChatFormatting.BLUE));
                list.add(Component.translatable(saturationString, 0.4).withStyle(ChatFormatting.BLUE));
            }

            if (itemStack.getComponents().has(DataComponents.FOOD)) {

                FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
                assert foodProperties != null;

                list.add(Component.translatable(nutritionString, foodProperties.nutrition()).withStyle(ChatFormatting.BLUE));
                list.add(Component.translatable(saturationString, BigDecimal.valueOf(foodProperties.saturation()).setScale(1, RoundingMode.DOWN).stripTrailingZeros()).withStyle(ChatFormatting.BLUE));

                if (!foodProperties.effects().isEmpty()) {
                    list.add(CommonComponents.EMPTY);
                    ICommonMethods.addFoodEffectTooltip(list, foodProperties.effects(), tooltipContext.tickRate());
                }

                if (itemStack.getItem() instanceof SuspiciousStewItem && tooltipFlag.isCreative()) list.add(CommonComponents.EMPTY);
            }
        }

        // endregion

        // region Spectral Arrow Effect Tooltip

        if (!FabricLoader.getInstance().isModLoaded("monster_mash")) {

            ClientLevel clientLevel = Minecraft.getInstance().level;

            if (clientLevel != null && itemStack.is(Items.SPECTRAL_ARROW)) {
                MobEffectInstance glowing = new MobEffectInstance(MobEffects.GLOWING, 200, 0);
                list.add(Component.translatable("potion.withDuration", Component.translatable(glowing.getDescriptionId()), MobEffectUtil.formatDuration(glowing, 1.0F, clientLevel.tickRateManager().tickrate())).withStyle(ChatFormatting.BLUE));
            }
        }

        // endregion
    }

    @SuppressWarnings("all")
    @Mixin(ItemStack.class)
    public static abstract class ItemStackMixin {

        @Shadow public abstract Item getItem();
        @Shadow public abstract ItemStack copy();

        @Environment(EnvType.CLIENT)
        @Inject(method = "getTooltipLines", at = @At("TAIL"))
        private void interfaced$addItemTooltipsAfterEnchantments(Item.TooltipContext tooltipContext, Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {

            ItemStack itemStack = this.getItem().getDefaultInstance();
            Minecraft minecraft = Minecraft.getInstance();

            if (!cir.getReturnValue().isEmpty() && !tooltipFlag.isAdvanced() && minecraft.level != null && minecraft.player != null) {

                // region Compass Tooltip

                if (itemStack.is(Items.COMPASS)) {

                    boolean isLodestoneCompass = this.copy().has(DataComponents.LODESTONE_TRACKER);

                    if (isLodestoneCompass && ModConfig.lodestoneCompassTooltip) {

                        LodestoneTracker lodestoneTracker = this.copy().getComponents().get(DataComponents.LODESTONE_TRACKER);

                        cir.getReturnValue().add(CommonComponents.EMPTY);
                        cir.getReturnValue().add(Component.translatable("gui.interfaced.item.compass.lodestone_location").withStyle(ChatFormatting.GRAY));

                        if (lodestoneTracker != null && lodestoneTracker.target().isEmpty()) {
                            ItemHelper.addNullTooltip(cir.getReturnValue());
                        } else {
                            BlockPos blockPos = lodestoneTracker.target().get().pos();
                            ItemHelper.addCoordinatesTooltip(cir.getReturnValue(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            ItemHelper.addDimensionTooltip(cir.getReturnValue(), lodestoneTracker.target().get().dimension().location().toString());
                        }

                    } else if (!isLodestoneCompass && ModConfig.compassTooltip) {

                        cir.getReturnValue().add(CommonComponents.EMPTY);
                        cir.getReturnValue().add(Component.translatable("gui.interfaced.item.compass.current_location").withStyle(ChatFormatting.GRAY));
                        ItemHelper.addCoordinatesTooltip(cir.getReturnValue(), minecraft.player.getBlockX(), minecraft.player.getBlockY(), minecraft.player.getBlockZ());
                        ItemHelper.addDirectionTooltip(cir.getReturnValue(), minecraft);
                    }
                }

                // endregion

                // region Recovery Compass Tooltip

                if (itemStack.is(Items.RECOVERY_COMPASS) && ModConfig.recoveryCompassTooltip) {

                    cir.getReturnValue().add(CommonComponents.EMPTY);
                    cir.getReturnValue().add(Component.translatable("gui.interfaced.item.recovery_compass.death_location").withStyle(ChatFormatting.GRAY));

                    if (minecraft.player.getLastDeathLocation().isPresent()) {

                        GlobalPos deathPos = minecraft.player.getLastDeathLocation().get();
                        int deathX = deathPos.pos().getX();
                        int deathY = deathPos.pos().getY();
                        int deathZ = deathPos.pos().getZ();

                        ItemHelper.addCoordinatesTooltip(cir.getReturnValue(), deathX, deathY, deathZ);
                        ItemHelper.addDimensionTooltip(cir.getReturnValue(), minecraft.player.getLastDeathLocation().get().dimension().location().toString());
                    } else {
                        ItemHelper.addNullTooltip(cir.getReturnValue());
                    }
                }

                // endregion

                // region Clock Tooltip

                if (itemStack.is(Items.CLOCK) && ModConfig.clockTooltip) {

                    int maxHour = ModConfig.clockTimeFormat ? 24 : 12;

                    long time = minecraft.level.getDayTime();
                    int hour = (int) ((time / 1000L + 6L) % 24L);
                    int minute = (int) (60L * (time % 1000L) / 1000L);

                    int hourOutput;
                    if (hour <= maxHour) hourOutput = hour;
                    else hourOutput = hour - maxHour;
                    if (hourOutput == 0) hourOutput = maxHour;

                    ChatFormatting chatFormatting = ChatFormatting.BLUE;

                    cir.getReturnValue().add(ICommonMethods.getClockComponent(hourOutput, hour, minute, chatFormatting));
                    cir.getReturnValue().add(ICommonMethods.getDayComponent(chatFormatting));
                }

                // endregion

                // region Environment Detector Tooltip

                if (itemStack.is(ModItems.ENVIRONMENT_DETECTOR) && ModConfig.environmentDetectorTooltip) {

                    Holder<Biome> biome = minecraft.level.getBiome(minecraft.player.getOnPos());

                    biome.unwrapKey().ifPresent(key -> {
                        Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));
                        cir.getReturnValue().add(Component.translatable("gui.interfaced.item.environment_detector.biome", biomeName).withStyle(ChatFormatting.BLUE));
                        cir.getReturnValue().add(Component.translatable("gui.interfaced.item.environment_detector.biome_temperature", BigDecimal.valueOf(biome.value().getBaseTemperature()).setScale(1, RoundingMode.DOWN).stripTrailingZeros()).withStyle(ChatFormatting.BLUE));
                    });

                    BlockPos highestPos = new BlockPos(minecraft.player.getBlockX(), minecraft.level.getHeight(Heightmap.Types.WORLD_SURFACE, minecraft.player.getBlockX(), minecraft.player.getBlockZ()), minecraft.player.getBlockZ()).above();
                    MutableComponent weatherType;

                    if (minecraft.level.isRainingAt(highestPos)) {
                        if (minecraft.level.isThundering()) weatherType = Component.translatable("gui.chrysalis.weather.thundering");
                        else weatherType = Component.translatable("gui.chrysalis.weather.raining");
                    } else {
                        if (minecraft.level.isRaining() && biome.value().getPrecipitationAt(highestPos) == Biome.Precipitation.SNOW) weatherType = Component.translatable("gui.chrysalis.weather.snowing");
                        else weatherType = Component.translatable("gui.chrysalis.weather.clear");
                    }

                    cir.getReturnValue().add(Component.translatable("gui.interfaced.item.environment_detector.weather", weatherType.withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.BLUE));
                }

                // endregion
            }
        }
    }
}
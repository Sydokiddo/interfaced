package net.sydokiddo.interfaced.mixin.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.sydokiddo.chrysalis.misc.util.helpers.ItemHelper;
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

        // Durability Tooltip

        ICommonMethods.addItemDurabilityTooltip(itemStack, list, tooltipFlag);

        // Food Tooltips

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
                    LodestoneTracker lodestoneTracker = this.copy().getComponents().get(DataComponents.LODESTONE_TRACKER);

                    int x = 0;
                    int y = 0;
                    int z = 0;

                    cir.getReturnValue().add(CommonComponents.EMPTY);

                    if (isLodestoneCompass) {

                        cir.getReturnValue().add(Component.translatable("gui.interfaced.item.compass.lodestone_location").withStyle(ChatFormatting.GRAY));

                        if (lodestoneTracker != null && lodestoneTracker.target().isPresent()) {
                            BlockPos blockPos = lodestoneTracker.target().get().pos();
                            x = blockPos.getX();
                            y = blockPos.getY();
                            z = blockPos.getZ();
                        }

                    } else {

                        cir.getReturnValue().add(Component.translatable("gui.interfaced.item.compass.current_location").withStyle(ChatFormatting.GRAY));

                        x = minecraft.player.getBlockX();
                        y = minecraft.player.getBlockY();
                        z = minecraft.player.getBlockZ();
                    }

                    if (isLodestoneCompass && lodestoneTracker != null && lodestoneTracker.target().isEmpty()) {
                        ItemHelper.addNullTooltip(cir.getReturnValue());
                    } else {
                        ItemHelper.addCoordinatesTooltip(cir.getReturnValue(), x, y, z);
                        if (isLodestoneCompass && lodestoneTracker != null) ItemHelper.addDimensionTooltip(cir.getReturnValue(), lodestoneTracker.target().get().dimension().location().toString());
                        if (!isLodestoneCompass) ItemHelper.addDirectionTooltip(cir.getReturnValue(), minecraft);
                    }
                }

                // endregion

                // region Recovery Compass Tooltip

                if (itemStack.is(Items.RECOVERY_COMPASS)) {

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

                if (itemStack.is(Items.CLOCK)) {

                    int maxHour = ICommonMethods.militaryTime ? 24 : 12;

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

                if (itemStack.is(ModItems.ENVIRONMENT_DETECTOR)) {

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
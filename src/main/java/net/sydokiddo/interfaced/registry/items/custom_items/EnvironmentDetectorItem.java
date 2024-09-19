package net.sydokiddo.interfaced.registry.items.custom_items;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sydokiddo.interfaced.registry.misc.ModSoundEvents;
import net.sydokiddo.interfaced.registry.misc.util.EnvironmentDetectorUsedPayload;
import org.jetbrains.annotations.NotNull;

public class EnvironmentDetectorItem extends Item {

    public EnvironmentDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {

        player.playSound(ModSoundEvents.ENVIRONMENT_DETECTOR_USE, 1.0F, 1.0F + level.getRandom().nextFloat() * 0.2F);
        player.gameEvent(GameEvent.ITEM_INTERACT_START);

        if (!level.isClientSide()) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (player instanceof ServerPlayer serverPlayer) ServerPlayNetworking.send(serverPlayer, new EnvironmentDetectorUsedPayload(1));
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(interactionHand), level.isClientSide());
    }
}
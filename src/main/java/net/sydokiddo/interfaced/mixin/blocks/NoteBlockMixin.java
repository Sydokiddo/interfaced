package net.sydokiddo.interfaced.mixin.blocks;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.sydokiddo.interfaced.registry.misc.util.NoteBlockPlayedPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/NoteBlock;playNote(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void interfaced$sendNoteHUDMessageOnAttack(BlockState blockState, Level level, BlockPos blockPos, Player player, CallbackInfo info) {
        if (player instanceof ServerPlayer serverPlayer) this.sendNoteBlockPacket(serverPlayer);
    }

    @Inject(method = "useWithoutItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/NoteBlock;playNote(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void interfaced$sendNoteHUDMessageOnUse(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player instanceof ServerPlayer serverPlayer) this.sendNoteBlockPacket(serverPlayer);
    }

    @Unique
    private void sendNoteBlockPacket(ServerPlayer serverPlayer) {
        ServerPlayNetworking.send(serverPlayer, new NoteBlockPlayedPayload(1));
    }
}
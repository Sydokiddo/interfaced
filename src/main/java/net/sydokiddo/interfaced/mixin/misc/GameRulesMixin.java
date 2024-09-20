package net.sydokiddo.interfaced.mixin.misc;

import net.minecraft.world.level.GameRules;
import net.sydokiddo.chrysalis.Chrysalis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRules.class)
public abstract class GameRulesMixin {

    @Shadow public abstract <T extends GameRules.Value<T>> T getRule(GameRules.Key<T> key);

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void interfaced$changeDefaultGameruleValues(CallbackInfo info) {
        if (Chrysalis.IS_DEBUG) return;
        this.getRule(GameRules.RULE_REDUCEDDEBUGINFO).set(true, null);
    }
}
package net.sydokiddo.interfaced.mixin.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.sydokiddo.interfaced.misc.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @Inject(method = "showDebugScreen", at = @At("HEAD"), cancellable = true)
    private void interfaced$preventF3MenuRendering(CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.disableF3Menu) cir.setReturnValue(false);
    }
}
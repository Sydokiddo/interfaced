package net.sydokiddo.interfaced.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;

@Environment(EnvType.CLIENT)
public class InterfacedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (Chrysalis.CHRYSALIS_INITIALIZED) {
            HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> ICommonMethods.renderCompassOverlay(guiGraphics));
            TooltipComponentCallback.EVENT.register(ICommonMethods::shouldRenderMapTooltip);
        }
    }
}
package net.sydokiddo.interfaced.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.interfaced.Interfaced;
import net.sydokiddo.interfaced.registry.items.ModItems;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;
import net.sydokiddo.interfaced.registry.misc.util.EnvironmentDetectorUsedPayload;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class InterfacedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (Chrysalis.CHRYSALIS_INITIALIZED) {

            HudRenderCallback.EVENT.register((guiGraphics, tickDelta) -> ICommonMethods.renderCompassOverlay(guiGraphics));
            TooltipComponentCallback.EVENT.register(ICommonMethods::shouldRenderMapTooltip);

            // region Item Model Loading

            ItemProperties.register(ModItems.ENVIRONMENT_DETECTOR, Interfaced.id("type"), (itemStack, client, livingEntity, value) -> {

                final float defaultValue = 0.1F;

                if (client != null && livingEntity != null) {

                    Holder<Biome> biome = client.getBiome(livingEntity.getOnPos());

                    if (biome.is(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)) return 0.2F;
                    else if (biome.is(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)) return 0.3F;
                    else return defaultValue;
                }

                return defaultValue;
            });

            // endregion

            // region Packets

            ClientPlayNetworking.registerGlobalReceiver(EnvironmentDetectorUsedPayload.TYPE, (payload, context) -> context.client().execute(() -> {

                assert context.client().level != null;
                assert context.client().player != null;
                Holder<Biome> biome = Objects.requireNonNull(context.client().level).getBiome(Objects.requireNonNull(context.client().player).getOnPos());

                biome.unwrapKey().ifPresent(key -> {
                    Component biomeName = Component.translatable(Util.makeDescriptionId("biome", key.location()));
                    Component component = Component.translatable("gui.interfaced.item.environment_detector.biome", biomeName);

                    Minecraft.getInstance().gui.setOverlayMessage(component, false);
                    Minecraft.getInstance().getNarrator().sayNow(component);
                });
            }));

            // endregion
        }
    }
}
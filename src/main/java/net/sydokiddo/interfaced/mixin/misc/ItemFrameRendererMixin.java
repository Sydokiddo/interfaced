package net.sydokiddo.interfaced.mixin.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.sydokiddo.interfaced.misc.config.ModConfig;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ItemFrameRenderer.class)
public abstract class ItemFrameRendererMixin extends EntityRenderer<ItemFrame> {

    private ItemFrameRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Unique
    private boolean isUnnamedClock(ItemFrame itemFrame) {
        return !itemFrame.getItem().isEmpty() && itemFrame.getItem().is(Items.CLOCK) && !itemFrame.getItem().has(DataComponents.CUSTOM_NAME) && ModConfig.clockItemFrameRendering;
    }

    @Inject(method = "shouldShowName(Lnet/minecraft/world/entity/decoration/ItemFrame;)Z", at = @At("HEAD"), cancellable = true)
    private void interfaced$showClockName(ItemFrame itemFrame, CallbackInfoReturnable<Boolean> cir) {
        if (this.isUnnamedClock(itemFrame) && Minecraft.renderNames() && this.entityRenderDispatcher.crosshairPickEntity == itemFrame) {
            double distance = this.entityRenderDispatcher.distanceToSqr(itemFrame);
            float blockAmount = itemFrame.isDiscrete() ? 32.0F : 64.0F;
            cir.setReturnValue(distance < (double) (blockAmount * blockAmount));
        }
    }

    @Inject(method = "renderNameTag(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IF)V", at = @At("HEAD"), cancellable = true)
    private void interfaced$renderClockInItemFrame(ItemFrame itemFrame, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, float tickDelta, CallbackInfo info) {

        if (this.isUnnamedClock(itemFrame)) {

            info.cancel();
            if (this.entityRenderDispatcher.distanceToSqr(itemFrame) > 4096.0D) return;
            Vec3 vec3 = itemFrame.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, itemFrame.getViewYRot(tickDelta));

            if (vec3 != null && Minecraft.getInstance().level != null) {

                poseStack.pushPose();

                poseStack.translate(vec3.x(), vec3.y() + 0.5, vec3.z());
                poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                poseStack.scale(0.025F, -0.025F, 0.025F);

                Matrix4f matrix4f = poseStack.last().pose();
                float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
                int finalOpacity = (int) (opacity * 255.0F) << 24;

                int maxHour = ModConfig.clockTimeFormat ? 24 : 12;
                long time = Minecraft.getInstance().level.getDayTime();
                int hour = (int) ((time / 1000L + 6L) % 24L);
                int minute = (int) (60L * (time % 1000L) / 1000L);

                int hourOutput;
                if (hour <= maxHour) hourOutput = hour;
                else hourOutput = hour - maxHour;
                if (hourOutput == 0) hourOutput = maxHour;

                Font font = this.getFont();
                final int xPos = -32;
                final int yPos = -20;
                ChatFormatting chatFormatting = ChatFormatting.WHITE;

                font.drawInBatch(ICommonMethods.getClockComponent(hourOutput, hour, minute, chatFormatting), xPos, yPos, 0, false, matrix4f, multiBufferSource, Font.DisplayMode.SEE_THROUGH, finalOpacity, light);
                font.drawInBatch(ICommonMethods.getClockComponent(hourOutput, hour, minute, chatFormatting), xPos, yPos, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, light);

                font.drawInBatch(ICommonMethods.getDayComponent(chatFormatting), xPos, yPos + 10, 0, false, matrix4f, multiBufferSource, Font.DisplayMode.SEE_THROUGH, finalOpacity, light);
                font.drawInBatch(ICommonMethods.getDayComponent(chatFormatting), xPos, yPos + 10, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, light);

                font.drawInBatch(ICommonMethods.getMoonPhaseComponent(chatFormatting), xPos, yPos + 20, 0, false, matrix4f, multiBufferSource, Font.DisplayMode.SEE_THROUGH, finalOpacity, light);
                font.drawInBatch(ICommonMethods.getMoonPhaseComponent(chatFormatting), xPos, yPos + 20, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.NORMAL, 0, light);

                poseStack.popPose();
            }
        }
    }
}
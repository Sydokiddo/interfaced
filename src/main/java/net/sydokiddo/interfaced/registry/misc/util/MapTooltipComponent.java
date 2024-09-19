package net.sydokiddo.interfaced.registry.misc.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class MapTooltipComponent implements ClientTooltipComponent, TooltipComponent {

    private final ResourceLocation map_background = ResourceLocation.withDefaultNamespace("textures/map/map_background.png");
    private final MapId mapId;
    private final MapItemSavedData mapData;

    public MapTooltipComponent(ItemStack itemStack) {
        this.mapId = itemStack.get(DataComponents.MAP_ID);
        this.mapData = MapItem.getSavedData(itemStack, Minecraft.getInstance().level);
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics guiGraphics) {

        if (Minecraft.getInstance().level == null || this.mapId == null || this.mapData == null) return;
        final PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        guiGraphics.blit(this.map_background, x, y, 0, 0, 64, 64, 64, 64);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(x + 3.2F, y + 3.2F, 401);
        poseStack.scale(0.45F, 0.45F, 1);
        Minecraft.getInstance().gameRenderer.getMapRenderer().render(poseStack, guiGraphics.bufferSource(), this.mapId, this.mapData, true, 15728880);
        poseStack.popPose();
    }

    @Override
    public int getHeight() {
        return 72;
    }

    @Override
    public int getWidth(Font font) {
        return 66;
    }
}
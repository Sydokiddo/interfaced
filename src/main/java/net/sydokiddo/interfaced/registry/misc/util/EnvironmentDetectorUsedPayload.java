package net.sydokiddo.interfaced.registry.misc.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record EnvironmentDetectorUsedPayload(int protocolVersion) implements CustomPacketPayload {

    public static final Type<EnvironmentDetectorUsedPayload> TYPE = CustomPacketPayload.createType("environment_detector_used");
    public static final StreamCodec<RegistryFriendlyByteBuf, EnvironmentDetectorUsedPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, EnvironmentDetectorUsedPayload::protocolVersion, EnvironmentDetectorUsedPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
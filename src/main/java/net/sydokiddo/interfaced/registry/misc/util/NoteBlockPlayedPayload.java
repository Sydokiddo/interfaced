package net.sydokiddo.interfaced.registry.misc.util;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record NoteBlockPlayedPayload(int protocolVersion) implements CustomPacketPayload {

    public static final Type<NoteBlockPlayedPayload> TYPE = CustomPacketPayload.createType("note_block_played");
    public static final StreamCodec<RegistryFriendlyByteBuf, NoteBlockPlayedPayload> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, NoteBlockPlayedPayload::protocolVersion, NoteBlockPlayedPayload::new);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
package compasses.expandedstorage.impl.item;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public record MutatorData(MutationMode mode, Optional<BlockPos> pos) {
    public static final MapCodec<MutatorData> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(
                MutationMode.CODEC.fieldOf("mode").forGetter(MutatorData::mode),
                BlockPos.CODEC.optionalFieldOf("pos").forGetter(MutatorData::pos)
        ).apply(instance, MutatorData::new);
    });
    public static StreamCodec<FriendlyByteBuf, MutatorData> STREAM_CODEC = StreamCodec.of(MutatorData::encode, MutatorData::decode);

    private static MutatorData decode(FriendlyByteBuf buffer) {
        MutationMode mode = buffer.readEnum(MutationMode.class);
        Optional<BlockPos> pos = buffer.readOptional(BlockPos.STREAM_CODEC);
        return new MutatorData(mode, pos);
    }

    private static void encode(FriendlyByteBuf buffer, MutatorData data) {
        buffer.writeEnum(data.mode);
        buffer.writeOptional(data.pos, BlockPos.STREAM_CODEC);
    }
}

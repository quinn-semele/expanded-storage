package compasses.expandedstorage.impl.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum MutationMode implements StringRepresentable {
    MERGE,
    SPLIT,
    ROTATE,
    SWAP_THEME;

    public static final Codec<MutationMode> CODEC = StringRepresentable.fromValues(MutationMode::values);

    private static final MutationMode[] VALUES = MutationMode.values();

    public static MutationMode from(byte index) {
        if (index >= 0 && index < MutationMode.VALUES.length) {
            return MutationMode.VALUES[index];
        }
        return null;
    }

    public byte toByte() {
        return (byte) this.ordinal();
    }

    @Override
    public String toString() {
        return this.name().toLowerCase(Locale.ROOT);
    }

    public MutationMode next() {
        return MutationMode.VALUES[(ordinal() + 1) % MutationMode.VALUES.length];
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return toString();
    }
}

package compasses.expandedstorage.impl.misc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record InventoryOpeningData(int slots, ResourceLocation forcedScreenType) {
    public static final StreamCodec<RegistryFriendlyByteBuf, InventoryOpeningData> CODEC = StreamCodec.of(InventoryOpeningData::encode, InventoryOpeningData::decode);

    private static InventoryOpeningData decode(RegistryFriendlyByteBuf buffer) {
        int slots = buffer.readInt();
        ResourceLocation forcedScreenType = buffer.readNullable(FriendlyByteBuf::readResourceLocation);

        return new InventoryOpeningData(slots, forcedScreenType);
    }

    private static void encode(RegistryFriendlyByteBuf buffer, InventoryOpeningData data) {
        buffer.writeInt(data.slots);
        buffer.writeNullable(data.forcedScreenType, FriendlyByteBuf::writeResourceLocation);
    }
}

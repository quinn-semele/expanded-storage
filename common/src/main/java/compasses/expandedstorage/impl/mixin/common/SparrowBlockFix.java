package compasses.expandedstorage.impl.mixin.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkSerializer.class)
public abstract class SparrowBlockFix {
    @Inject(
            method = "read(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/ai/village/poi/PoiManager;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/level/chunk/ProtoChunk;",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/Codec;parse(Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;",
                    ordinal = 0,
                    remap = false
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void expandedstorage$replaceSparrowBlocks(ServerLevel level, PoiManager poiManager, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir, ChunkPos chunkPos, UpgradeData upgradeData, boolean bl, ListTag listTag, int i, LevelChunkSection[] levelChunkSections, boolean bl2, ChunkSource chunkSource, LevelLightEngine levelLightEngine, Registry registry, Codec codec, boolean bl3, int j, CompoundTag compoundTag, int k, int l) {
        CompoundTag blockState = compoundTag.getCompound("block_states");
        if (blockState.contains("palette", Tag.TAG_LIST)) {
            ListTag paletteTag = (ListTag) blockState.get("palette");
            if (paletteTag.getElementType() == Tag.TAG_COMPOUND) {
                for (Tag t : paletteTag) {
                    CompoundTag palletEntry = (CompoundTag) t;
                    String blockId = palletEntry.getString("Name");
                    if (blockId.startsWith("expandedstorage:") && blockId.endsWith("_with_sparrow")) {
                        palletEntry.putString("Name", blockId.substring(0, blockId.length() - 13));
                        CompoundTag fixedTag = new CompoundTag();
                        fixedTag.putString("sparrow", "true");
                        palletEntry.getCompound("Properties").merge(fixedTag);
                    }
                }
            }
        }
    }
}

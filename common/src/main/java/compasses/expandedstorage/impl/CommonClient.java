package compasses.expandedstorage.impl;

import compasses.expandedstorage.impl.block.MiniStorageBlock;
import compasses.expandedstorage.impl.item.MutationMode;
import compasses.expandedstorage.impl.item.StorageMutator;
import compasses.expandedstorage.impl.misc.ClientPlatformHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CommonClient {
    private static ClientPlatformHelper platformHelper;

    public static void initialize(ClientPlatformHelper helper) {
        platformHelper = helper;
    }

    @SuppressWarnings("unused")
    public static float hasSparrowProperty(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i) {
        return MiniStorageBlock.hasSparrowProperty(stack) ? 1.0f : 0.0f;
    }

    @SuppressWarnings("unused")
    public static float currentMutatorToolMode(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int i) {
        MutationMode mode = StorageMutator.getMode(stack);
        boolean isSparrow = stack.hasCustomHoverName() && stack.getHoverName().getString().equalsIgnoreCase("sparrow");
        if (mode == MutationMode.SWAP_THEME) {
            if (isSparrow) {
                return 1.0F;
            }
            return 0.8F;
        } else if (mode == MutationMode.ROTATE) {
            return 0.6F;
        } else if (mode == MutationMode.SPLIT) {
            return 0.4F;
        } else if (mode == MutationMode.MERGE) {
            return 0.2F;
        }
        return 0.0F;
    }

    public static ClientPlatformHelper platformHelper() {
        return platformHelper;
    }
}

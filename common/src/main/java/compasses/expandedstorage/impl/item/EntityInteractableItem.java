package compasses.expandedstorage.impl.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface EntityInteractableItem {
    InteractionResult es_interactEntity(Level level, Entity entity, Player player, InteractionHand hand, ItemStack stack);
}

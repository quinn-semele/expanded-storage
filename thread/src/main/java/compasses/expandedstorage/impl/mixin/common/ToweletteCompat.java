package compasses.expandedstorage.impl.mixin.common;

import compasses.expandedstorage.impl.block.ChestBlock;
import compasses.expandedstorage.impl.block.MiniStorageBlock;
import org.spongepowered.asm.mixin.Mixin;
import virtuoel.towelette.api.Fluidloggable;

@SuppressWarnings("unused")
@Mixin({ChestBlock.class, MiniStorageBlock.class})
public abstract class ToweletteCompat implements Fluidloggable {

}

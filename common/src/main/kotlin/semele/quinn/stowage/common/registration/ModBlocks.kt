package semele.quinn.stowage.common.registration

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import semele.quinn.stowage.common.Utils
import semele.quinn.stowage.common.barrel.BarrelBlock
import semele.quinn.stowage.common.barrel.CopperBarrelBlock
import semele.quinn.stowage.common.old_chest.CopperOldChestBlock
import semele.quinn.stowage.common.old_chest.OldChestBlock

object ModBlocks {
    val COPPER_BARREL: CopperBarrelBlock = block(Utils.id("copper_barrel"))
    val EXPOSED_COPPER_BARREL: CopperBarrelBlock = block(Utils.id("exposed_copper_barrel"))
    val WEATHERED_COPPER_BARREL: CopperBarrelBlock = block(Utils.id("weathered_copper_barrel"))
    val OXIDIZED_COPPER_BARREL: CopperBarrelBlock = block(Utils.id("oxidized_copper_barrel"))
    val WAXED_COPPER_BARREL: BarrelBlock = block(Utils.id("waxed_copper_barrel"))
    val WAXED_EXPOSED_COPPER_BARREL: BarrelBlock = block(Utils.id("waxed_exposed_copper_barrel"))
    val WAXED_WEATHERED_COPPER_BARREL: BarrelBlock = block(Utils.id("waxed_weathered_copper_barrel"))
    val WAXED_OXIDIZED_COPPER_BARREL: BarrelBlock = block(Utils.id("waxed_oxidized_copper_barrel"))

    val OLD_COPPER_CHEST: CopperOldChestBlock = block(Utils.id("old_copper_chest"))
    val OLD_EXPOSED_COPPER_CHEST: CopperOldChestBlock = block(Utils.id("old_exposed_copper_chest"))
    val OLD_WEATHERED_COPPER_CHEST: CopperOldChestBlock = block(Utils.id("old_weathered_copper_chest"))
    val OLD_OXIDIZED_COPPER_CHEST: CopperOldChestBlock = block(Utils.id("old_oxidized_copper_chest"))
    val WAXED_OLD_COPPER_CHEST: OldChestBlock = block(Utils.id("waxed_old_copper_chest"))
    val WAXED_OLD_EXPOSED_COPPER_CHEST: OldChestBlock = block(Utils.id("waxed_old_exposed_copper_chest"))
    val WAXED_OLD_WEATHERED_COPPER_CHEST: OldChestBlock = block(Utils.id("waxed_old_weathered_copper_chest"))
    val WAXED_OLD_OXIDIZED_COPPER_CHEST: OldChestBlock = block(Utils.id("waxed_old_oxidized_copper_chest"))

    @Suppress("UNCHECKED_CAST")
    private fun <T : Block> block(id: ResourceLocation): T {
        return BuiltInRegistries.BLOCK[id] as T
    }
}
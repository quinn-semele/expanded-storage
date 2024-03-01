/*
 * Copyright 2024 Quinn Semele
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package semele.quinn.stowage.common.old_chest

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import semele.quinn.stowage.api.StowageChestType

class OldChestBlock(
    properties: Properties,
    val openingStat: ResourceLocation,
    val slots: Int
) : Block(properties), EntityBlock {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(CHEST_TYPE, StowageChestType.SINGLE)
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)

        builder.add(CHEST_TYPE, BlockStateProperties.HORIZONTAL_FACING)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        if (state.getValue(CHEST_TYPE) == StowageChestType.SINGLE) {
            return state.setValue(
                BlockStateProperties.HORIZONTAL_FACING,
                rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING))
            )
        }

        return super.rotate(state, rotation)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.HORIZONTAL_FACING)))
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return OldChestBlockEntity.blockEntityType.create(pos, state)
    }

    companion object {
        val CHEST_TYPE: EnumProperty<StowageChestType> = EnumProperty.create("type", StowageChestType::class.java)
    }
}
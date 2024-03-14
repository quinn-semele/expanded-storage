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

package semele.quinn.stowage.common.barrel

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties

open class BarrelBlock(
    properties: Properties,
    val openingStat: ResourceLocation,
    val slots: Int
) : Block(properties), EntityBlock {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(BlockStateProperties.FACING, Direction.UP)
                .setValue(BlockStateProperties.OPEN, false)
        )
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        return defaultBlockState().setValue(BlockStateProperties.FACING, context.nearestLookingDirection.opposite)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)

        builder.add(BlockStateProperties.FACING, BlockStateProperties.OPEN)
    }

    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        return state.setValue(
            BlockStateProperties.FACING,
            rotation.rotate(state.getValue(BlockStateProperties.FACING))
        )
    }

    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(BlockStateProperties.FACING)))
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return BarrelBlockEntity.blockEntityType.create(pos, state)
    }
}
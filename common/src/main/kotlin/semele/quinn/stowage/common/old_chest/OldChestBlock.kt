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
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DoubleBlockCombiner.BlockType
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING as FACING
import net.minecraft.world.level.block.state.properties.EnumProperty
import semele.quinn.stowage.api.StowageChestType
import semele.quinn.stowage.common.Utils.isBlock
import java.lang.IllegalStateException

open class OldChestBlock(
    properties: Properties,
    val openingStat: ResourceLocation,
    val slots: Int
) : Block(properties), EntityBlock {
    init {
        registerDefaultState(
            defaultBlockState()
                .setValue(CHEST_TYPE, StowageChestType.SINGLE)
                .setValue(FACING, Direction.NORTH)
        )
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)

        builder.add(CHEST_TYPE, FACING)
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
        val level = context.level
        val pos = context.clickedPos

        val chestFacing = context.horizontalDirection.opposite
        var chestType = StowageChestType.SINGLE

        if (context.isSecondaryUseActive) {
            val clickedDirection: Direction = context.clickedFace.opposite
            val clickedState: BlockState = level.getBlockState(pos.relative(clickedDirection))

            if (isValidMergeTarget(clickedState, chestFacing)) {
                chestType = getChestTypeForNeighbour(chestFacing, clickedDirection)
            }
        } else {
            for (direction in Direction.entries) {
                val offsetState = level.getBlockState(pos.relative(direction))

                if (isValidMergeTarget(offsetState, chestFacing)) {
                    chestType = getChestTypeForNeighbour(chestFacing, direction)
                    break
                }
            }
        }

        return defaultBlockState().setValue(FACING, chestFacing).setValue(CHEST_TYPE, chestType)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos
    ): BlockState {
        val blockType = getBlockType(state.getValue(CHEST_TYPE))

        if (blockType == BlockType.SINGLE) {
            val newState = state.setValue(CHEST_TYPE, getChestTypeForNeighbour(state.getValue(FACING), direction))

            if (isValidDoubleChest(newState, neighborState)) {
                return newState
            }
        } else {
            val otherChest = level.getBlockState(pos.relative(getDirectionToAttached(state)))

            val newState = checkForOxidisation(state, otherChest)

            return if (!isValidDoubleChest(newState, otherChest)) {
                state.setValue(CHEST_TYPE, StowageChestType.SINGLE)
            } else {
                newState
            }
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    protected open fun checkForOxidisation(chest: BlockState, otherChest: BlockState): BlockState {
        return chest
    }

    private fun isValidMergeTarget(state: BlockState, requiredFacing: Direction): Boolean {
        return state.isBlock(this) &&
                state.getValue(FACING) == requiredFacing &&
                state.getValue(CHEST_TYPE) == StowageChestType.SINGLE
    }

    /**
     * @param chest A known good half of a double chest whose type isn't [StowageChestType.SINGLE].
     */
    private fun isValidDoubleChest(chest: BlockState, otherState: BlockState): Boolean {
        return otherState.isBlock(chest.block) &&
                chest.getValue(FACING) == otherState.getValue(FACING) &&
                chest.getValue(CHEST_TYPE).opposite() == otherState.getValue(CHEST_TYPE)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun rotate(state: BlockState, rotation: Rotation): BlockState {
        if (state.getValue(CHEST_TYPE) == StowageChestType.SINGLE) {
            return state.setValue(FACING, rotation.rotate(state.getValue(FACING)))
        }

        return super.rotate(state, rotation)
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        return state.rotate(mirror.getRotation(state.getValue(FACING)))
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
        return OldChestBlockEntity.blockEntityType.create(pos, state)
    }

    companion object {
        fun getChestTypeForNeighbour(chestForwardDir: Direction, chestOffsetDir: Direction): StowageChestType {
            if (chestForwardDir.clockWise == chestOffsetDir) {
                return StowageChestType.RIGHT
            } else if (chestForwardDir.counterClockWise == chestOffsetDir) {
                return StowageChestType.LEFT
            } else if (chestForwardDir == chestOffsetDir) {
                return StowageChestType.BACK
            } else if (chestForwardDir == chestOffsetDir.opposite) {
                return StowageChestType.FRONT
            } else if (chestOffsetDir == Direction.DOWN) {
                return StowageChestType.TOP
            } else if (chestOffsetDir == Direction.UP) {
                return StowageChestType.BOTTOM
            }
            return StowageChestType.SINGLE
        }

        fun getBlockType(type: StowageChestType): BlockType = when(type) {
            StowageChestType.TOP, StowageChestType.FRONT, StowageChestType.LEFT -> BlockType.FIRST
            StowageChestType.BOTTOM, StowageChestType.BACK, StowageChestType.RIGHT -> BlockType.SECOND
            StowageChestType.SINGLE -> BlockType.SINGLE
        }

        fun getDirectionToAttached(state: BlockState): Direction {
            return getDirectionToAttached(state.getValue(CHEST_TYPE), state.getValue(FACING))
        }

        private fun getDirectionToAttached(chestType: StowageChestType, facing: Direction): Direction = when(chestType) {
            StowageChestType.TOP -> Direction.DOWN
            StowageChestType.BOTTOM -> Direction.UP
            StowageChestType.FRONT -> facing.opposite
            StowageChestType.BACK -> facing
            StowageChestType.LEFT -> facing.counterClockWise
            StowageChestType.RIGHT -> facing.clockWise
            StowageChestType.SINGLE -> throw IllegalStateException("StowageChestType.SINGLE has no attached direction.")
        }

        val CHEST_TYPE: EnumProperty<StowageChestType> = EnumProperty.create("type", StowageChestType::class.java)
    }
}
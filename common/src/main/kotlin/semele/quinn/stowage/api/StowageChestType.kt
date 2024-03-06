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

package semele.quinn.stowage.api

import net.minecraft.util.StringRepresentable
import java.lang.IllegalStateException

enum class StowageChestType : StringRepresentable {
    TOP,
    BOTTOM,
    FRONT,
    BACK,
    LEFT,
    RIGHT,
    SINGLE;

    fun opposite(): StowageChestType = when(this) {
        TOP -> BOTTOM
        BOTTOM -> TOP
        FRONT -> BACK
        BACK -> FRONT
        LEFT -> RIGHT
        RIGHT -> LEFT
        SINGLE -> throw IllegalStateException("StowageChestType.SINGLE has no opposite.")
    }

    override fun getSerializedName(): String = name.lowercase()
}

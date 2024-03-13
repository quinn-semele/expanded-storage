/*
 * Copyright 2024 Quinn Semele
 *
 * Use of this source code is governed by the MIT license
 * which can be found at https://opensource.org/licenses/MIT.
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

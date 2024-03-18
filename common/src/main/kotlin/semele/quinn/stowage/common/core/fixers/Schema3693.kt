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

package semele.quinn.stowage.common.core.fixers

import com.mojang.datafixers.DSL
import com.mojang.datafixers.schemas.Schema
import com.mojang.datafixers.types.templates.TypeTemplate
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.datafix.fixes.References
import net.minecraft.util.datafix.schemas.NamespacedSchema
import semele.quinn.stowage.common.Utils
import java.util.function.Supplier

class Schema3693(versionKey: Int, parent: Schema) : NamespacedSchema(versionKey, parent) {
    override fun registerBlockEntities(schema: Schema): MutableMap<String, Supplier<TypeTemplate>> {
        val map = super.registerBlockEntities(schema)

        registerInventory(schema, map, Utils.CHEST_CONTENT)
        registerInventory(schema, map, Utils.BARREL_CONTENT)
        registerInventory(schema, map, Utils.OLD_CHEST_CONTENT)
        registerInventory(schema, map, Utils.MINI_STORAGE_CONTENT)

        return map
    }

    private fun registerInventory(schema: Schema, map: MutableMap<String, Supplier<TypeTemplate>>, id: ResourceLocation) {
        schema.register(map, id.toString()) { ->
            DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.`in`(schema)))
        }
    }
}
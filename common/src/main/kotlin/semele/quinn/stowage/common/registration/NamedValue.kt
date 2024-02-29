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

package semele.quinn.stowage.common.registration

import net.minecraft.resources.ResourceLocation
import java.util.function.Supplier

class NamedValue<T>(
    val name: ResourceLocation,
    val supplier: Supplier<T>
) {
    val value by lazy {
        supplier.get()
    }

    operator fun component1(): ResourceLocation = name
    operator fun component2(): T = value
}

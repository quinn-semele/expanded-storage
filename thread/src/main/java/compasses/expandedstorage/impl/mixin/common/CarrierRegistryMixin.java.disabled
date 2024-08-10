package compasses.expandedstorage.impl.mixin.common;

import com.google.common.collect.HashBiMap;
import compasses.expandedstorage.impl.compat.carrier.AccessibleCarrierRegistry;
import me.steven.carrier.api.Carriable;
import me.steven.carrier.api.CarriableRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = CarriableRegistry.class, remap = false)
public class CarrierRegistryMixin implements AccessibleCarrierRegistry {
    @Final
    @Shadow(remap = false)
    private HashBiMap<ResourceLocation, Carriable<?>> idToEntry;

    @Final
    @Shadow(remap = false)
    private HashBiMap<Object, Carriable<?>> objToEntry;

    @Override
    public void expandedstorage$removeEntry(ResourceLocation id) {
        var entry = idToEntry.remove(id);

        if (entry != null) {
            objToEntry.inverse().remove(entry);
        }
    }
}

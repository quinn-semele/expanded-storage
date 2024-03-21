package compasses.expandedstorage.impl.mixin.common;

import com.google.common.collect.HashBiMap;
import compasses.expandedstorage.impl.compat.carrier.AccessibleCarrierRegistry;
import me.steven.carrier.api.Carriable;
import me.steven.carrier.api.CarriableRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CarriableRegistry.class)
public class CarrierRegistryMixin implements AccessibleCarrierRegistry {
    @Shadow(remap = false)
    @Final
    private HashBiMap<ResourceLocation, Carriable<?>> idToEntry;

    @Shadow(remap = false)
    @Final
    private HashBiMap<Object, Carriable<?>> objToEntry;

    @Override
    public void expandedstorage$removeEntry(ResourceLocation id) {
        var entry = idToEntry.remove(id);

        if (entry != null) {
            objToEntry.inverse().remove(entry);
        }
    }
}

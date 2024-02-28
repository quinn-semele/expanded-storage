package semele.quinn.stowage.neoforge

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import semele.quinn.stowage.common.Utils

@Mod("stowage")
class Main(container: ModContainer, bus: IEventBus) {
    init {
        Utils.LOGGER.info("Hello from Stowage. (NeoForge)")
    }
}
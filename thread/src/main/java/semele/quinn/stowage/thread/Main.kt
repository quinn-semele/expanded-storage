package semele.quinn.stowage.thread

import net.fabricmc.api.ModInitializer
import semele.quinn.stowage.common.Utils

object Main : ModInitializer {
    override fun onInitialize() {
        Utils.LOGGER.info("Hello from Stowage. (Fabric/Quilt)")
    }
}
package semele.quinn.stowage.thread;

import net.fabricmc.api.ModInitializer;
import semele.quinn.stowage.common.Utils;

public class Main implements ModInitializer {
    @Override
    public void onInitialize() {
        Utils.LOGGER.info("Hello from Stowage. (Fabric/Quilt)");
    }
}

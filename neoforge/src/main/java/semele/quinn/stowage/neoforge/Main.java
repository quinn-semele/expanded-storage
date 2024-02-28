package semele.quinn.stowage.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import semele.quinn.stowage.common.Utils;

@Mod("stowage")
public class Main {
    public Main(ModContainer container, IEventBus bus) {
        Utils.LOGGER.info("Hello from Stowage. (NeoForge)");
    }
}

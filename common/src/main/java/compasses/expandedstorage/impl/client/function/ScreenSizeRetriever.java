package compasses.expandedstorage.impl.client.function;

import compasses.expandedstorage.impl.client.function.ScreenSize;
import org.jetbrains.annotations.Nullable;

public interface ScreenSizeRetriever {
    /**
     * Return null if this screen cannot display the number of slots on a scaledWidth x scaledHeight sized screen.
     */
    @Nullable
    ScreenSize get(int slots, int scaledWidth, int scaledHeight);
}

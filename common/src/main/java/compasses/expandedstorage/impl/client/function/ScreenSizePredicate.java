package compasses.expandedstorage.impl.client.function;

public interface ScreenSizePredicate {
    @SuppressWarnings("unused")
    static boolean noTest(int scaledWidth, int scaledHeight) {
        return false;
    }

    boolean test(int scaledWidth, int scaledHeight);
}

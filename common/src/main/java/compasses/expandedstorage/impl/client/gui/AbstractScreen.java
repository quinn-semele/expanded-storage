package compasses.expandedstorage.impl.client.gui;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import compasses.expandedstorage.impl.CommonClient;
import compasses.expandedstorage.impl.client.helpers.ErrorlessTextureGetter;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.client.function.ScreenSize;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractScreen extends AbstractContainerScreen<AbstractHandler> {
    protected final int inventoryWidth, inventoryHeight, totalSlots;
    protected final ResourceLocation textureLocation;
    protected final int textureWidth, textureHeight;

    protected AbstractScreen(AbstractHandler handler, Inventory playerInventory, Component title, ScreenSize screenSize) {
        super(handler, playerInventory, title);
        totalSlots = handler.getInventory().getContainerSize();
        inventoryWidth = screenSize.getWidth();
        inventoryHeight = screenSize.getHeight();
        textureLocation = this instanceof MiniStorageScreen ?
                Utils.id("textures/gui/container/mini_chest_screen.png") :
                Utils.id("textures/gui/container/shared_" + inventoryWidth + "_" + inventoryHeight + ".png");

        if (!(this instanceof FakePickScreen)) {
            boolean isTexturePresent = ((ErrorlessTextureGetter) Minecraft.getInstance().getTextureManager()).expandedstorage$isTexturePresent(textureLocation);

            if (!isTexturePresent && !(this instanceof MiniStorageScreen)) {
                int guiWidth = 36 + Utils.SLOT_SIZE * inventoryWidth;
                int guiHeight = 132 + Utils.SLOT_SIZE * inventoryHeight;
                int textureWidth = (int) (Math.ceil(guiWidth / 16.0f) * 16);
                int textureHeight = (int) (Math.ceil(guiHeight / 16.0f) * 16);

                RenderSystem.setShaderTexture(0, Utils.id("textures/gui/container/atlas_gen.png"));
                RenderSystem.bindTexture(RenderSystem.getShaderTexture(0));
                try (NativeImage atlas = new NativeImage(96, 96, false)) {
                    atlas.downloadTexture(0, false);
                    try (NativeImage image = new NativeImage(textureWidth, textureHeight, false)) {
                        image.fillRect(0, 0, textureWidth, textureHeight, 0x00FFFFFF);

                        AbstractScreen.renderGui(inventoryWidth, inventoryHeight, (destX, destY, w, h, srcX, srcY) -> {
                            atlas.copyRect(image, srcX, srcY, destX, destY, w, h, false, false);
                        });

                        DynamicTexture texture = new DynamicTexture(image);
                        Minecraft.getInstance().getTextureManager().register(textureLocation, texture);
                    }
                }
            }

            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureLocation);

            if (texture instanceof DynamicTexture dynamicTexture) {
                textureWidth = dynamicTexture.getPixels().getWidth();
                textureHeight = dynamicTexture.getPixels().getHeight();
            } else if (texture instanceof SizedSimpleTexture simpleTexture) {
                textureWidth = simpleTexture.getWidth();
                textureHeight = simpleTexture.getHeight();
            } else {
                throw new IllegalStateException();
            }
        } else {
            textureWidth = 0;
            textureHeight = 0;
        }
    }

//    private void rect(PoseStack stack, int x, int y, int width, int height, float uOffset, float vOffset) {
//        blit(stack, x, y, width, height, uOffset, vOffset, width, height, 96, 96);
//    }

    public static void renderGui(int inventoryWidth, int inventoryHeight, CopyFunction function) {
        // RenderSystem.setShaderTexture(0, Utils.id("textures/gui/container/atlas_gen.png"));

        // top
        {
            // left
            function.apply(0, 0, 7, 17, 1, 1);
            // middle
            for (int x = 0; x < inventoryWidth; x++) {
                function.apply(7 + x * Utils.SLOT_SIZE, 0, 18, 17, 9, 1);
            }
            // right
            function.apply(7 + inventoryWidth * Utils.SLOT_SIZE, 0, 7, 17, 28, 1);
            // scrollbar
            function.apply(7 + inventoryWidth * Utils.SLOT_SIZE + 7, 0, 22, 17, 36, 1);
        }

        // main container
        {
            for (int y = 0; y < inventoryHeight; y++) {
                int scollbarYOffset = y == 0 ? 19 : y == inventoryHeight - 1 ? 57 : 38;
                // left
                function.apply(0, 17 + Utils.SLOT_SIZE * y, 7, 18, 1, 19);
                // middle
                for (int x = 0; x < inventoryWidth; x++) {
                    function.apply(7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * y, Utils.SLOT_SIZE, Utils.SLOT_SIZE, 9, 19);
                }
                // right
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth, 17 + Utils.SLOT_SIZE * y, 7, 18, 28, 19);
                // scrollbar
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth + 7, 17 + Utils.SLOT_SIZE * y, 22, 18, 36, scollbarYOffset);
            }
        }

        // divider below main container
        {
            // left
            function.apply(0, 17 + Utils.SLOT_SIZE * inventoryHeight, 7, 14, 1, 38);

            //middle
            for (int x = 0; x < inventoryWidth; x++) {
                function.apply(7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * inventoryHeight, Utils.SLOT_SIZE, 14, 9, 38);
            }

            //right
            function.apply(7 + Utils.SLOT_SIZE * inventoryWidth, 17 + Utils.SLOT_SIZE * inventoryHeight, 7, 14, 28, 38);

            if (inventoryWidth > 9) {
                // scrollbar
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth + 7, 17 + Utils.SLOT_SIZE * inventoryHeight, 22, 17, 59, 76);
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth + 7, 17 + Utils.SLOT_SIZE * inventoryHeight + 17, 12, 15, 59, 1);
            } else {
                // scrollbar
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth + 7, 17 + Utils.SLOT_SIZE * inventoryHeight, 22, 7, 36, 76);
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth + 7, 17 + Utils.SLOT_SIZE * inventoryHeight + 7, 12, 15, 59, 1);
            }
        }

        // bottom of main container
        {
            if (inventoryWidth > 9) {
                // left
                function.apply(0, 17 + Utils.SLOT_SIZE * inventoryHeight + 7 + 3, 7, 7, 1, 58);
                // middle
                int sideParts = (int) Math.ceil((inventoryWidth - 9) / 2.0f);
                for (int i = 0; i < sideParts; i++) {
                    function.apply(7 + Utils.SLOT_SIZE * i, 17 + Utils.SLOT_SIZE * inventoryHeight + 7 + 3, 18, 7, 9, 58);
                    function.apply(7 + Utils.SLOT_SIZE * (inventoryWidth - i - 1), 17 + Utils.SLOT_SIZE * inventoryHeight + 7 + 3, 18, 7, 9, 58);
                }
                // right
                function.apply(7 + Utils.SLOT_SIZE * inventoryWidth, 17 + Utils.SLOT_SIZE * inventoryHeight + 7 + 3, 7, 7, 28, 58);
            }
        }
        int startX = (int) ((inventoryWidth - 9) / 2.0f * Utils.SLOT_SIZE);
        // player inventory
        {
            for (int y = 0; y < 3; y++) {
                // left
                function.apply(startX, 17 + Utils.SLOT_SIZE * (inventoryHeight + y) + 14, 7, 18, 1, 19);
                //middle
                for (int x = 0; x < 9; x++) {
                    function.apply(startX + 7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * (inventoryHeight + y) + 14, 18, 18, 9, 19);
                }
                //right
                function.apply(startX + 7 + 9 * Utils.SLOT_SIZE, 17 + Utils.SLOT_SIZE * (inventoryHeight + y) + 14, 7, 18, 28, 19);
            }
            // left
            function.apply(startX, 17 + Utils.SLOT_SIZE * (inventoryHeight + 3) + 14, 7, 4, 1, 53);
            function.apply(startX, 17 + Utils.SLOT_SIZE * (inventoryHeight + 3) + 14 + 4, 7, 18, 1, 19);
            function.apply(startX, 17 + Utils.SLOT_SIZE * (inventoryHeight + 4) + 14 + 4, 7, 7, 1, 58);
            //middle
            for (int x = 0; x < 9; x++) {
                function.apply(startX + 7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * (inventoryHeight + 3) + 14, 18, 4, 9, 53);
                function.apply(startX + 7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * (inventoryHeight + 3) + 14 + 4, 18, 18, 9, 19);
                function.apply(startX + 7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * (inventoryHeight + 4) + 14 + 4, 18, 7, 9, 58);
            }
            //right
            function.apply(startX + 7 + 9 * Utils.SLOT_SIZE, 17 + Utils.SLOT_SIZE * (inventoryHeight + 3) + 14, 7, 4, 28, 53);
            function.apply(startX + 7 + 9 * Utils.SLOT_SIZE, 17 + Utils.SLOT_SIZE * (inventoryHeight + 3) + 14 + 4, 7, 18, 28, 19);
            function.apply(startX + 7 + 9 * Utils.SLOT_SIZE, 17 + Utils.SLOT_SIZE * (inventoryHeight + 4) + 14 + 4, 7, 7, 28, 58);
        }

        if (inventoryWidth > 9) {
            function.apply(startX, 17 + Utils.SLOT_SIZE * (inventoryHeight) + 14, 3, 3, 20, 66);
            function.apply(startX + Utils.SLOT_SIZE * 9 + 11, 17 + Utils.SLOT_SIZE * (inventoryHeight) + 14, 3, 3, 24, 66);
        }

        // blank slots
        {
            for (int x = 0; x < inventoryWidth; x++) {
                function.apply(7 + Utils.SLOT_SIZE * x, 17 + Utils.SLOT_SIZE * (inventoryHeight + 4) + 14 + 4 + 7, Utils.SLOT_SIZE, Utils.SLOT_SIZE, 1, 66);
            }
        }

    }

    public static AbstractScreen createScreen(AbstractHandler handler, Inventory playerInventory, Component title) {
        ResourceLocation forcedScreenType = handler.getForcedScreenType();
        ResourceLocation preference = forcedScreenType != null ? forcedScreenType : CommonClient.platformHelper().configWrapper().getPreferredScreenType();
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int slots = handler.getInventory().getContainerSize();

        if (forcedScreenType == null && AbstractScreen.canSingleScreenDisplay(slots, scaledWidth, scaledHeight) && AbstractScreen.shouldPreferSingleScreen(preference)) {
            preference = Utils.SINGLE_SCREEN_TYPE;
        }

        if (preference == null) {
            return new FakePickScreen(handler, playerInventory, title);
        }

        ScreenSize screenSize = getScreenSize(preference, slots, scaledWidth, scaledHeight);

        if (screenSize == null) {
            throw new IllegalStateException("screenSize should never be null...");
        }

        if (Utils.PAGINATED_SCREEN_TYPE.equals(preference)) {
            return new PageScreen(handler, playerInventory, title, screenSize);
        } else if (Utils.SCROLLABLE_SCREEN_TYPE.equals(preference)) {
            return new ScrollScreen(handler, playerInventory, title, screenSize);
        } else if (Utils.SINGLE_SCREEN_TYPE.equals(preference)) {
            return new SingleScreen(handler, playerInventory, title, screenSize);
        } else if (Utils.MINI_STORAGE_SCREEN_TYPE.equals(preference)) {
            return new MiniStorageScreen(handler, playerInventory, title, screenSize);
        }

        throw new IllegalArgumentException("Unknown preference.");
    }

    private static boolean shouldPreferSingleScreen(ResourceLocation type) {
        return Utils.PAGINATED_SCREEN_TYPE.equals(type) || Utils.SCROLLABLE_SCREEN_TYPE.equals(type);
    }

    private static boolean canSingleScreenDisplay(int slots, int scaledWidth, int scaledHeight) {
        if (slots <= 54) {
            return true;
        }

        if (CommonClient.platformHelper().configWrapper().fitVanillaConstraints()) {
            return false;
        }

        if (scaledHeight >= 276) {
            if (slots <= 81) {
                return true;
            }
            if (scaledWidth >= 230 && slots <= 108) {
                return true;
            }
            if (scaledWidth >= 284 && slots <= 135) {
                return true;
            }
            if (scaledWidth >= 338 && slots <= 162) {
                return true;
            }
        }
        if (scaledWidth >= 338) {
            if (scaledHeight >= 330 && slots <= 216) {
                return true;
            }
            return scaledHeight >= 384 && slots <= 270;
        }
        return false;
    }

    @Nullable
    public static ScreenSize getScreenSize(ResourceLocation type, int slots, int scaledWidth, int scaledHeight) {
        if (Utils.PAGINATED_SCREEN_TYPE.equals(type)) {
            return PageScreen.retrieveScreenSize(slots, scaledWidth, scaledHeight);
        } else if (Utils.SCROLLABLE_SCREEN_TYPE.equals(type)) {
            return ScrollScreen.retrieveScreenSize(slots, scaledWidth, scaledHeight);
        } else if (Utils.SINGLE_SCREEN_TYPE.equals(type)) {
            return SingleScreen.retrieveScreenSize(slots, scaledWidth, scaledHeight);
        } else if (Utils.MINI_STORAGE_SCREEN_TYPE.equals(type)) {
            return MiniStorageScreen.retrieveScreenSize(slots, scaledWidth, scaledHeight);
        }

        return null;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public final boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.handleKeyPress(keyCode, scanCode, modifiers)) {
            return true;
        } else if (CommonClient.platformHelper().isConfigKeyPressed(keyCode, scanCode, modifiers) && menu.getForcedScreenType() == null
                && CommonClient.platformHelper().configWrapper().getPreferredScreenType() != null) {
            minecraft.setScreen(new PickScreen(this));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    /**
     * @return true if a screen specific keybinding is pressed otherwise false to follow through with additional checks.
     */
    protected boolean handleKeyPress(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @NotNull
    @ApiStatus.OverrideOnly
    public List<Rect2i> getExclusionZones() {
        return List.of();
    }

    // todo: was this api for someone?
    public int getInventoryWidth() {
        return inventoryWidth;
    }
}

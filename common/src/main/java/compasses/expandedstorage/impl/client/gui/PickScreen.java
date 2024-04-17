package compasses.expandedstorage.impl.client.gui;

import com.google.common.collect.ImmutableSortedSet;
import com.mojang.blaze3d.vertex.PoseStack;
import compasses.expandedstorage.impl.CommonClient;
import compasses.expandedstorage.impl.client.gui.widget.PickButton;
import compasses.expandedstorage.impl.client.gui.widget.ScreenPickButton;
import compasses.expandedstorage.impl.misc.Utils;
import compasses.expandedstorage.impl.inventory.handler.AbstractHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class PickScreen extends Screen {
    public static final Component CURRENT_OPTION_TEXT = Component.translatable("screen.expandedstorage.current_option_notice").withStyle(ChatFormatting.GOLD);
    public static final Map<ResourceLocation, PickButton> BUTTON_SETTINGS = Map.of(
            Utils.PAGINATED_SCREEN_TYPE, new PickButton(
                    Utils.id("textures/gui/page_button.png"),
                    Component.translatable("screen.expandedstorage.page_screen")
            ),
            Utils.SCROLLABLE_SCREEN_TYPE, new PickButton(
                    Utils.id("textures/gui/scroll_button.png"),
                    Component.translatable("screen.expandedstorage.scroll_screen")
            ),
            Utils.SINGLE_SCREEN_TYPE, new PickButton(
                    Utils.id("textures/gui/single_button.png"),
                    Component.translatable("screen.expandedstorage.single_screen"),

                    Component.translatable("screen.expandedstorage.off_screen_warning_1").withStyle(ChatFormatting.GRAY),
                    Component.translatable("screen.expandedstorage.off_screen_warning_2").withStyle(ChatFormatting.GRAY)
            ) {
                @Override
                public boolean shouldShowWarning(int scaledWidth, int scaledHeight) {
                    return scaledWidth < 370 || scaledHeight < 386; // Smallest possible resolution a double netherite chest fits on.
                }
            }
    );
    private final Set<ResourceLocation> options = ImmutableSortedSet.copyOf(PickScreen.BUTTON_SETTINGS.keySet());
    private final List<ScreenPickButton> optionButtons = new ArrayList<>(options.size());
    private final Supplier<Screen> returnToScreen;
    private final AbstractHandler handler;
    private int topPadding;

    public PickScreen(AbstractScreen currentScreen) {
        this(currentScreen.getMenu(), () -> {
            return AbstractScreen.createScreen(currentScreen.getMenu(), Minecraft.getInstance().player.getInventory(), currentScreen.getTitle());
        });
    }

    public PickScreen(Screen returnToScreen) {
        this(null, () -> returnToScreen);
    }

    private PickScreen(@Nullable AbstractHandler handler, Supplier<Screen> returnToScreen) {
        super(Component.translatable("screen.expandedstorage.screen_picker_title"));
        this.handler = handler;
        this.returnToScreen = returnToScreen;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onClose() {
        if (handler != null) {
            ResourceLocation preference = CommonClient.platformHelper().configWrapper().getPreferredScreenType();
            if (preference == null) {
                minecraft.player.closeContainer();
                return;
            }
            else if (AbstractScreen.getScreenSize(preference, handler.getInventory().getContainerSize(), minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight()) == null) {
                minecraft.player.displayClientMessage(Component.translatable("text.expandedstorage.short_prefix").withStyle(ChatFormatting.GOLD).append(Component.translatable("chat.expandedstorage.cannot_display_screen", Component.translatable("screen." + preference.getNamespace() + "." + preference.getPath() + "_screen")).withStyle(ChatFormatting.WHITE)), false);
                minecraft.player.closeContainer();
                return;
            }
            handler.clearSlots();
        }
        minecraft.setScreen(returnToScreen.get());
    }

    @Override
    public boolean isPauseScreen() {
        //noinspection ConstantConditions
        return minecraft.level == null;
    }

    @Override
    protected void init() {
        super.init();
        ResourceLocation preference = CommonClient.platformHelper().configWrapper().getPreferredScreenType();
        int choices = options.size();
        int columns = Math.min(Math.floorDiv(width, 96), choices);
        int innerPadding = Math.min((width - columns * 96) / (columns + 1), 20); // 20 is smallest gap for any screen.
        int outerPadding = (width - (((columns - 1) * innerPadding) + (columns * 96))) / 2;
        int x = 0;
        int topPadding = (height - 96) / 2;
        this.topPadding = topPadding;
        optionButtons.clear();
        for (ResourceLocation option : options) {
            PickButton settings = PickScreen.BUTTON_SETTINGS.get(option);
            boolean isWarn = settings.shouldShowWarning(width, height);
            boolean isCurrent = option.equals(preference);
            Button.OnTooltip tooltip = new Button.OnTooltip() {

                @Override
                public void onTooltip(Button button, PoseStack stack, int x, int y) {
                    List<Component> tooltip = new ArrayList<>(4);
                    tooltip.add(button.getMessage());
                    if (isCurrent) {
                        tooltip.add(CURRENT_OPTION_TEXT);
                    }
                    if (isWarn) {
                        tooltip.addAll(settings.getWarningText());
                    }
                    PickScreen.this.renderTooltip(stack, tooltip, Optional.empty(), x, y);
                }

                @Override
                public void narrateTooltip(Consumer<Component> consumer) {
                    if (isCurrent) {
                        consumer.accept(CURRENT_OPTION_TEXT);
                    }
                    if (isWarn) {
                        MutableComponent text = Component.literal("");
                        for (Component component : settings.getWarningText()) {
                            text.append(component);
                        }
                        consumer.accept(text);
                    }
                }
            };
            optionButtons.add(this.addRenderableWidget(new ScreenPickButton(outerPadding + (innerPadding + 96) * x, topPadding, 96, 96,
                    settings.getTexture(), settings.getTitle(), isWarn, isCurrent, button -> this.updatePlayerPreference(option), tooltip)));
            x++;
        }
    }

    private void updatePlayerPreference(ResourceLocation selection) {
        CommonClient.platformHelper().configWrapper().setPreferredScreenType(selection);
        this.onClose();
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        optionButtons.forEach(button -> button.renderButtonTooltip(stack, mouseX, mouseY));
        GuiComponent.drawCenteredString(stack, font, title, width / 2, Math.max(topPadding / 2, 0), 0xFFFFFFFF);
    }
}

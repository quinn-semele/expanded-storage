package dev.compasses.expandedstorage.block.misc;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum BlockColor implements StringRepresentable {
    NONE("none"),
    WHITE("white"),
    ORANGE("orange"),
    MAGENTA("magenta"),
    LIGHT_BLUE("light_blue"),
    YELLOW("yellow"),
    LIME("lime"),
    PINK("pink"),
    GRAY("gray"),
    LIGHT_GRAY("light_gray"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    BROWN("brown"),
    GREEN("green"),
    RED("red"),
    BLACK("black");

    private final String name;

    BlockColor(String name) {
        this.name = name;
    }

    public String getSuffix() {
        if (this == NONE) {
            return "";
        }

        return "_" + getSerializedName();
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return name;
    }
}

package dev.compasses.expandedstorage.block.misc;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum DoubleBlockType implements StringRepresentable {
    SINGLE("single"),
    LEFT("left"),
    RIGHT("right"),
    FRONT("front"),
    BACK("back"),
    TOP("top"),
    BOTTOM("bottom");

    private final String name;

    DoubleBlockType(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return this.name;
    }

    public DoubleBlockType getOpposite() {
        return switch (this) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case FRONT -> BACK;
            case BACK -> FRONT;
            case TOP -> BOTTOM;
            case BOTTOM -> TOP;
            case SINGLE -> throw new IllegalStateException("DoubleBlockType.SINGLE has no opposite.");
        };
    }
}

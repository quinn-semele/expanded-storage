package compasses.expandedstorage.api;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Locale;

public enum EsChestType implements StringRepresentable {
    TOP(-1),
    BOTTOM(-1),
    FRONT(0),
    BACK(2),
    LEFT(1),
    RIGHT(3),
    SINGLE(-1);

    private final String name;
    private final int offset;

    EsChestType(int offset) {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.offset = offset;
    }

    public static EsChestType from(ChestType value) {
        return switch (value) {
            case SINGLE -> EsChestType.SINGLE;
            case LEFT -> EsChestType.RIGHT;
            case RIGHT -> EsChestType.LEFT;
        };
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public EsChestType getOpposite() {
        if (this == EsChestType.TOP) {
            return EsChestType.BOTTOM;
        } else if (this == EsChestType.BOTTOM) {
            return EsChestType.TOP;
        } else if (this == EsChestType.FRONT) {
            return EsChestType.BACK;
        } else if (this == EsChestType.BACK) {
            return EsChestType.FRONT;
        } else if (this == EsChestType.LEFT) {
            return EsChestType.RIGHT;
        } else if (this == EsChestType.RIGHT) {
            return EsChestType.LEFT;
        }
        throw new IllegalStateException("EsChestType.SINGLE has no opposite type.");
    }

    public int getOffset() {
        return offset;
    }
}

package ellemes.expandedstorage.api;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @deprecated Use replacements under the compasses.expandedstorage.api package; for removal in minecraft 1.21.
 */
@Deprecated(forRemoval = true)
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

    @NotNull
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

    public static EsChestType fromNewType(compasses.expandedstorage.api.EsChestType chestType) {
        return switch (chestType) {
            case TOP -> EsChestType.TOP;
            case BOTTOM -> EsChestType.BOTTOM;
            case FRONT -> EsChestType.FRONT;
            case BACK -> EsChestType.BACK;
            case LEFT -> EsChestType.LEFT;
            case RIGHT -> EsChestType.RIGHT;
            case SINGLE -> EsChestType.SINGLE;
        };
    }

    public compasses.expandedstorage.api.EsChestType toNewType() {
        return switch (this) {
            case TOP -> compasses.expandedstorage.api.EsChestType.TOP;
            case BOTTOM -> compasses.expandedstorage.api.EsChestType.BOTTOM;
            case FRONT -> compasses.expandedstorage.api.EsChestType.FRONT;
            case BACK -> compasses.expandedstorage.api.EsChestType.BACK;
            case LEFT -> compasses.expandedstorage.api.EsChestType.LEFT;
            case RIGHT -> compasses.expandedstorage.api.EsChestType.RIGHT;
            case SINGLE -> compasses.expandedstorage.api.EsChestType.SINGLE;
        };
    }
}

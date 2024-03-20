package compasses.expandedstorage.impl.item;

import compasses.expandedstorage.impl.misc.Utils;
import net.minecraft.world.InteractionResult;

public class ToolUsageResult {
    private final InteractionResult result;
    private final int delay;

    private ToolUsageResult(InteractionResult result, int delay) {
        this.result = result;
        this.delay = delay;
    }

    public static ToolUsageResult fastSuccess() {
        return new ToolUsageResult(InteractionResult.SUCCESS, Utils.TOOL_USAGE_QUICK_DELAY);
    }

    public static ToolUsageResult slowSuccess() {
        return new ToolUsageResult(InteractionResult.SUCCESS, Utils.TOOL_USAGE_DELAY);
    }

    public static ToolUsageResult fail() {
        return new ToolUsageResult(InteractionResult.FAIL, 0);
    }

    public InteractionResult getResult() {
        return result;
    }

    public int getDelay() {
        return delay;
    }
}

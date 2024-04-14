package compasses.expandedstorage.impl.fixer;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixerBuilder;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import net.minecraft.util.datafix.fixes.AddNewChoices;
import net.minecraft.util.datafix.fixes.BlockRenameFix;
import net.minecraft.util.datafix.fixes.ItemRenameFix;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.fixes.StatsRenameFix;

import java.util.Map;
import java.util.function.Supplier;

public class DataFixerUtils {
    public static void registerBlockEntities(int versionKey, Map<String, Supplier<TypeTemplate>> map, Schema schema) {
        if (versionKey == 27070) {
            schema.register(map, "expandedstorage:cursed_chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
            schema.register(map, "expandedstorage:old_cursed_chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
            schema.register(map, "expandedstorage:barrel", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
            schema.register(map, "expandedstorage:mini_chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
        } else if (versionKey == 28520) {
            map.remove("expandedstorage:cursed_chest");
            map.remove("expandedstorage:old_cursed_chest");
            schema.register(map, "expandedstorage:chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
            schema.register(map, "expandedstorage:old_chest", () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
        }
    }

    public static void register1_17DataFixer(DataFixerBuilder builder, Schema schema) {
        builder.addFixer(new AddNewChoices(schema, "Add Expanded Storage BE", References.BLOCK_ENTITY));
    }

    public static void register1_18DataFixer(DataFixerBuilder builder, Schema schema) {
        builder.addFixer(new AddNewChoices(schema, "Add renamed Expanded Storage BE", References.BLOCK_ENTITY));
        builder.addFixer(BlockRenameFix.create(schema, "Rename ES Blocks: christmas_chest -> present", id -> {
            if (id.equals("expandedstorage:christmas_chest")) return "expandedstorage:present";
            return id;
        }));
        builder.addFixer(ItemRenameFix.create(schema, "Rename ES Items: christmas_chest -> present, chest_mutator -> storage_mutator", id -> {
            if (id.equals("expandedstorage:christmas_chest")) return "expandedstorage:present";
            else if (id.equals("expandedstorage:chest_mutator")) return "expandedstorage:storage_mutator";
            return id;
        }));
        builder.addFixer(new StatsRenameFix(schema, "Rename ES Stats: open_christmas_chest -> open_present", Map.of("expandedstorage:open_christmas_chest", "expandedstorage:open_present")));
    }
}

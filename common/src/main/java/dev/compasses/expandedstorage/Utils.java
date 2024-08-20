package dev.compasses.expandedstorage;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	public static final String MOD_ID = "expandedstorage";
	public static final Logger LOGGER = LoggerFactory.getLogger("Expanded Storage");

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	public static IllegalStateException codeError(int id) {
		return new IllegalStateException("Quinn Semele messed up the code, reference " + id + ".");
	}
}
package net.sydokiddo.mod_id;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.mod_id.registry.ModRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class Mod implements ModInitializer {

	public static final String MOD_ID = "mod_id";
	public static final Logger LOGGER = LoggerFactory.getLogger("Mod Name");

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		if (Chrysalis.CHRYSALIS_INITIALIZED) {
			ModRegistry.registerAll();
            LOGGER.info("Thank you for downloading {}!", LOGGER.getName());
		} else {
			LOGGER.error("Failed to initialize mod, Chrysalis is not installed!", new Exception());
		}
	}
}
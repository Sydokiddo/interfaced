package net.sydokiddo.mod_id;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.mod_id.registry.InterfacedRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class Interfaced implements ModInitializer {

	public static final String MOD_ID = "interfaced";
	public static final Logger LOGGER = LoggerFactory.getLogger("Interfaced");

	public static ResourceLocation id(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}

	@Override
	public void onInitialize() {
		if (Chrysalis.CHRYSALIS_INITIALIZED) {
			InterfacedRegistry.registerAll();
            LOGGER.info("Thank you for downloading {}!", LOGGER.getName());
		} else {
			LOGGER.error("Failed to initialize mod, Chrysalis is not installed!", new Exception());
		}
	}
}
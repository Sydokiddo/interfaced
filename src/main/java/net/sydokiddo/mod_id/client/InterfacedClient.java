package net.sydokiddo.mod_id.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.sydokiddo.chrysalis.Chrysalis;

@Environment(EnvType.CLIENT)
@SuppressWarnings("all")
public class InterfacedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        if (Chrysalis.CHRYSALIS_INITIALIZED) {
            // Client Registry
        }
    }
}
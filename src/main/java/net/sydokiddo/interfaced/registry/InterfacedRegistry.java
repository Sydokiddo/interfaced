package net.sydokiddo.interfaced.registry;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.sydokiddo.interfaced.Interfaced;
import net.sydokiddo.interfaced.registry.items.ModItems;
import net.sydokiddo.interfaced.registry.misc.ModCreativeModeTabs;
import net.sydokiddo.interfaced.registry.misc.ModLootTableModifiers;
import net.sydokiddo.interfaced.registry.misc.ModSoundEvents;
import net.sydokiddo.interfaced.registry.misc.util.EnvironmentDetectorUsedPayload;
import net.sydokiddo.interfaced.registry.misc.util.NoteBlockPlayedPayload;

public class InterfacedRegistry {

    public static void registerAll() {

        ModItems.registerItems();
        ModSoundEvents.registerSounds();
        ModCreativeModeTabs.registerCreativeTabs();
        ModLootTableModifiers.modifyLootTables();

        PayloadTypeRegistry.playS2C().register(EnvironmentDetectorUsedPayload.TYPE, EnvironmentDetectorUsedPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(NoteBlockPlayedPayload.TYPE, NoteBlockPlayedPayload.CODEC);

        TradeOfferHelper.registerWanderingTraderOffers(1, (trades) -> {
            trades.add((entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), Items.MAP.getDefaultInstance(), 4, 1, 0));
            trades.add((entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), Items.COMPASS.getDefaultInstance(), 4, 1, 0));
            trades.add((entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), Items.CLOCK.getDefaultInstance(), 4, 1, 0));
            trades.add((entity, random) -> new MerchantOffer(new ItemCost(Items.EMERALD, 2), ModItems.ENVIRONMENT_DETECTOR.getDefaultInstance(), 4, 1, 0));
        });

        System.out.println("Registering Content for " + Interfaced.LOGGER.getName());
    }
}
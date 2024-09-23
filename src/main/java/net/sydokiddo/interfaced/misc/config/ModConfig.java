package net.sydokiddo.interfaced.misc.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.sydokiddo.interfaced.Interfaced;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;

public class ModConfig {

    private static final ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
        .id(Interfaced.id("config"))
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
        .setPath(FabricLoader.getInstance().getConfigDir().resolve("interfaced.json5"))
        .setJson5(true)
        .build())
    .build();

    public static void loadConfig() {
        ModConfig.HANDLER.load();
    }

    public static void saveConfig() {
        ModConfig.HANDLER.save();
    }

    // region Compass Config

    @SerialEntry public static boolean compassGUIInformation = true;
    @SerialEntry public static boolean compassTooltip = true;
    @SerialEntry public static boolean lodestoneCompassTooltip = true;
    @SerialEntry public static boolean recoveryCompassTooltip = true;

    // endregion

    // region Clock Config

    @SerialEntry public static boolean clockItemFrameRendering = true;
    @SerialEntry public static boolean clockTooltip = true;
    @SerialEntry public static boolean clockTimeFormat = false;

    // endregion

    // region Map Config

    @SerialEntry public static boolean mapImageTooltip = true;

    // endregion

    // region Environment Detector Config

    @SerialEntry public static boolean environmentDetectorItemInteraction = true;
    @SerialEntry public static boolean environmentDetectorTooltip = true;

    // endregion

    // region Miscellaneous Config

    @SerialEntry public static boolean durabilityTooltip = true;
    @SerialEntry public static boolean foodPropertiesTooltip = !ICommonMethods.HAS_APPLESKIN;
    @SerialEntry public static boolean spectralArrowTooltip = true;
    @SerialEntry public static boolean noteBlockNoteHUD = true;

    // endregion

    // region Experimental Config

    @SerialEntry public static boolean changeReducedDebugInfoDefaultValue = true;
    @SerialEntry public static boolean disableF3Menu = false;

    // endregion
}
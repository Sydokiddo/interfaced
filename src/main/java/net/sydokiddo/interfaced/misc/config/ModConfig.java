package net.sydokiddo.interfaced.misc.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class ModConfig {

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
}
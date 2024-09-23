package net.sydokiddo.interfaced.misc.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;

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

    // region Miscellaneous Config

    @SerialEntry public static boolean durabilityTooltip = true;
    @SerialEntry public static boolean foodPropertiesTooltip = !ICommonMethods.HAS_APPLESKIN;
    @SerialEntry public static boolean spectralArrowTooltip = true;
    @SerialEntry public static boolean noteBlockNoteHUD = true;

    // endregion

    // region Experimental Config

    @SerialEntry public static boolean changeReducedDebugInfoDefaultValue = true;

    // endregion
}
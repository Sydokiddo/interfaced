package net.sydokiddo.interfaced.misc.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.sydokiddo.chrysalis.Chrysalis;
import net.sydokiddo.chrysalis.misc.util.helpers.ConfigHelper;
import net.sydokiddo.chrysalis.registry.ChrysalisRegistry;
import net.sydokiddo.interfaced.Interfaced;
import net.sydokiddo.interfaced.registry.misc.ICommonMethods;

@Environment(EnvType.CLIENT)
public class ModMenuCompatibility implements ModMenuApi {

    private ControllerBuilder<Boolean> booleanOption(Option<Boolean> option) {
        return BooleanControllerBuilder.create(option).formatValue(ConfigHelper::booleanComponent).coloured(true);
    }

    @SuppressWarnings("all")
    private ControllerBuilder<Boolean> namedBooleanOption(Option<Boolean> option, String yes, String no, boolean colored) {
        return BooleanControllerBuilder.create(option).formatValue(value -> value ? Component.translatable(yes) : Component.translatable(no)).coloured(colored);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parentScreen -> YetAnotherConfigLib.createBuilder().title(Component.translatable("mod.interfaced"))

            .category(ConfigCategory.createBuilder().name(ConfigHelper.genericCategoryName(Interfaced.MOD_ID)).tooltip(Component.translatable(ConfigHelper.genericCategoryDescription))

                // region Compass Config

                .group(OptionGroup.createBuilder().name(ConfigHelper.groupName(Interfaced.MOD_ID, "gui.interfaced.config.compass", ICommonMethods.COMPASS_ICON)).description(OptionDescription.of(Component.translatable("gui.interfaced.config.compass.description"))).collapsed(true)

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.compass.gui_information")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.compass.gui_information.description")))
                    .binding(true, () -> ModConfig.compassGUIInformation, newVal -> ModConfig.compassGUIInformation = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.compass.require_compass_for_gui_information")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.compass.require_compass_for_gui_information.description")))
                    .binding(true, () -> ModConfig.requiresCompassForGUIInformation, newVal -> ModConfig.requiresCompassForGUIInformation = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.compass.compass_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.compass.compass_tooltip.description")))
                    .binding(true, () -> ModConfig.compassTooltip, newVal -> ModConfig.compassTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.compass.lodestone_compass_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.compass.lodestone_compass_tooltip.description")))
                    .binding(true, () -> ModConfig.lodestoneCompassTooltip, newVal -> ModConfig.lodestoneCompassTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.compass.recovery_compass_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.compass.recovery_compass_tooltip.description")))
                    .binding(true, () -> ModConfig.recoveryCompassTooltip, newVal -> ModConfig.recoveryCompassTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                .build())

                // endregion

                // region Clock Config

                .group(OptionGroup.createBuilder().name(ConfigHelper.groupName(Interfaced.MOD_ID, "gui.interfaced.config.clock", ICommonMethods.CLOCK_ICON)).description(OptionDescription.of(Component.translatable("gui.interfaced.config.clock.description"))).collapsed(true)

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.clock.item_frame_rendering")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.clock.item_frame_rendering.description")))
                    .binding(true, () -> ModConfig.clockItemFrameRendering, newVal -> ModConfig.clockItemFrameRendering = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.clock.clock_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.clock.clock_tooltip.description")))
                    .binding(true, () -> ModConfig.clockTooltip, newVal -> ModConfig.clockTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.clock.time_format")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.clock.time_format.description")))
                    .binding(false, () -> ModConfig.clockTimeFormat, newVal -> ModConfig.clockTimeFormat = newVal)
                    .controller(option -> this.namedBooleanOption(option, "gui.interfaced.config.clock.time_format.military_time", "gui.interfaced.config.clock.time_format.standard_time", false))
                    .build())

                .build())

                // endregion

                // region Map Config

                .group(OptionGroup.createBuilder().name(ConfigHelper.groupName(Interfaced.MOD_ID, "gui.interfaced.config.map", ICommonMethods.MAP_ICON)).description(OptionDescription.of(Component.translatable("gui.interfaced.config.map.description"))).collapsed(true)

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.map.map_image_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.map.map_image_tooltip.description")))
                    .binding(true, () -> ModConfig.mapImageTooltip, newVal -> ModConfig.mapImageTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                .build())

                // endregion

                // region Environment Detector Config

                .group(OptionGroup.createBuilder().name(ConfigHelper.groupName(Interfaced.MOD_ID, "gui.interfaced.config.environment_detector", ICommonMethods.ENVIRONMENT_DETECTOR_ICON)).description(OptionDescription.of(Component.translatable("gui.interfaced.config.environment_detector.description"))).collapsed(true)

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.environment_detector.item_interaction")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.environment_detector.item_interaction.description")))
                    .binding(true, () -> ModConfig.environmentDetectorItemInteraction, newVal -> ModConfig.environmentDetectorItemInteraction = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.environment_detector.environment_detector_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.environment_detector.environment_detector_tooltip.description")))
                    .binding(true, () -> ModConfig.environmentDetectorTooltip, newVal -> ModConfig.environmentDetectorTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                .build())

                // endregion

                // region Miscellaneous Config

                .group(OptionGroup.createBuilder().name(ConfigHelper.groupName(Interfaced.MOD_ID, ConfigHelper.miscellaneousCategoryName, ICommonMethods.MISCELLANEOUS_ICON)).description(OptionDescription.of(Component.translatable(ConfigHelper.miscellaneousCategoryDescription))).collapsed(true)

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.miscellaneous.durability_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.miscellaneous.durability_tooltip.description")))
                    .binding(true, () -> ModConfig.durabilityTooltip, newVal -> ModConfig.durabilityTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.miscellaneous.food_properties_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.miscellaneous.food_properties_tooltip.description")))
                    .binding(!ICommonMethods.HAS_APPLESKIN, () -> ModConfig.foodPropertiesTooltip, newVal -> ModConfig.foodPropertiesTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.miscellaneous.spectral_arrow_tooltip")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.miscellaneous.spectral_arrow_tooltip.description")))
                    .binding(true, () -> ModConfig.spectralArrowTooltip, newVal -> ModConfig.spectralArrowTooltip = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.miscellaneous.note_block_note_hud")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.miscellaneous.note_block_note_hud.description")))
                    .binding(true, () -> ModConfig.noteBlockNoteHUD, newVal -> ModConfig.noteBlockNoteHUD = newVal)
                    .controller(this::booleanOption)
                    .build())

                .build())

                // endregion

                // region Experimental Config

                .group(OptionGroup.createBuilder().name(ConfigHelper.groupNameWithCustomFont(Chrysalis.MOD_ID, "tooltip_icons", ConfigHelper.experimentalCategoryName, ChrysalisRegistry.WARNING_ICON)).description(OptionDescription.of(Component.translatable(ConfigHelper.experimentalCategoryDescription))).collapsed(true)

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.experimental.change_reducedDebugInfo_default_value")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.experimental.change_reducedDebugInfo_default_value.description")))
                    .binding(true, () -> ModConfig.changeReducedDebugInfoDefaultValue, newVal -> ModConfig.changeReducedDebugInfoDefaultValue = newVal)
                    .controller(this::booleanOption)
                    .build())

                    .option(Option.<Boolean>createBuilder()
                    .name(Component.translatable("gui.interfaced.config.experimental.disable_f3_menu")).description(OptionDescription.of(Component.translatable("gui.interfaced.config.experimental.disable_f3_menu.description")))
                    .binding(false, () -> ModConfig.disableF3Menu, newVal -> ModConfig.disableF3Menu = newVal)
                    .controller(this::booleanOption)
                    .build())

                .build())

                // endregion

            .build())
            .save(ModConfig::saveConfig)

        .build()
        .generateScreen(parentScreen);
    }
}
package net.sydokiddo.interfaced.registry.misc;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.sydokiddo.interfaced.Interfaced;

public class ModSoundEvents {

    // Sound Events

    public static final SoundEvent ENVIRONMENT_DETECTOR_USE = registerSoundEvent("item.environment_detector.use");

    // Registry

    @SuppressWarnings("all")
    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation resourceLocation = Interfaced.id(name);
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(resourceLocation);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, resourceLocation, soundEvent);
    }

    public static void registerSounds() {}
}
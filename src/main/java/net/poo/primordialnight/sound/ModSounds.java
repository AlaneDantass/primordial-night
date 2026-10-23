package net.poo.primordialnight.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.poo.primordialnight.PrimordialNightMod;

/**
 * Centralized registry for custom sound effects (Boss roar, Void Slam impact, Phase 2 transition).
 *
 * Responsible: Dev 4
 * Reference: Forge-Tutorial-1.20.X-29-attackAnimation (ModSounds.java)
 */
public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PrimordialNightMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}

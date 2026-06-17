package com.snackpirate.constructscasting;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CCSounds {
	private static final DeferredRegister<SoundEvent> EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, ConstructsCasting.MOD_ID);

	public static void register(IEventBus eventBus) {
		EVENTS.register(eventBus);
	}
	private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
		return EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ConstructsCasting.id(name)));
	}

	public static DeferredHolder<SoundEvent, SoundEvent> SLIME_CAST = registerSoundEvent("cast.generic.slime");
}

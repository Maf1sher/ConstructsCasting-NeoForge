package com.snackpirate.constructscasting.spells;

import com.snackpirate.constructscasting.ConstructsCasting;
import com.snackpirate.constructscasting.spells.slime.slimeball.SlimeballProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class CCEntities {
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, ConstructsCasting.MOD_ID);

	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}

	public static final DeferredHolder<EntityType<?>, EntityType<SlimeballProjectile>> SLIMEBALL_PROJECTILE =
			ENTITIES.register("slimeball_projectile", () -> EntityType.Builder.<SlimeballProjectile>of(SlimeballProjectile::new, MobCategory.MISC)
					.sized(.5f, .5f)
					.clientTrackingRange(64)
					.build(ConstructsCasting.id("slimeball_projectile").toString()));

}

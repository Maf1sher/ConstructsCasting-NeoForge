package com.snackpirate.constructscasting.fluids;

import com.snackpirate.constructscasting.ConstructsCasting;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import slimeknights.mantle.fluid.texture.ClientTextureFluidType;

/** Registers data-driven fluid textures for all Construct's Casting fluid types. */
@EventBusSubscriber(modid = ConstructsCasting.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CCFluidClientExtensions {
	private CCFluidClientExtensions() {}

	@SubscribeEvent
	static void registerFluidExtensions(RegisterClientExtensionsEvent event) {
		for (var entry : NeoForgeRegistries.FLUID_TYPES.entrySet()) {
			if (entry.getKey().location().getNamespace().equals(ConstructsCasting.MOD_ID)) {
				event.registerFluidType(new ClientTextureFluidType(entry.getValue()), entry.getValue());
			}
		}
	}
}

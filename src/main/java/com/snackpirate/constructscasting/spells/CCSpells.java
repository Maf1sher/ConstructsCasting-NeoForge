package com.snackpirate.constructscasting.spells;

import com.snackpirate.constructscasting.CCDamageTypes;
import com.snackpirate.constructscasting.CCSounds;
import com.snackpirate.constructscasting.ConstructsCasting;
import com.snackpirate.constructscasting.items.CCItems;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class CCSpells {

	private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, ConstructsCasting.MOD_ID);

	public static void register(IEventBus eventBus) {
		SPELLS.register(eventBus);
		Attributes.ATTRIBUTES.register(eventBus);
		Schools.SCHOOLS.register(eventBus);
	}

	public static final Supplier<AbstractSpell> FREEZE_SPELL = registerSpell(new FreezeSpell());
	public static final Supplier<AbstractSpell> INVERT = registerSpell(new InvertSpell());

	public static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
		return SPELLS.register(spell.getSpellName(), () -> spell);
	}

	@EventBusSubscriber(modid = ConstructsCasting.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
	public static class Attributes {
		private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, ConstructsCasting.MOD_ID);

		public static final DeferredHolder<Attribute, Attribute> SLIME_POWER = newPowerAttribute("slime");
		public static final DeferredHolder<Attribute, Attribute> SLIME_RESIST = newResistanceAttribute("slime");

		private static DeferredHolder<Attribute, Attribute> newResistanceAttribute(String id) {
			return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.constructs_casting." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
		}

		private static DeferredHolder<Attribute, Attribute> newPowerAttribute(String id) {
			return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.constructs_casting." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
		}
		@SubscribeEvent
		public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
			e.getTypes().forEach(entity -> ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute)));
		}

	}

	public static class Schools {
		private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, ConstructsCasting.MOD_ID);

		public static final ResourceLocation SLIME_LOC = ConstructsCasting.id("slime");
		public static final Supplier<SchoolType> SLIME = registerSchool(new SchoolType(SLIME_LOC,
				CCItems.Tags.SLIME_FOCUS,
				Component.translatable("school.constructs_casting.slime").withStyle(Style.EMPTY.withColor(0x119c3b)),
				Attributes.SLIME_POWER,
				Attributes.SLIME_RESIST,
				CCSounds.SLIME_CAST,
				CCDamageTypes.SLIME_MAGIC,
				false,
				true
		));

		private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
			return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
		}
	}
}

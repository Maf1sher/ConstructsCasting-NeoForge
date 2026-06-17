package com.snackpirate.constructscasting;

import com.snackpirate.constructscasting.fluids.CCFluidEffects;
import com.snackpirate.constructscasting.fluids.CCFluids;
import com.snackpirate.constructscasting.items.CCItems;
import com.snackpirate.constructscasting.items.ModifiableSpellbookItem;
import com.snackpirate.constructscasting.items.ModifiableSpellbookRenderer;
import com.snackpirate.constructscasting.items.book.ArtificersGuideItem;
import com.snackpirate.constructscasting.spells.CCEntities;
import com.snackpirate.constructscasting.spells.slime.slimeball.SlimeballProjectileRenderer;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.registries.FluidRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.fluids.util.ConstantFluidContainerWrapper;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.shared.CommonsClientEvents;
import slimeknights.tconstruct.shared.TinkerEffects;
import slimeknights.tconstruct.tools.data.ModifierIds;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;

@EventBusSubscriber(modid = ConstructsCasting.MOD_ID)
public class CCEvents {
	private static final String SOULBOUND_SLOT = "tic_soulbound_slot";

	@SubscribeEvent
	static void registerCapabilities(RegisterCapabilitiesEvent event) {
		registerItemPouring(event, ItemRegistry.LIGHTNING_BOTTLE.get(), CCFluids.liquidLightning, 250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.INK_COMMON.get(),       FluidRegistry.COMMON_INK.get(),  250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.INK_UNCOMMON.get(),     FluidRegistry.UNCOMMON_INK.get(),     250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.INK_RARE.get(),         FluidRegistry.RARE_INK.get(),         250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.INK_EPIC.get(),         FluidRegistry.EPIC_INK.get(),         250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.INK_LEGENDARY.get(),    FluidRegistry.LEGENDARY_INK.get(),    250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.BLOOD_VIAL.get(), FluidRegistry.BLOOD.get(),    250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.ICE_VENOM_VIAL.get(), FluidRegistry.ICE_VENOM_FLUID.get(),    250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.TIMELESS_SLURRY.get(), FluidRegistry.TIMELESS_SLURRY_FLUID.get(),    250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.GREATER_HEALING_POTION.get(), FluidRegistry.GREATER_HEALING_ELIXIR_FLUID.get(),    250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.INVISIBILITY_ELIXIR.get(), FluidRegistry.INVISIBILITY_ELIXIR_FLUID.get(),                250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.GREATER_INVISIBILITY_ELIXIR.get(), FluidRegistry.GREATER_INVISIBILITY_ELIXIR_FLUID.get(),250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.EVASION_ELIXIR.get(), FluidRegistry.EVASION_ELIXIR_FLUID.get(),                          250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.GREATER_EVASION_ELIXIR.get(), FluidRegistry.GREATER_EVASION_ELIXIR_FLUID.get(),          250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.OAKSKIN_ELIXIR.get(), FluidRegistry.GREATER_OAKSKIN_ELIXIR_FLUID.get(),                  250, Items.GLASS_BOTTLE.getDefaultInstance());
		registerItemPouring(event, ItemRegistry.GREATER_OAKSKIN_ELIXIR.get(), FluidRegistry.GREATER_OAKSKIN_ELIXIR_FLUID.get(),          250, Items.GLASS_BOTTLE.getDefaultInstance());
	}

	private static void registerItemPouring(RegisterCapabilitiesEvent event, Item input, FluidObject<? extends Fluid> fluidObject, int amount, ItemStack output) {
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) ->
				new ConstantFluidContainerWrapper(new FluidStack(fluidObject.get(), amount), stack, output), input);
	}

	private static void registerItemPouring(RegisterCapabilitiesEvent event, Item input, Fluid fluidObject, int amount, ItemStack output) {
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) ->
				new ConstantFluidContainerWrapper(new FluidStack(fluidObject, amount), stack, output), input);
	}

	@SubscribeEvent
	static void enderferenceAntiSpell(SpellPreCastEvent event) {
		Player entity = event.getEntity();
		if (entity.hasEffect(TinkerEffects.enderference)) {
            String spellId = event.getSpellId();
			if (spellId.equals("irons_spellbooks:teleport") || spellId.equals("irons_spellbooks:blood_step") || spellId.equals("irons_spellbooks:frost_step")) {
				entity.level().playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 2f, 0.2f + Utils.random.nextFloat() * .2f);
				entity.displayClientMessage(Component.translatable("ui.constructs_casting.enderference_anti_teleport").withStyle(ChatFormatting.RED), true);
				event.setCanceled(true);
			}
		}
	}
    @SubscribeEvent
    static void soulboundSpellbooks(DropRulesEvent event) {
        event.addOverride((stack) -> (stack.is(CCItems.Tags.MOD_SPELLBOOKS) && ModifierUtil.getModifierLevel(stack, ModifierIds.soulbound) > 0), ICurio.DropRule.ALWAYS_KEEP);
    }
	@SubscribeEvent
	static void spellProjectileModifiers(EntityJoinLevelEvent event) {
		if (!event.loadedFromDisk() && event.getEntity() instanceof AbstractMagicProjectile projectile) {
			var owner = projectile.getOwner();
			if (owner instanceof LivingEntity livingEntity) {
				CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
					handler.findCurio("spellbook", 0).ifPresent(result -> {
						if (result.stack().is(CCItems.Tags.MOD_SPELLBOOKS)) {
							ModifierNBT modifiers = ToolStack.from(result.stack()).getModifiers();
							EntityModifierCapability.getCapability(projectile).addModifiers(modifiers);
						}
					});
				});
			}
		}
	}
	@SubscribeEvent
	static void damageModifiers(LivingDamageEvent.Pre event) {
		DamageSource source = event.getSource();
		LivingEntity entity = event.getEntity();
		float originalDamage = event.getNewDamage();
		if (source.is(DamageTypes.FREEZE)) {
			int level = TinkerEffect.getLevel(entity, CCFluidEffects.MobEffects.frostbite);
			if (level > 0) {
				originalDamage *= (float) Math.pow(2, level);
			}
		}
		event.setNewDamage(originalDamage);
	}
    @SubscribeEvent
    static void initSpellbooks(PlayerEvent.ItemCraftedEvent event) {
        ItemStack crafted = event.getCrafting();
        if (crafted.is(Items.AIR)) {
        } else {
			ItemStack toInitialize = event.getCrafting();
			if (toInitialize.is(CCItems.Tags.MOD_SPELLBOOKS)) {
				((ModifiableSpellbookItem) toInitialize.getItem()).initializeSpellContainer(toInitialize);
			}
		}
    }

	@EventBusSubscriber(modid = ConstructsCasting.MOD_ID, value = Dist.CLIENT)
	public static class ForgeClientEvents {
	}

	@EventBusSubscriber(modid = ConstructsCasting.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ModClientEvents {
		@SubscribeEvent
		static void registerCurioRenderers(FMLClientSetupEvent e) {
            CuriosRendererRegistry.register(CCItems.travellersSpellbook.get(), ModifiableSpellbookRenderer::new);
			CuriosRendererRegistry.register(CCItems.slimySpellbook.get(), ModifiableSpellbookRenderer::new);
			CuriosRendererRegistry.register(CCItems.platedSpellbook.get(), ModifiableSpellbookRenderer::new);
		}
		@SubscribeEvent
		static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(CCEntities.SLIMEBALL_PROJECTILE.get(), SlimeballProjectileRenderer::new);
		}
		@SubscribeEvent
		static void clientSetup(final FMLClientSetupEvent event) {
			ArtificersGuideItem.ARTIFICERS_GUIDE.fontRenderer = CommonsClientEvents.unicodeFontRender();
		}
	}
}

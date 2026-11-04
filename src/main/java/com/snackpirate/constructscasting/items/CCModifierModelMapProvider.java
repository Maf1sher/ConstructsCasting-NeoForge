package com.snackpirate.constructscasting.items;

import com.snackpirate.constructscasting.ConstructsCasting;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.library.client.modifiers.DyedModifierModel;
import slimeknights.tconstruct.library.data.AbstractModifierModelMapProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;

/** Generates modifier model maps for Construct's Casting tools, replacing the legacy modifier roots. */
public class CCModifierModelMapProvider extends AbstractModifierModelMapProvider {
	public CCModifierModelMapProvider(PackOutput output) {
		super(output, ConstructsCasting.MOD_ID);
	}

	@Override
	protected void addModels() {
		// battlestaff: mirror the modifier set the legacy roots used to render
		tool(CCItems.battlestaff).large('/')
			.basic(
				ModifierIds.diamond, ModifierIds.emerald, ModifierIds.netherite,
				ModifierIds.reinforced, ModifierIds.overforced, ModifierIds.experienced,
				ModifierIds.cooling, ModifierIds.knockback, ModifierIds.hydraulic,
				ModifierIds.smite, ModifierIds.antiaquatic, ModifierIds.baneOfSssss,
				ModifierIds.luck, ModifierIds.magnetic, ModifierIds.silky,
				ModifierIds.sharpness, ModifierIds.blasting)
			.luminosity(15, ModifierIds.lightspeed)
			.luminosity(10, ModifierIds.fiery)
			.luminosity(7, ModifierIds.haste)
			.luminosity(2, ModifierIds.unbreakable)
			.fluid(ModifierIds.bucketing)
			.tank();

		// eldritch staff: dyed textures live under item/eldritch_staff, unlike the default item/tool prefix
		tool(CCItems.eldritchStaff).dyed(new DyedModifierModel(
			material("item/eldritch_staff/modifiers/tconstruct_dyed"),
			material("item/eldritch_staff/large_modifiers/tconstruct_dyed")))
			// suppress the legacy dyed model; the constant above renders it instead
			.empty(TinkerModifiers.dyed.getId());
		tool(CCItems.eldritchStaff, "/broken").dyed(new DyedModifierModel(
			material("item/eldritch_staff/modifiers/broken/tconstruct_dyed"),
			material("item/eldritch_staff/large_modifiers/broken/tconstruct_dyed")))
			.empty(TinkerModifiers.dyed.getId());
	}

	@Override
	public String getName() {
		return "Construct's Casting Modifier Model Map Provider";
	}
}

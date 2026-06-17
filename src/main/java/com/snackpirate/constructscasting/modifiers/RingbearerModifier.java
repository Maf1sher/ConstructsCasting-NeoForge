package com.snackpirate.constructscasting.modifiers;

import com.snackpirate.constructscasting.ConstructsCasting;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.CuriosApi;

public class RingbearerModifier extends Modifier implements EquipmentChangeModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE);
        super.registerHooks(hookBuilder);
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
//        ConstructsCasting.LOGGER.info("ringbearer eq");
        if (!context.getLevel().isClientSide()) {
            CuriosApi.getCuriosInventory(context.getEntity()).ifPresent(handler -> handler.getStacksHandler("ring").ifPresent(stacks -> stacks.addTransientModifier(new AttributeModifier(ConstructsCasting.id("ringbearer"), 2, AttributeModifier.Operation.ADD_VALUE))));
        }
        EquipmentChangeModifierHook.super.onEquip(tool, modifier, context);
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
//        ConstructsCasting.LOGGER.info("ringbearer uneq");
        if (!context.getLevel().isClientSide()) {
            CuriosApi.getCuriosInventory(context.getEntity()).ifPresent(handler -> handler.getStacksHandler("ring").ifPresent(stacks -> stacks.removeModifier(ConstructsCasting.id("ringbearer"))));
        }
        EquipmentChangeModifierHook.super.onUnequip(tool, modifier, context);
    }
}

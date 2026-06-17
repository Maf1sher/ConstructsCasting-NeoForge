package com.snackpirate.constructscasting.items;

import io.redspace.ironsspellbooks.item.armor.ExtendedArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.item.armor.ModifiableArmorItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ModifiableGeoArmorItem extends ModifiableArmorItem implements GeoItem {

    private final software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public ModifiableGeoArmorItem(ArmorMaterial materialIn, Type type, Properties builderIn, ToolDefinition toolDefinition) {
        super(materialIn, type, builderIn, toolDefinition);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 20, this::predicate));
    }
    private PlayState predicate(AnimationState<ModifiableGeoArmorItem> extendedArmorItemAnimationState) {
        extendedArmorItemAnimationState.getController().setAnimation(RawAnimation.begin().thenLoop("idle"));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

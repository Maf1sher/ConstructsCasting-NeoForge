package com.snackpirate.constructscasting.items;

import com.snackpirate.constructscasting.ConstructsCasting;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ModifiableGeoArmorRenderer extends GeoArmorRenderer<ModifiableGeoArmorItem> {
    public ModifiableGeoArmorRenderer() {
        super(new DefaultedItemGeoModel<>(ConstructsCasting.id("modifiable_geo_armor")));
    }

    @Override
    public ResourceLocation getTextureLocation(ModifiableGeoArmorItem animatable) {
        return super.getTextureLocation(animatable);
    }
}

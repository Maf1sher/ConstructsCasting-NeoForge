package com.snackpirate.constructscasting;

import com.mojang.logging.LogUtils;
import com.snackpirate.constructscasting.fluids.CCFluidEffects;
import com.snackpirate.constructscasting.fluids.CCFluids;
import com.snackpirate.constructscasting.items.CCBlocks;
import com.snackpirate.constructscasting.items.CCItems;
import com.snackpirate.constructscasting.items.CCToolSpriteProvider;
import com.snackpirate.constructscasting.items.CCTools;
import com.snackpirate.constructscasting.materials.*;
import com.snackpirate.constructscasting.modifiers.*;
import com.snackpirate.constructscasting.modifiers.hooks.CCModifierHooks;
import com.snackpirate.constructscasting.recipe.*;
import com.snackpirate.constructscasting.spells.CCEntities;
import com.snackpirate.constructscasting.spells.CCSpells;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;
import slimeknights.mantle.client.model.NBTKeyModel;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.data.material.GeneratorPartTextureJsonGenerator;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Mod(ConstructsCasting.MOD_ID)
@EventBusSubscriber(modid = ConstructsCasting.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ConstructsCasting {
    public static final String MOD_ID = "constructs_casting";
    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, ConstructsCasting.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.constructs_casting.constructs_casting"))
            .icon(() -> CCItems.platedSpellbook.get().getRenderTool())
            .displayItems(CCItems::addTabItems)
            .build());

    public static final Logger LOGGER = LogUtils.getLogger();

    public ConstructsCasting(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        CCModifiers.MODIFIERS.register(modEventBus);
        CCFluids.FLUIDS.register(modEventBus);
        CCFluidEffects.MobEffects.register(modEventBus);
        CCItems.ITEMS.register(modEventBus);
        CCBlocks.BLOCKS.register(modEventBus);
        CCRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        CCSounds.register(modEventBus);
        CCEntities.register(modEventBus);
        CCSpells.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }

    public static ResourceLocation id(String name) {
        return Objects.requireNonNull(ResourceLocation.tryBuild(MOD_ID, name));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        CCModifierHooks.init();
        CCFluids.registerFluidEffects();
        registerSerializers();
        MaterialRegistry.getInstance().registerStatType(MagicBaseMaterialStats.TYPE, CCToolStats.MAGIC);
        MaterialRegistry.getInstance().registerStatType(MagicClothMaterialStats.TYPE, CCToolStats.MAGIC);
        MaterialRegistry.getInstance().registerStatType(CCMaterialStats.Statless.ADORNMENT.getType());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            NBTKeyModel.registerExtraTexture(TConstruct.getResource("creative_slot"), "affinity", ConstructsCasting.id("gui/modifiers/affinity_slot"));
        }
    }

    private void registerSerializers() {
        ModifierModule.LOADER.register(ConstructsCasting.id("spellbook_strap"), SpellbookStrapModule.LOADER);
        ModifierModule.LOADER.register(ConstructsCasting.id("bonus_curio_slots"), BonusCurioSlotModule.LOADER);
        ModifierModule.LOADER.register(ConstructsCasting.id("combustive"), CombustiveModule.LOADER);
        ModifierModule.LOADER.register(ConstructsCasting.id("mana_protection"), ManaProtectionModule.LOADER);
        ModifierModule.LOADER.register(ConstructsCasting.id("mana_on_hit"), ManaOnHitModule.LOADER);
        ModifierModule.LOADER.register(ConstructsCasting.id("self_damage_cast"), SelfDamageOnCastModule.LOADER);
        ModifierModule.LOADER.register(id("venomagic"), VenomagicModule.LOADER);
        LivingEntityPredicate.LOADER.register(ConstructsCasting.id("magic_user"), CCModifiers.magicUser.getLoader());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator gen = event.getGenerator();
        boolean server = event.includeServer();
        PackOutput output = gen.getPackOutput();
        CCMaterials mats = new CCMaterials(output);
        gen.addProvider(server, mats);
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        CCMaterialTextures materialSprites = new CCMaterialTextures();
        CCToolSpriteProvider partSprites = new CCToolSpriteProvider(MOD_ID);

        gen.addProvider(server, new MaterialPartTextureGenerator(output, fileHelper, new TinkerPartSpriteProvider(), materialSprites));
        gen.addProvider(server, new MaterialPartTextureGenerator(output, fileHelper, partSprites, getOverride(), materialSprites, new TinkerMaterialSpriteProvider()));
        gen.addProvider(server, new GeneratorPartTextureJsonGenerator(output, MOD_ID, partSprites));
        gen.addProvider(server, new GeneratorPartTextureJsonGenerator(output, TConstruct.MOD_ID, partSprites));

        gen.addProvider(server, new CCTools.CCToolDefinitions(output, MOD_ID));
        gen.addProvider(server, new CCMaterials.MaterialStats(output, mats));
        gen.addProvider(server, new CCMaterials.CCMaterialRenderInfo(output, new CCMaterialTextures(), fileHelper));
        gen.addProvider(server, new CCModifiers(output));
        gen.addProvider(server, new CCMaterials.CCMaterialTraits(output, mats));
        gen.addProvider(server, new CCMaterials.Tags(output, MOD_ID, fileHelper));
        gen.addProvider(server, new CCSlotLayoutProvider(output));
        CCBlocks.Tags blockTags = new CCBlocks.Tags(output, provider, MOD_ID, fileHelper);
        gen.addProvider(server, blockTags);
        gen.addProvider(server, new CCItems.Tags(output, provider, blockTags.contentsGetter(), MOD_ID, fileHelper));
        gen.addProvider(server, new CCFluids.CCFluidTextures(output, MOD_ID));
        gen.addProvider(server, new CCFluids.CCBucketModels(output, MOD_ID));
        gen.addProvider(server, new CCFluids.Tags(output, provider, MOD_ID, fileHelper));
        gen.addProvider(server, new CCFluids.Tags.CCFluidTooltipProvider(output, MOD_ID));
        gen.addProvider(server, new CCRecipes(output, provider));
        gen.addProvider(server, new CCLootTableProvider(output, provider));
        gen.addProvider(server, new CCFluidTransfer(output, MOD_ID));
        gen.addProvider(server, new CCMobEquipment(output, MOD_ID));
        gen.addProvider(server, new CCLootInjections(output, IronsSpellbooks.MODID));
        gen.addProvider(server, new CCFluidEffects(output, ConstructsCasting.MOD_ID));
        gen.addProvider(server, new CCLang(output, ConstructsCasting.MOD_ID, "en_us"));
        gen.addProvider(server, new CCDamageTypes.Tags(output, provider, MOD_ID, fileHelper));
        gen.addProvider(server, new CCModifiers.Tags(output, MOD_ID, fileHelper));
    }

    private static GeneratorPartTextureJsonGenerator.StatOverride getOverride() {
        GeneratorPartTextureJsonGenerator.StatOverride.Builder builder = new GeneratorPartTextureJsonGenerator.StatOverride.Builder();
        CCMaterials.tinkerClothMaterials.forEach((material) -> builder.addVariant(MagicClothMaterialStats.ID, material));
        CCMaterials.tinkerMagicMaterials.forEach((material) -> builder.addVariant(MagicBaseMaterialStats.ID, material));
        CCMaterials.tinkerAdornMaterials.forEach((material) -> builder.addVariant(CCMaterialStats.Statless.ADORNMENT.getIdentifier(), material));
        return builder.build();
    }

}

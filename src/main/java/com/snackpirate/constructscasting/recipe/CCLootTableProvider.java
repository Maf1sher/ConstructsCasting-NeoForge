package com.snackpirate.constructscasting.recipe;

import com.snackpirate.constructscasting.ConstructsCasting;
import com.snackpirate.constructscasting.items.CCBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CCLootTableProvider extends LootTableProvider {

	private static final Set<ResourceKey<LootTable>> REQUIRED_TABLES = Set.of();

	public CCLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, REQUIRED_TABLES, List.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)), registries);

	}

	public static class Blocks extends BlockLootSubProvider {
		protected Blocks(HolderLookup.Provider registries) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
		}
		@SuppressWarnings("deprecation")
		@Override
		protected Iterable<Block> getKnownBlocks() {
			return BuiltInRegistries.BLOCK.stream()
					.filter(block -> ConstructsCasting.MOD_ID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace()))
					.collect(Collectors.toList());
		}

		@Override
		protected void generate() {
			this.dropSelf(CCBlocks.arcaneBlock.get());
			this.dropSelf(CCBlocks.exiliteBlock.get());
			this.dropSelf(CCBlocks.mithrilBlock.get());
			this.dropSelf(CCBlocks.pyriumBlock.get());
		}
	}

}

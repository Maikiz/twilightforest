package twilightforest.data;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.fml.RegistryObject;
import twilightforest.TwilightForestMod;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CraftingShapes extends RecipeProvider {
	public CraftingShapes(DataGenerator generator) {
		super(generator);
	}

	protected final Ingredient itemWithNBT(RegistryObject<? extends IItemProvider> item, Consumer<CompoundNBT> nbtSetter) {
		return itemWithNBT(item.get(), nbtSetter);
	}

	protected final Ingredient itemWithNBT(IItemProvider item, Consumer<CompoundNBT> nbtSetter) {
		ItemStack stack = new ItemStack(item);

		CompoundNBT nbt = new CompoundNBT();
		nbtSetter.accept(nbt);
		stack.setTag(nbt);

		try {
			Constructor<NBTIngredient> constructor = NBTIngredient.class.getDeclaredConstructor(ItemStack.class);

			constructor.setAccessible(true);

			return constructor.newInstance(stack);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// This will just defer to the regular Ingredient method instead of some overridden thing, but whatever.
		// Forge PRs are too slow to even feel motivated about fixing it on the Forge end.
		return NBTIngredient.fromStacks(stack);
	}

	protected final Ingredient multipleIngredients(Ingredient... ingredientArray) {
		List<Ingredient> ingredientList = ImmutableList.copyOf(ingredientArray);

		try {
			Constructor<CompoundIngredient> constructor = CompoundIngredient.class.getDeclaredConstructor(List.class);

			constructor.setAccessible(true);

			return constructor.newInstance(ingredientList);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		// This will just defer to the regular Ingredient method instead of some overridden thing, but whatever.
		// Forge PRs are too slow to even feel motivated about fixing it on the Forge end.
		return CompoundIngredient.merge(ingredientList);
	}

	protected final void castleBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, IItemProvider... ingredients) {
		ShapedRecipeBuilder.shapedRecipe(result.get(), 4)
				.patternLine("##")
				.patternLine("##")
				.key('#', Ingredient.fromItems(ingredients))
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locCastle(name));
	}

	protected final void stairsBlock(Consumer<IFinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, IItemProvider... ingredients) {
		ShapedRecipeBuilder.shapedRecipe(result.get(),  8)
				.patternLine("#  ")
				.patternLine("## ")
				.patternLine("###")
				.key('#', Ingredient.fromItems(ingredients))
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, loc);
	}

	protected final void stairsRightBlock(Consumer<IFinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, IItemProvider... ingredients) {
		ShapedRecipeBuilder.shapedRecipe(result.get(),  8)
				.patternLine("###")
				.patternLine(" ##")
				.patternLine("  #")
				.key('#', Ingredient.fromItems(ingredients))
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, loc);
	}

	protected final void reverseStairsBlock(Consumer<IFinishedRecipe> consumer, ResourceLocation loc, Supplier<? extends Block> result, IItemProvider ingredient) {
		ShapelessRecipeBuilder.shapelessRecipe(result.get(),  3)
				.addIngredient(ingredient)
				.addIngredient(ingredient)
				.addIngredient(ingredient)
				.addIngredient(ingredient)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, loc);
	}

	protected final void compressedBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, ITag.INamedTag<Item> ingredient) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("###")
				.patternLine("###")
				.patternLine("###")
				.key('#', ingredient)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, TwilightForestMod.prefix("compressed_blocks/" + name));
	}

	protected final void reverseCompressBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> ingredient) {
		ShapelessRecipeBuilder.shapelessRecipe(result.get(), 9)
				.addIngredient(ingredient)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, TwilightForestMod.prefix("compressed_blocks/reversed/" + name));
	}

	protected final void helmetItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("###")
				.patternLine("# #")
				.key('#', material)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void chestplateItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("# #")
				.patternLine("###")
				.patternLine("###")
				.key('#', material)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void leggingsItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("###")
				.patternLine("# #")
				.patternLine("# #")
				.key('#', material)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void bootsItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("# #")
				.patternLine("# #")
				.key('#', material)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void pickaxeItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material, ITag.INamedTag<Item> handle) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("###")
				.patternLine(" X ")
				.patternLine(" X ")
				.key('#', material)
				.key('X', handle)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void swordItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material, ITag.INamedTag<Item> handle) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("#")
				.patternLine("#")
				.patternLine("X")
				.key('#', material)
				.key('X', handle)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void axeItem(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Item> result, ITag.INamedTag<Item> material, ITag.INamedTag<Item> handle) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("##")
				.patternLine("#X")
				.patternLine(" X")
				.key('#', material)
				.key('X', handle)
				.addCriterion("has_" + result.get().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locEquip(name));
	}

	protected final void buttonBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapelessRecipeBuilder.shapelessRecipe(result.get())
				.addIngredient(material.get())
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_button"));
	}

	protected final void doorBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
				.patternLine("##")
				.patternLine("##")
				.patternLine("##")
				.key('#', material.get())
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_door"));
	}

	protected final void fenceBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get(), 3)
				.patternLine("#S#")
				.patternLine("#S#")
				.key('#', material.get())
				.key('S', Tags.Items.RODS_WOODEN)
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_fence"));
	}

	protected final void gateBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("S#S")
				.patternLine("S#S")
				.key('#', material.get())
				.key('S', Tags.Items.RODS_WOODEN)
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_gate"));
	}

	protected final void planksBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapelessRecipeBuilder.shapelessRecipe(result.get(), 4)
				.addIngredient(material.get())
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_planks"));
	}

	protected final void plateBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get())
				.patternLine("##")
				.key('#', material.get())
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_plate"));
	}

	protected final void slabBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
				.patternLine("###")
				.key('#', material.get())
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_slab"));
	}

	protected final void trapdoorBlock(Consumer<IFinishedRecipe> consumer, String name, Supplier<? extends Block> result, Supplier<? extends Block> material) {
		ShapedRecipeBuilder.shapedRecipe(result.get(), 6)
				.patternLine("###")
				.patternLine("###")
				.key('#', material.get())
				.addCriterion("has_" + result.get().asItem().getRegistryName().getPath(), hasItem(result.get()))
				.build(consumer, locWood(name + "_trapdoor"));
	}

	protected final ResourceLocation locCastle(String name) {
		return TwilightForestMod.prefix("castleblock/" + name);
	}

	protected final ResourceLocation locEquip(String name) {
		return TwilightForestMod.prefix("equipment/" + name);
	}

	protected final ResourceLocation locNaga(String name) {
		return TwilightForestMod.prefix("nagastone/" + name);
	}

	protected final ResourceLocation locWood(String name) {
		return TwilightForestMod.prefix("wood/" + name);
	}
}

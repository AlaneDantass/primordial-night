package net.poo.primordialnight.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.poo.primordialnight.item.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.END_COMPASS.get())
                .pattern("ECE")
                .pattern("CBC")
                .pattern("ECE")
                .define('E', Items.ENDER_PEARL)
                .define('C', Items.CRYING_OBSIDIAN)
                .define('B', Items.COMPASS)
                .unlockedBy(getHasName(Items.ENDER_PEARL), has(Items.ENDER_PEARL))
                .save(pWriter);

    }
}
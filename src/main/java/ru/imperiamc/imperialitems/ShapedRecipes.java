package ru.imperiamc.imperialitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

import static ru.imperiamc.imperialitems.RecipeComponents.*;

public class ShapedRecipes {
    private final ImperialItems plugin;

    public ShapedRecipes(ImperialItems plugin) {
        this.plugin = plugin;
    }

    public ShapedRecipe bloodyMap() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, BEWITCHED_AMELIA_SWORD.getKey()),
                componentManager.getOrDefault(BEWITCHED_AMELIA_SWORD.getName()));
        recipe.shape(" S ", "SCS", " S ");
        recipe.setIngredient('S', componentManager.getOrDefault(BEWITCHED_SWORD_SHARD.getName()));
        recipe.setIngredient('C', componentManager.getOrDefault(CRYSTAL_SWORD.getName()));
        return recipe;
    }

    public ShapedRecipe bewitchedAmeliaSword() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, BLOODY_MAP.getKey()),
                componentManager.getOrDefault(BLOODY_MAP.getName()));
        recipe.shape("FFF", "FBF", "FFF");
        recipe.setIngredient('F', componentManager.getOrDefault(BLOODY_MAP_FRAGMENT.getName()));
        recipe.setIngredient('B', componentManager.getOrDefault(BEAST_BLOOD.getName()));
        return recipe;
    }
}

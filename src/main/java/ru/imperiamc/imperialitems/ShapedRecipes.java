package ru.imperiamc.imperialitems;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.ItemFileManager;

import static ru.imperiamc.imperialitems.RecipeComponents.*;

public class ShapedRecipes {
    private final ImperialItems plugin;

    public ShapedRecipes(ImperialItems plugin) {
        this.plugin = plugin;
    }

    public ShapedRecipe bloodyMap() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, BLOODY_MAP.getKey()),
                componentManager.get(BLOODY_MAP.getName()));
        recipe.shape("FFF", "FBF", "FFF");
        recipe.setIngredient('F', componentManager.get(BLOODY_MAP_FRAGMENT.getName()));
        recipe.setIngredient('B', componentManager.get(BEAST_BLOOD.getName()));
        return recipe;
    }
}

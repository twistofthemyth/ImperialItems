package ru.imperiamc.imperialitems.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapelessRecipe;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.ItemFileManager;

import static ru.imperiamc.imperialitems.recipes.RecipeComponents.*;

public class ShapelessRecipes {
    private final ImperialItems plugin;

    public ShapelessRecipes(ImperialItems plugin) {
        this.plugin = plugin;
    }

    public ShapelessRecipe blessedAmethystShard() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        return new ShapelessRecipe(new NamespacedKey(plugin, BLESSED_AMETHYST_SHARD.getKey()),
                componentManager.getOrDefault(BLESSED_AMETHYST_SHARD.getName()))
                .addIngredient(Material.AMETHYST_SHARD)
                .addIngredient(Material.PAPER)
                .addIngredient(Material.GOLD_INGOT);
    }

    public ShapelessRecipe cursedAmethystShard() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        return new ShapelessRecipe(new NamespacedKey(plugin, CURSED_AMETHYST_SHARD.getKey()),
                componentManager.getOrDefault(CURSED_AMETHYST_SHARD.getName()))
                .addIngredient(Material.AMETHYST_SHARD)
                .addIngredient(Material.PAPER)
                .addIngredient(Material.BONE);
    }

    public ShapelessRecipe enchantedAmethystShard() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        return new ShapelessRecipe(new NamespacedKey(plugin, ENCHANTED_AMETHYST_SHARD.getKey()),
                componentManager.getOrDefault(ENCHANTED_AMETHYST_SHARD.getName()))
                .addIngredient(Material.AMETHYST_SHARD)
                .addIngredient(Material.PAPER)
                .addIngredient(Material.LAPIS_LAZULI);
    }

    public ShapelessRecipe redstoneAmethystShard() {
        ItemFileManager componentManager = plugin.getRecipeComponentManager();
        return new ShapelessRecipe(new NamespacedKey(plugin, REDSTONE_AMETHYST_SHARD.getKey()),
                componentManager.getOrDefault(REDSTONE_AMETHYST_SHARD.getName()))
                .addIngredient(Material.AMETHYST_SHARD)
                .addIngredient(Material.PAPER)
                .addIngredient(Material.COPPER_INGOT);
    }
}

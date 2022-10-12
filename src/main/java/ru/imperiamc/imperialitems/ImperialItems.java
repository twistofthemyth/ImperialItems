package ru.imperiamc.imperialitems;

import lombok.Getter;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import ru.imperiamc.imperialitems.listeners.ItemHeldListener;
import ru.imperiamc.imperialitems.managers.JsonRuleManager;
import ru.imperiamc.imperialitems.managers.RecipeComponentManager;
import ru.imperiamc.imperialitems.managers.RuleManager;
import ru.imperiamc.imperialitems.recipe.ShapedRecipes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class ImperialItems extends JavaPlugin {

    @Getter
    private static ImperialItems instance;
    @Getter
    private RuleManager ruleManager;

    @Getter
    private RecipeComponentManager recipeComponentManager;

    @Override
    public void onEnable() {
        instance = this;
        createDataFolder();
        getServer().getPluginManager().registerEvents(new ItemHeldListener(), this);
        Objects.requireNonNull(getCommand("ii")).setExecutor(new CommandExecutorImpl(this));
        ruleManager = new JsonRuleManager();
        recipeComponentManager = new RecipeComponentManager(createRecipeFolder());

        ShapedRecipes recipes = new ShapedRecipes(this);
        getServer().addRecipe(recipes.bloodyMap());
    }

    @Override
    public void onDisable() {
        ruleManager.save();
    }

    private void createDataFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }

    private Path createRecipeFolder() {
        createDataFolder();
        Path recipeFolder = Paths.get(getDataFolder().getPath(), "recipes");
        if (!recipeFolder.toFile().exists()) {
            try {
                Files.createDirectory(recipeFolder);
            } catch (IOException e) {
                getLogger().warning(e.getMessage());
            }
        }
        return recipeFolder;
    }
}

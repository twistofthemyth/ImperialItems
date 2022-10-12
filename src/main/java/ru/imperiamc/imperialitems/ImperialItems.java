package ru.imperiamc.imperialitems;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;
import java.util.Objects;

public final class ImperialItems extends JavaPlugin {
    @Getter
    private ItemFileManager recipeComponentManager;
    @Getter
    private ItemFileManager referenceItemManager;

    @Override
    public void onEnable() {
        createDataFolder();
        recipeComponentManager = new ItemFileManager(this, Paths.get(getDataFolder().getPath(), "recipes"));
        referenceItemManager = new ItemFileManager(this, Paths.get(getDataFolder().getPath(), "references"));

        getServer().getPluginManager().registerEvents(new ItemHeldListener(this), this);
        Objects.requireNonNull(getCommand("ii")).setExecutor(new CommandExecutorImpl(this));

        ShapedRecipes recipes = new ShapedRecipes(this);
        getServer().addRecipe(recipes.bloodyMap());
    }

    private void createDataFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }
}

package ru.imperiamc.imperialitems;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.imperiamc.imperialitems.keep.KeepInventoryListener;
import ru.imperiamc.imperialitems.recipes.ShapedRecipes;
import ru.imperiamc.imperialitems.recipes.ShapelessRecipes;
import ru.imperiamc.imperialitems.silk.SilkTouchListener;

import java.nio.file.Paths;
import java.util.Objects;

public final class ImperialItems extends JavaPlugin {

    private final ShapedRecipes shapedRecipes = new ShapedRecipes(this);
    private final ShapelessRecipes shapelessRecipes = new ShapelessRecipes(this);

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

        // Recipes
        addRecipes();

        // Event Listeners
        getServer().getPluginManager().registerEvents(new SilkTouchListener(), this);
        getServer().getPluginManager().registerEvents(new KeepInventoryListener(), this);
    }

    public void reload() {
        getServer().resetRecipes();
        addRecipes();
    }

    private void createDataFolder() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
    }

    private void addRecipes() {
        getServer().addRecipe(shapedRecipes.bloodyMap());
        getServer().addRecipe(shapedRecipes.bewitchedAmeliaSword());
        getServer().addRecipe(shapelessRecipes.enchantedAmethystShard());
        getServer().addRecipe(shapelessRecipes.cursedAmethystShard());
        getServer().addRecipe(shapelessRecipes.blessedAmethystShard());
        getServer().addRecipe(shapelessRecipes.redstoneAmethystShard());
    }
}

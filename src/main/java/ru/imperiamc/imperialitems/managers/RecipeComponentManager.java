package ru.imperiamc.imperialitems.managers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

@ParametersAreNonnullByDefault
public class RecipeComponentManager {
    private final static Logger LOG = Logger.getLogger(RecipeComponentManager.class.getName());
    private final Path recipeComponentsFolder;

    public RecipeComponentManager(Path recipeComponentsFolder) {
        this.recipeComponentsFolder = recipeComponentsFolder;
    }

    public void add(ItemStack item, String component) {
        File componentFile = Path.of(recipeComponentsFolder.toString(), component).toFile();
        if (componentFile.exists()) {
            componentFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(componentFile)) {
            fos.write(item.serializeAsBytes());
            LOG.info(() -> "Added recipe component " + component);
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }
    }

    public ItemStack get(String component) {
        File componentFile = Path.of(recipeComponentsFolder.toString(), component).toFile();
        try (FileInputStream fis = new FileInputStream(componentFile)) {
            return ItemStack.deserializeBytes(fis.readAllBytes());
        } catch (IOException e) {
            LOG.warning(e.getMessage());
        }
        return new ItemStack(Material.BEDROCK, 1);
    }

    public void remove(String component) {
        File componentFile = Path.of(recipeComponentsFolder.toString(), component).toFile();
        if (componentFile.exists()) {
            componentFile.delete();
        }
        LOG.info(() -> "Removed recipe component " + component);
    }
}

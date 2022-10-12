package ru.imperiamc.imperialitems;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ParametersAreNonnullByDefault
public class ItemFileManager {
    protected final ImperialItems plugin;
    protected final Path folder;

    public ItemFileManager(ImperialItems plugin, Path folder) {
        this.plugin = plugin;
        this.folder = folder;
        if (!folder.toFile().exists()) {
            try {
                Files.createDirectory(folder);
            } catch (IOException e) {
                plugin.getLogger().warning(e.getMessage());
            }
        }
    }

    public void add(ItemStack item, String fileName) {
        File itemFile = Path.of(folder.toString(), fileName).toFile();
        if (itemFile.exists()) {
            itemFile.delete();
        }
        try (FileOutputStream fos = new FileOutputStream(itemFile)) {
            fos.write(item.serializeAsBytes());
            plugin.getLogger().info(() -> "Item file created: " + fileName);
        } catch (IOException e) {
            plugin.getLogger().warning(e.getMessage());
        }
    }

    @Nullable
    public ItemStack get(String itemName) {
        File componentFile = Path.of(folder.toString(), itemName).toFile();
        try (FileInputStream fis = new FileInputStream(componentFile)) {
            return ItemStack.deserializeBytes(fis.readAllBytes());
        } catch (IOException e) {
            return null;
        }
    }

    @Nonnull
    public ItemStack getOrDefault(String item) {
        ItemStack itemStack = get(item);
        return itemStack == null ? new ItemStack(Material.BEDROCK, 1) : itemStack;
    }

    public void remove(String itemName) {
        File componentFile = Path.of(folder.toString(), itemName).toFile();
        if (componentFile.exists()) {
            componentFile.delete();
        }
        plugin.getLogger().info(() -> "Item file removed: " + itemName);
    }
}

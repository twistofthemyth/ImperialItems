package ru.imperiamc.imperialitems.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.models.ItemRecordMo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class JsonRuleManager extends AbstractRuleManager {

    private final static Logger LOG = Logger.getLogger(RuleManager.class.getCanonicalName());
    private final File dataFolder;
    private final Gson gson;

    public JsonRuleManager() {
        super();
        dataFolder = ImperialItems.getInstance().getDataFolder();
        gson = new GsonBuilder().setPrettyPrinting().create();
        load();
    }

    @Override
    public synchronized void save() {
        LOG.info("Item data saving...");
        localData.getKeys().forEach(this::save);
    }

    @Override
    public synchronized void save(@NotNull Material material) {
        localData.getAll(material).forEach((key, value) -> save(material, key));
    }

    private synchronized void save(@NotNull Material material, String ruleName) {
        try (FileWriter fw = new FileWriter(getFilePath(material, ruleName))) {
            fw.write(gson.toJson(localData.get(material, ruleName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void load() {
        LOG.info("Item data loading...");
        localData.clearAll();
        File[] files = dataFolder.listFiles();
        if (files != null) {
            Arrays.stream(files)
                    .filter(File::isDirectory)
                    .forEach(this::load);
        } else {
            LOG.info("Item Data not found");
        }
    }

    private synchronized void load(@NotNull File directory) {
        Material material = Material.getMaterial(directory.getName());
        File[] files = directory.listFiles();
        if (material != null && files != null) {
            LOG.info("Saving " + material + " item data");
            Arrays.stream(files).forEach(e -> load(material, e.getName().replace(".json", "")));
        }
    }

    private synchronized void load(@NotNull Material material, @NotNull String ruleName) {
        try (FileReader reader = new FileReader(getFilePath(material, ruleName))) {
            localData.add(material, ruleName, gson.fromJson(reader, ItemRecordMo.class));
        } catch (IOException ignore) {
        }
    }

    private File getFilePath(@NotNull Material material, @NotNull String ruleName) {
        File dir = new File(ImperialItems.getInstance().getDataFolder() + "\\" + material.name());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return new File(dir + "\\" + ruleName + ".json");
    }
}

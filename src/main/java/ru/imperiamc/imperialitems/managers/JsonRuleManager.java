package ru.imperiamc.imperialitems.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.models.ItemRecordMo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
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
    public synchronized void load() {
        LOG.info("Item data loading...");
        localData.clearAll();
        File[] files = dataFolder.listFiles();
        if (files != null) {
            Arrays.stream(files)
                    .map(e -> Material.getMaterial(e.getName().replace(".json", "")))
                    .filter(Objects::nonNull)
                    .forEach(this::load);
        } else {
            LOG.info("Item Data not found");
        }
    }

    @Override
    public synchronized void save() {
        LOG.info("Item data saving...");
        localData.getKeys().forEach(this::save);
    }

    @Override
    public synchronized void save(@NotNull Material material) {
        try (FileWriter fw = new FileWriter(getFilePath(material))) {
            fw.write(gson.toJson(localData.getAll(material)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void load(@NotNull Material material) {
        LOG.info("Загрузка данных для " + material);
        try (FileReader fr = new FileReader(getFilePath(material))) {
            localData.addAll(material, gson.fromJson(fr, new TypeToken<Map<String, ItemRecordMo>>() {
            }.getType()));
        } catch (IOException ignore) {
        }
    }

    private File getFilePath(Material material) {
        return new File(ImperialItems.getInstance().getDataFolder() + "\\" + material.name() + ".json");
    }
}

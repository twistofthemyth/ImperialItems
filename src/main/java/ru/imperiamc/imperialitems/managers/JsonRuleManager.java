package ru.imperiamc.imperialitems.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.models.ItemMo;
import ru.imperiamc.imperialitems.models.ItemRecordMo;
import ru.imperiamc.imperialitems.models.LocalStorage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class JsonRuleManager implements RuleManager {

    private final static Logger LOG = Logger.getLogger(RuleManager.class.getCanonicalName());
    private final File dataFolder = ImperialItems.getInstance().getDataFolder();
    private final Gson gson;

    private final LocalStorage localData;

    public JsonRuleManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        localData = new LocalStorage();
        load();
    }

    @Override
    public void addRule(@NotNull String ruleName, @NotNull ItemMo item) {
        localData.add(item.getMaterial(), ruleName, new ItemRecordMo(item));
        saveData(item.getMaterial());
    }

    @Override
    public boolean addRuleSuitableLore(@NotNull Material material, @NotNull String ruleName, @NotNull String suitableLore) {
        ItemRecordMo itemRecord = localData.get(material, ruleName);
        if (itemRecord != null) {
            if (itemRecord.getSuitableLore() == null) {
                itemRecord.setSuitableLore(new ArrayList<>());
            }
            itemRecord.getSuitableLore().add(suitableLore);
            saveData(material);
            return true;
        }
        return false;
    }

    @Override
    public boolean addRuleNotSuitableLore(@NotNull Material material, @NotNull String ruleName, @NotNull String notSuitableLore) {
        ItemRecordMo itemRecord = localData.get(material, ruleName);
        if (itemRecord != null) {
            if (itemRecord.getNotSuitableLore() == null) {
                itemRecord.setNotSuitableLore(new ArrayList<>());
            }
            itemRecord.getNotSuitableLore().add(notSuitableLore);
            saveData(material);
            return true;
        }
        return false;
    }

    @Override
    @Nullable
    public ItemMo getReplacement(@NotNull ItemMo itemMo) {
        ItemRecordMo record = findRecord(itemMo, localData.get(itemMo.getMaterial()));
        if (record != null) {
            return record.getItemMo();
        }
        return null;
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
                    .forEach(this::loadData);
        } else {
            LOG.info("Item Data not found");
        }
    }

    @Override
    public synchronized void save() {
        LOG.info("Item data saving...");
        localData.getKeys().forEach(this::saveData);
    }


    @Nullable
    private ItemRecordMo findRecord(@NotNull ItemMo itemMo, @NotNull List<ItemRecordMo> records) {
        return records.stream().filter(record -> isSuitable(itemMo, record)).findFirst().orElse(null);
    }

    private boolean isSuitable(ItemMo itemMo, ItemRecordMo record) {
        if (record.getSuitableLore() != null) {
            boolean isSuitable = record.getSuitableLore().stream().anyMatch(itemMo::hasLore);
            if (isSuitable && record.getNotSuitableLore() != null) {
                return record.getNotSuitableLore().stream().noneMatch(itemMo::hasLore);
            }
            return isSuitable;
        }
        return false;
    }

    private synchronized void saveData(Material material) {
        try (FileWriter fw = new FileWriter(getFilePath(material))) {
            fw.write(gson.toJson(localData.getAll(material)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void loadData(@NotNull Material material) {
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

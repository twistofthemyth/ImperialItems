package ru.imperiamc.imperialitems.managers;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.imperiamc.imperialitems.models.ItemRecordMo;

import java.util.*;

public class LocalStorage {

    private final Map<Material, Map<String, ItemRecordMo>> data;

    public LocalStorage() {
        this.data = new HashMap<>();
    }

    public void add(@NotNull Material material, @NotNull String name, @NotNull ItemRecordMo itemRecord) {
        if (!data.containsKey(material)) {
            data.put(material, new HashMap<>());
        }
        data.get(material).put(name, itemRecord);
    }

    public void addAll(@NotNull Material material, @NotNull Map<String, ItemRecordMo> itemRecords) {
        if (!data.containsKey(material)) {
            data.put(material, new HashMap<>());
        }
        data.get(material).putAll(itemRecords);
    }

    @Nullable
    public ItemRecordMo get(@NotNull Material material, @NotNull String name) {
        if (data.containsKey(material)) {
            return data.get(material).get(name);
        }
        return null;
    }

    @NotNull
    public List<ItemRecordMo> get(@NotNull Material material) {
        if (data.containsKey(material)) {
            return new ArrayList<>(data.get(material).values());
        }
        return Collections.emptyList();
    }

    @NotNull
    public Map<String, ItemRecordMo> getAll(@NotNull Material material) {
        if (data.containsKey(material)) {
            return data.get(material);
        }
        return Collections.emptyMap();
    }

    public void remove(@NotNull Material material, @NotNull String name) {
        if (data.containsKey(material)) {
            data.get(material).remove(name);
        }
    }

    public void clear(@NotNull Material material) {
        if (data.containsKey(material)) {
            data.get(material).clear();
        }
    }

    public void clearAll(){
        data.clear();
    }

    public Set<Material> getKeys() {
        return data.keySet();
    }
}

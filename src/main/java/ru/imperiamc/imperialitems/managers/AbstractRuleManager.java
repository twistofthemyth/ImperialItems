package ru.imperiamc.imperialitems.managers;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.imperiamc.imperialitems.models.ItemMo;
import ru.imperiamc.imperialitems.models.ItemRecordMo;
import ru.imperiamc.imperialitems.models.LocalStorage;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRuleManager implements RuleManager {
    protected final LocalStorage localData;

    protected AbstractRuleManager() {
        localData = new LocalStorage();
    }

    @Override
    public void addRule(@NotNull String ruleName, @NotNull ItemMo item) {
        localData.add(item.getMaterial(), ruleName, new ItemRecordMo(item));
        save(item.getMaterial());
    }

    @Override
    public boolean addRuleSuitableLore(@NotNull Material material, @NotNull String ruleName, @NotNull String suitableLore) {
        ItemRecordMo itemRecord = localData.get(material, ruleName);
        if (itemRecord != null) {
            if (itemRecord.getSuitableLore() == null) {
                itemRecord.setSuitableLore(new ArrayList<>());
            }
            itemRecord.getSuitableLore().add(suitableLore);
            save(material);
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
            save(material);
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
}

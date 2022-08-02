package ru.imperiamc.imperialitems.managers;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.imperiamc.imperialitems.models.ItemMo;

public interface RuleManager {
    void addRule(@NotNull String loreSign, @NotNull ItemMo item);

    boolean addRuleSuitableLore(@NotNull Material material, @NotNull String ruleName, @NotNull String suitableLore);

    boolean addRuleNotSuitableLore(@NotNull Material material, @NotNull String ruleName, @NotNull String notSuitableLore);

    @Nullable
    ItemMo getReplacement(@NotNull ItemMo itemMo);

    void load();

    void save();

    void save(@NotNull Material material);
}

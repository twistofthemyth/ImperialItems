package ru.imperiamc.imperialitems.managers;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.imperiamc.imperialitems.models.ItemMo;

import java.util.List;

public interface RuleManager {
    void addRule(@NotNull String loreSign, @NotNull ItemMo item);

    @NotNull
    ItemMo getRuleItem(@NotNull Material material, @NotNull String loreSign);

    @Nullable
    String getLoreSign(@NotNull Material material, @NotNull List<Component> lore);
}

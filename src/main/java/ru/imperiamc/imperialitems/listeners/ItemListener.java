package ru.imperiamc.imperialitems.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.managers.RuleManager;
import ru.imperiamc.imperialitems.models.ItemMo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemListener implements Listener {

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        boolean result = itemSoftUpdate(player);
        if (result) {
            itemAllUpdate(player);
        }
    }

    private boolean itemSoftUpdate(Player player) {
        List<ItemStack> itemList =
                new ArrayList<>(Arrays.stream(player.getInventory().getArmorContents())
                        .filter(Objects::nonNull)
                        .toList());
        itemList.add(player.getInventory().getItemInMainHand());
        itemList.add(player.getInventory().getItemInOffHand());

        return itemList.stream().map(this::tryToUpdateItem).anyMatch(e -> e.equals(true));
    }

    private void itemAllUpdate(Player player) {
        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach(this::tryToUpdateItem);
    }

    private boolean tryToUpdateItem(@NotNull ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            RuleManager ruleManager = ImperialItems.getInstance().getRuleManager();
            ItemMo oldItem = new ItemMo(item);
            ItemMo replacement = ruleManager.getReplacement(new ItemMo(item));
            if (replacement != null) {
                if (!replacement.equals(oldItem)) {
                    updateItemMeta(item, replacement.toItemStack());
                    return true;
                }
            }
        }
        return false;
    }

    private void updateItemMeta(@NotNull ItemStack oldItem, @NotNull ItemStack newItem) {
        ItemMeta oldMeta = oldItem.getItemMeta();
        ItemMeta newMeta = newItem.getItemMeta();
        if (newMeta.hasLore()) {
            oldMeta.lore(newItem.getItemMeta().lore());
        }
        if (newMeta.hasAttributeModifiers()) {
            oldMeta.setAttributeModifiers(newItem.getItemMeta().getAttributeModifiers());
        }
        if (newMeta.hasCustomModelData()) {
            oldMeta.setCustomModelData(newItem.getItemMeta().getCustomModelData());
        }
        oldMeta.setUnbreakable(newItem.getItemMeta().isUnbreakable());

        oldItem.setItemMeta(oldMeta);
    }
}

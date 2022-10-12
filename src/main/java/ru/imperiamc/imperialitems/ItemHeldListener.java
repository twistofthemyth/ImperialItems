package ru.imperiamc.imperialitems;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemHeldListener implements Listener {

    private final ImperialItems plugin;

    public ItemHeldListener(ImperialItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        boolean result = itemSoftUpdate(player);
        if (result) {
            itemAllUpdate(player);
            playUpdateEffect(player);
        }
    }

    private boolean itemSoftUpdate(@NotNull Player player) {
        List<ItemStack> itemList = new ArrayList<>(Arrays.stream(player.getInventory().getArmorContents())
                .filter(Objects::nonNull)
                .toList());
        itemList.add(player.getInventory().getItemInMainHand());
        itemList.add(player.getInventory().getItemInOffHand());

        boolean result = false;
        for (ItemStack item : itemList) {
            if (tryToUpdateItem(item)) {
                result = true;
            }
        }
        return result;
    }

    private void itemAllUpdate(@NotNull Player player) {
        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach(this::tryToUpdateItem);
    }

    private boolean tryToUpdateItem(@NotNull ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) {
            ItemFileManager referenceManager = plugin.getReferenceItemManager();
            ItemStack reference = referenceManager.get(item.getType().name() + item.getItemMeta().getCustomModelData());
            if (reference != null && !compareItems(item, reference)) {
                updateItem(item, reference);
                return true;
            }
        }
        return false;
    }

    private void updateItem(@NotNull ItemStack oldItem, @NotNull ItemStack newItem) {
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

    private void playUpdateEffect(@NotNull Player player) {
        player.sendMessage(ChatColor.RED + "Вы чувствуйте чье то злобное внимание");
        player.sendMessage(ChatColor.DARK_RED + "Странная аура застилает ваши предметы");
        player.playSound(player.getLocation(), Sound.ENTITY_FOX_TELEPORT, 10, 0);
        player.spawnParticle(Particle.DRAGON_BREATH, player.getLocation(), 100, 1, 1, 1, 0.1);
    }

    private boolean compareItems(ItemStack actual, ItemStack reference) {
        ItemMeta actualMeta = actual.getItemMeta();
        ItemMeta referenceMeta = reference.getItemMeta();

        // Lore compare
        if (actualMeta.hasLore() != referenceMeta.hasLore()) {
            return false;
        }
        if (actualMeta.hasLore() &&
                !actualMeta.lore().equals(referenceMeta.lore())) {
            return false;
        }


        // Attributes compare
        if (actualMeta.hasAttributeModifiers() != referenceMeta.hasAttributeModifiers()) {
            return false;
        }
        if (actualMeta.hasAttributeModifiers() &&
                !actualMeta.getAttributeModifiers().equals(referenceMeta.getAttributeModifiers())) {
            return false;
        }

        return true;
    }
}

package ru.imperiamc.imperialitems.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.managers.RuleManager;
import ru.imperiamc.imperialitems.models.ItemMo;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Logger;

public class ItemListener implements Listener {

    private static final Logger LOG = Logger.getLogger(ItemListener.class.getName());

    @EventHandler
    public void onChangeItem(PlayerItemHeldEvent event) {
        Arrays.stream(event.getPlayer().getInventory().getArmorContents()).filter(Objects::nonNull).forEach(this::tryToUpdateItem);
        tryToUpdateItem(event.getPlayer().getInventory().getItemInMainHand());
        tryToUpdateItem(event.getPlayer().getInventory().getItemInOffHand());
    }

    public void tryToUpdateItem(@NotNull ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            LOG.info("Начинаем проверку для предмета \n" + item.getType());
            RuleManager ruleManager = ImperialItems.getInstance().getRuleManager();
            String loreSign = ruleManager.getLoreSign(item.getType(), Objects.requireNonNull(item.lore()));
            if (loreSign != null) {
                LOG.info("Найдено правило '" + loreSign + "'");
                ItemMo newItem = ruleManager.getRuleItem(item.getType(), loreSign);
                ItemMo oldItem = new ItemMo(item);
                if (!newItem.equals(oldItem)) {
                    updateItemMeta(item, newItem.toItemStack());
                    LOG.info("Предмет заменен");
                }
            }
        }
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

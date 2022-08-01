package ru.imperiamc.imperialitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.managers.RuleManager;
import ru.imperiamc.imperialitems.models.ItemMo;

import java.util.logging.Logger;

public class UpdateItemCommand implements CommandExecutor {

    private static final Logger LOG = Logger.getLogger(UpdateItemCommand.class.getName());

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            ItemStack holdItem = player.getInventory().getItemInMainHand();
            if (holdItem.getItemMeta().hasLore()) {
                LOG.info("Начинаем проверку для предмета \n" + holdItem);
                RuleManager ruleManager = ImperialItems.getInstance().getRuleManager();
                String loreSign = ruleManager.getLoreSign(holdItem.getType(), holdItem.lore());
                if (loreSign != null) {
                    LOG.info("Найдено правило '" + loreSign + "'");
                    ItemMo newItem = ruleManager.getRuleItem(holdItem.getType(), loreSign);
                    ItemMo oldItem = new ItemMo(holdItem);
                    if (!newItem.equals(oldItem)) {
                        updateItem(holdItem, newItem.toItemStack());
                        LOG.info("Предмет заменен");
                        return true;
                    }
                } else {
                    LOG.info("Правило для предмета не найдено.");
                }
            }
        }
        return false;
    }

    private static void updateItem(ItemStack oldItem, ItemStack newItem) {
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

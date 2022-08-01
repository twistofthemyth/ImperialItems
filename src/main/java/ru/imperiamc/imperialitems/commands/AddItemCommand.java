package ru.imperiamc.imperialitems.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.ImperialItems;
import ru.imperiamc.imperialitems.models.ItemMo;

import java.util.Arrays;

public class AddItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                StringBuilder loreSign = new StringBuilder();
                Arrays.stream(args).forEach(e -> loreSign.append(e).append(" "));
                return addItemRule(loreSign.toString().trim(), player.getInventory().getItemInMainHand());
            }
        }
        return false;
    }

    private boolean addItemRule(String loreSign, ItemStack itemStack) {
        if (itemStack.getItemMeta().hasLore()) {
            ImperialItems.getInstance().getRuleManager().addRule(loreSign, new ItemMo(itemStack));
            return true;
        }
        return false;
    }
}

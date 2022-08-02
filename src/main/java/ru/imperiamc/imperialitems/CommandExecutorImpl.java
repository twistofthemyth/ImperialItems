package ru.imperiamc.imperialitems;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.models.ItemMo;

public class CommandExecutorImpl implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length > 0 && "add".equals(args[0])) {
            addItemCommand(player, command, label, args);
        }
        return true;
    }

    private void addItemCommand(@NotNull Player player, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            StringBuilder loreSign = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                loreSign.append(args[i]).append(" ");
            }

            ItemStack item = player.getInventory().getItemInMainHand();
            if (addItemRule(loreSign.toString().trim(), item)) {
                sendMessage(player, String.format("Правило %s для %s успешно добавлено", loreSign, item.getType()));
            }
        } else {
            sendMessage(player, "Укажите название правила");
        }
    }

    private boolean addItemRule(String loreSign, ItemStack itemStack) {
        if (itemStack.getItemMeta().hasLore()) {
            ImperialItems.getInstance().getRuleManager().addRule(loreSign, new ItemMo(itemStack));
            return true;
        }
        return false;
    }

    private void sendMessage(@NotNull Player player, @NotNull String message) {
        player.sendMessage(Component.text("[ImperialItems] " + message));
    }
}

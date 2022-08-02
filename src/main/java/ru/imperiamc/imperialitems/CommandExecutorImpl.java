package ru.imperiamc.imperialitems;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.imperiamc.imperialitems.managers.RuleManager;
import ru.imperiamc.imperialitems.models.ItemMo;

public class CommandExecutorImpl implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length > 0) {
            switch (args[0]) {
                case "add" -> addItemCommand(player, args);
                case "addlore" -> addLoreCommand(player, args, true);
                case "addalore" -> addLoreCommand(player, args, false);
                case "reload" -> reloadCommand();
            }
        }
        return true;
    }

    private void addItemCommand(@NotNull Player player, @NotNull String[] args) {
        if (args.length == 2) {
            String ruleName = args[1];
            ItemStack item = player.getInventory().getItemInMainHand();
            if (addItemRule(ruleName, item)) {
                sendMessage(player, String.format("Правило %s для %s успешно добавлено", ruleName, item.getType()));
            }
        } else {
            sendMessage(player, "Укажите название правила: /ii add <название_правила>");
        }
    }

    private void addLoreCommand(@NotNull Player player, @NotNull String[] args, boolean isSuitable) {
        if (args.length > 2) {
            RuleManager ruleManager = ImperialItems.getInstance().getRuleManager();
            String ruleName = args[1];
            ItemStack item = player.getInventory().getItemInMainHand();
            StringBuilder suitableLore = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                suitableLore.append(args[i]).append(" ");
            }
            boolean result = isSuitable ?
                    ruleManager.addRuleSuitableLore(item.getType(), ruleName, suitableLore.toString().trim()) :
                    ruleManager.addRuleNotSuitableLore(item.getType(), ruleName, suitableLore.toString().trim());
            if (result) {
                sendMessage(player, "Правило " + ruleName + "обновлено.");
            } else {
                sendMessage(player, String.format("Правило %s для %s не найдено", ruleName, item.getType()));
            }
        } else {
            sendMessage(player, String.format("Укажите необходимый лор для правила: /ii %s <название_правила> <лор>", isSuitable ? "addlore" : "addalore"));
        }
    }

    private void reloadCommand() {
        ImperialItems.getInstance().getRuleManager().load();
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

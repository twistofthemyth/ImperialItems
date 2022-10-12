package ru.imperiamc.imperialitems;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.imperiamc.imperialitems.managers.RuleManager;
import ru.imperiamc.imperialitems.models.ItemMo;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommandExecutorImpl implements CommandExecutor {

    private final ImperialItems plugin;

    public CommandExecutorImpl(ImperialItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("imperialitems.admin")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "add" -> addItemCommand(player, args);
                        case "addrecipe" -> addRecipeComponentCommand(player, args);
                        case "addlore" -> addLoreCommand(player, args, true);
                        case "addalore" -> addLoreCommand(player, args, false);
                        case "reload" -> reloadCommand();
                    }
                } else {
                    sendHelp(player);
                }
            } else {
                sendMessage(player, "Access denied", false);
            }
        }
        return true;
    }

    private void addItemCommand(Player player, String[] args) {
        if (args.length == 2) {
            String ruleName = args[1];
            ItemStack item = player.getInventory().getItemInMainHand();
            if (addItemRule(ruleName, item)) {
                sendMessage(player,
                        String.format("Rule %s for material %s added", ruleName, item.getType()), true);
            }
        } else {
            sendHelp(player);
        }
    }

    private void addRecipeComponentCommand(Player player, String[] args) {
        if (args.length > 1) {
            String componentName = args[1];
            ItemStack item = player.getInventory().getItemInMainHand();
            plugin.getRecipeComponentManager().add(item, componentName);
            sendMessage(player,
                    String.format("Recipe component %s added", componentName), true);
        } else {
            sendMessage(player, "Internal error", false);
        }
    }

    private void addLoreCommand(Player player, String[] args, boolean isSuitable) {
        if (args.length > 2) {
            RuleManager ruleManager = plugin.getRuleManager();
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
                sendMessage(player, "Rule " + ruleName + " updated", true);
            } else {
                sendMessage(player,
                        String.format("Rule %s for material %s not found", ruleName, item.getType()), true);
            }
        } else {
            sendHelp(player);
        }
    }

    private void reloadCommand() {
        plugin.getRuleManager().load();
    }

    private boolean addItemRule(String loreSign, ItemStack itemStack) {
        if (itemStack.getItemMeta().hasLore()) {
            plugin.getRuleManager().addRule(loreSign, new ItemMo(itemStack));
            return true;
        }
        return false;
    }

    private void sendMessage(Player player, String message, boolean success) {
        player.sendMessage(Component.text(ChatColor.GOLD + "[ImperialItems] "
                + (success ? ChatColor.GREEN : ChatColor.RED) + message));
    }

    private void sendCommandHelp(Player player, String command, String descr) {
        player.sendMessage(Component.text(ChatColor.GOLD + command + " - " + ChatColor.GREEN + descr));
    }

    private void sendHelp(Player player) {
        sendCommandHelp(player, "/ii add <rule name>", "add rule for item in mainhand");
        sendCommandHelp(player, "/ii addlore <rule name> <lore string>", "add lore filter for rule");
        sendCommandHelp(player, "/ii addalore <rule name> <lore string>", "add inverted lore filter for rule");
        sendCommandHelp(player, "/ii reload", "reload plugin");
    }
}

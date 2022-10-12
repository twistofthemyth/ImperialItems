package ru.imperiamc.imperialitems;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CommandExecutorImpl implements CommandExecutor {

    private final ImperialItems plugin;

    public CommandExecutorImpl(ImperialItems plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && player.hasPermission("imperialitems.admin")) {
            if (args.length == 2 && "add".equals(args[0]) && "reference".equals(args[1])) {
                addItemReferenceCommand(player);
            } else if (args.length == 2 && "remove".equals(args[0]) && "reference".equals(args[1])) {
                removeItemReferenceCommand(player);
            } else if (args.length == 3 && "add".equals(args[0]) && "recipe".equals(args[1])) {
                addRecipeComponentCommand(player, args[2]);
            } else if (args.length == 3 && "remove".equals(args[0]) && "recipe".equals(args[1])) {
                removeRecipeComponentCommand(player, args[2]);
            } else {
                sendHelp(player);
            }
        }
        return true;
    }

    private void addItemReferenceCommand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        String referenceName = item.getType().name() + item.getItemMeta().getCustomModelData();
        plugin.getReferenceItemManager().add(item, referenceName);
        sendMessage(player,
                String.format("Item reference %s added", referenceName), true);
    }

    private void removeItemReferenceCommand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        String referenceName = item.getType().name() + item.getItemMeta().getCustomModelData();
        plugin.getReferenceItemManager().remove(referenceName);
        sendMessage(player,
                String.format("Item reference %s removed", referenceName), true);
    }

    private void addRecipeComponentCommand(Player player, String itemName) {
        ItemStack item = player.getInventory().getItemInMainHand();
        plugin.getRecipeComponentManager().add(item, itemName);
        sendMessage(player, String.format("Recipe component %s added", item), true);
    }

    private void removeRecipeComponentCommand(Player player, String itemName) {
        plugin.getReferenceItemManager().remove(itemName);
        sendMessage(player, String.format("Recipe component %s removed", itemName), true);
    }

    private void sendMessage(Player player, String message, boolean success) {
        player.sendMessage(Component.text(ChatColor.GOLD + "[ImperialItems] "
                + (success ? ChatColor.GREEN : ChatColor.RED) + message));
    }

    private void sendCommandHelp(Player player, String command, String descr) {
        player.sendMessage(Component.text(ChatColor.GOLD + command + " - " + ChatColor.GREEN + descr));
    }


    private void sendHelp(Player player) {
        sendCommandHelp(player, "/ii add reference ", "add item held on main hand as replacement rule");
        sendCommandHelp(player, "/ii add recipe <component name>", "adds item held on main hand as recipe component");
        sendCommandHelp(player, "/ii remove reference", "remove item held on main hand from replacement rules");
        sendCommandHelp(player, "/ii remove recipe", "remove recipe component");
    }
}

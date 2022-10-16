package ru.imperiamc.imperialitems.silk;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class SilkTouchListener implements Listener {

    public static Set<Material> silkTouchBlockList = ImmutableSet.of(Material.BUDDING_AMETHYST);

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (isSilkTouch(event.getPlayer()) && isHandledBlock(event.getBlock())) {
            dropItem(event.getBlock());
        }
    }

    private boolean isSilkTouch(Player player) {
        return player.getInventory().getItemInMainHand().hasItemMeta() &&
                player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }

    private boolean isHandledBlock(Block block) {
        return silkTouchBlockList.contains(block.getBlockData().getMaterial());
    }

    private void dropItem(Block block) {
        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType(), 1));
    }
}

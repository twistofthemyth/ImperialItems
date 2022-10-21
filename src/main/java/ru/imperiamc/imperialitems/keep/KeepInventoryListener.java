package ru.imperiamc.imperialitems.keep;

import com.google.common.collect.ImmutableSet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Set;

public class KeepInventoryListener implements Listener {

    public static Set<String> ignoredWorlds = ImmutableSet.of("world", "world_nether", "world_the_end");

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!ignoredWorlds.contains(event.getPlayer().getWorld().getName())) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }
}

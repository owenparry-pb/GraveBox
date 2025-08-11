package me.bebo.graveBox.listeners;

import me.bebo.graveBox.GraveBox;
import me.bebo.graveBox.tracking.GraveTracking;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class CompassListener implements Listener {
    private final GraveBox plugin;

    public CompassListener(GraveBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCompassInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.COMPASS) {
            return;
        }

        Player player = event.getPlayer();
        if (!plugin.getGraveTracker().isTracking(player)) {
            return;
        }

        // Force update compass on right-click
        GraveTracking tracking = plugin.getGraveTracker().getTrackedGrave(player);
        if (tracking != null) {
            plugin.getGraveTracker().updateTracking(player, tracking);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clean up tracking when player leaves
        plugin.getGraveTracker().stopTracking(event.getPlayer());
    }
}
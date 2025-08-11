package me.bebo.graveBox.listeners;

import me.bebo.graveBox.GraveBox;
import me.bebo.graveBox.tracking.GraveTracking;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CompassListener implements Listener {
    private final GraveBox plugin;

    public CompassListener(GraveBox plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCompassUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.COMPASS) {
            if (plugin.getGraveTracker().isTracking(player)) {
                // Update compass target if already tracking
                GraveTracking tracking = plugin.getGraveTracker().getTrackedGrave(player);
                if (tracking != null) {
                    plugin.getGraveTracker().updateTracking(player, tracking);
                    player.setCompassTarget(tracking.getLocation());
                    player.sendMessage(plugin.tl("messages.compass.updated"));
                }
            }
        }
    }
}
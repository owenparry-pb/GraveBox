package me.bebo.graveBox;

import me.bebo.graveBox.backup.GraveBackup;
import me.bebo.graveBox.tracking.GraveTracker;
import me.bebo.graveBox.gui.GraveGUI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GraveBox extends JavaPlugin implements Listener {
    private GraveTracker graveTracker;
    private GraveBackup graveBackup;
    private GraveGUI graveGUI;

    @Override
    public void onEnable() {
        // Initialize components
        graveTracker = new GraveTracker(this);
        graveBackup = new GraveBackup(this);
        graveGUI = new GraveGUI(this, graveTracker, graveBackup);

        // Register events
        getServer().getPluginManager().registerEvents(this, this);

        // Register commands
        getCommand("grave").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                graveGUI.openMainMenu(player);
                return true;
            }
            return false;
        });
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        // Create grave at death location
        graveTracker.addGrave(player, deathLocation);
        
        // Backup inventory
        graveBackup.backupInventory(player, deathLocation);
        
        // Clear default drops
        event.getDrops().clear();
        event.setKeepLevel(true);
        event.setDroppedExp(0);

        player.sendMessage("§6Your grave has been created at: " + 
            String.format("§e%d, %d, %d", 
                deathLocation.getBlockX(), 
                deathLocation.getBlockY(), 
                deathLocation.getBlockZ()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!event.getView().getTitle().equals("§8GraveBox Management")) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        // Handle GUI interactions here
        // This is where you'd implement the logic for teleporting to graves
        // and restoring inventories based on which item was clicked
    }

    @Override
    public void onDisable() {
        // Clean up resources if needed
    }
}
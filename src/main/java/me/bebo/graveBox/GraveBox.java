package me.bebo.graveBox;

import me.bebo.graveBox.commands.GraveTrackCommand;
import me.bebo.graveBox.listeners.CompassListener;
import me.bebo.graveBox.tracking.GraveTracker;
import me.bebo.graveBox.tracking.GraveTracking;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.ChatColor;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GraveBox extends JavaPlugin {
    private GraveTracker graveTracker;
    private FileConfiguration messages;

    @Override
    public void onEnable() {
        getLogger().info("GraveBox: onEnable started");

        try {
            // Save default config
            getLogger().info("Saving default config...");
            saveDefaultConfig();

            // Initialize tracking system
            getLogger().info("Initializing GraveTracker...");
            graveTracker = new GraveTracker(this);

            // Register commands
            getLogger().info("Registering gravetrack command...");
            if (getCommand("gravetrack") == null) {
                getLogger().severe("Command 'gravetrack' not found in plugin.yml!");
            } else {
                getCommand("gravetrack").setExecutor(new GraveTrackCommand(this));
            }

            // Register listeners
            getLogger().info("Registering listeners...");
            getServer().getPluginManager().registerEvents(new CompassListener(this), this);

            // Load messages
            getLogger().info("Loading messages...");
            loadMessages();

            getLogger().info("GraveBox: onEnable completed successfully.");
        } catch (Exception e) {
            getLogger().severe("Error during onEnable: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("GraveBox: onDisable started");

        if (graveTracker != null) {
            getLogger().info("Cleaning up GraveTracker...");
            graveTracker.cleanup();
        }
        // Save any pending data
        if (messages != null) {
            try {
                getLogger().info("Saving messages.yml...");
                messages.save(new File(getDataFolder(), "messages.yml"));
            } catch (IOException e) {
                getLogger().warning("Failed to save messages.yml: " + e.getMessage());
            }
        }

        getLogger().info("GraveBox: onDisable completed.");
    }

    public GraveTracker getGraveTracker() {
        return graveTracker;
    }

    // Add this method to your existing translation system
    public String tl(String path, Object... args) {
        String message = messages.getString(path, path);
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replace("{" + i + "}", String.valueOf(args[i]));
            }
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private void loadMessages() {
        getLogger().info("Attempting to load messages.yml...");
        // Create messages.yml if it doesn't exist
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            getLogger().info("messages.yml not found, saving default resource...");
            saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
        getLogger().info("messages.yml loaded.");
    }

    // Update your existing onPlayerDeath method to include grave tracking
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        getLogger().info("PlayerDeathEvent triggered for player: " + event.getEntity().getName());
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();

        // Create a grave at or near the death location
        Location graveLocation = findSafeGraveLocation(deathLocation);

        // After creating the grave, register it with the tracking system
        UUID graveId = UUID.randomUUID();
        getLogger().info("Registering grave for player " + player.getName() + " at location: " + graveLocation);
        graveTracker.registerGrave(
            graveId,
            player.getUniqueId(),
            player.getName(),
            graveLocation
        );

        // Automatically give them a tracking compass if enabled
        if (getConfig().getBoolean("grave.tracking.auto-track", true)) {
            GraveTracking tracking = graveTracker.findLatestGrave(player.getUniqueId());
            if (tracking != null) {
                getLogger().info("Auto-tracking enabled: starting tracking for player " + player.getName());
                graveTracker.startTracking(player, tracking);
            }
        }
    }

    private Location findSafeGraveLocation(Location original) {
        // Simple implementation - you might want to make this more sophisticated
        return original.getBlock().getLocation().add(0.5, 0, 0.5);
    }
}
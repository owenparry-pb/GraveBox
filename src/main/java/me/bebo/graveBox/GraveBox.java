package me.bebo.graveBox;

import me.bebo.graveBox.commands.GraveTrackCommand;
import me.bebo.graveBox.listeners.CompassListener;
import me.bebo.graveBox.tracking.GraveTracker;
import org.bukkit.plugin.java.JavaPlugin;

public class GraveBox extends JavaPlugin {
    private GraveTracker graveTracker;
    private FileConfiguration messages;

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        
        // Initialize tracking system
        graveTracker = new GraveTracker(this);
        
        // Register commands
        getCommand("gravetrack").setExecutor(new GraveTrackCommand(this));
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new CompassListener(this), this);
        
        // Load messages
        loadMessages();
    }

    @Override
    public void onDisable() {
        if (graveTracker != null) {
            graveTracker.cleanup();
        }
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
        // Create messages.yml if it doesn't exist
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }

        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    // Update your existing onPlayerDeath method to include grave tracking
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        
        // Your existing grave creation code...
        
        // After creating the grave, register it with the tracking system
        UUID graveId = UUID.randomUUID(); // Or however you generate grave IDs
        graveTracker.registerGrave(
            graveId,
            player.getUniqueId(),
            player.getName(),
            graveLocation // The location where the grave was actually created
        );
        
        // Automatically give them a tracking compass if enabled
        if (getConfig().getBoolean("grave.tracking.auto-track", true)) {
            GraveTracking tracking = graveTracker.findLatestGrave(player.getUniqueId());
            if (tracking != null) {
                graveTracker.startTracking(player, tracking);
            }
        }
    }
}
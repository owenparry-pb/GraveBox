package me.bebo.graveBox.hud;

import me.bebo.graveBox.GraveBox;
import me.bebo.graveBox.Grave;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GraveHUD {
    private final GraveBox plugin;
    private final Map<UUID, BossBar> playerBars = new HashMap<>();
    private final int updateInterval;
    private final int maxDistance;
    private final String displayFormat;
    private final Map<String, String> directionArrows;
    private BukkitRunnable updateTask;

    public GraveHUD(GraveBox plugin) {
        this.plugin = plugin;
        this.updateInterval = plugin.getConfig().getInt("hud.update-interval", 10);
        this.maxDistance = plugin.getConfig().getInt("hud.max-tracking-distance", 500);
        this.displayFormat = plugin.getConfig().getString("hud.display-format", 
            "§e{direction} §7{distance}m §8({compass})");
        
        this.directionArrows = new HashMap<>();
        for (String direction : Arrays.asList("NORTH", "NORTH_EAST", "EAST", "SOUTH_EAST", 
                                            "SOUTH", "SOUTH_WEST", "WEST", "NORTH_WEST")) {
            directionArrows.put(direction, plugin.getConfig().getString("hud.direction-arrows." + direction, "→"));
        }
        
        startUpdateTask();
    }

    public boolean toggleHUD(Player player) {
        if (playerBars.containsKey(player.getUniqueId())) {
            // Remove HUD
            BossBar bar = playerBars.get(player.getUniqueId());
            bar.removePlayer(player);
            bar.setVisible(false);
            playerBars.remove(player.getUniqueId());
            player.sendMessage("§cGrave HUD disabled!");
            return false;
        } else {
            // Create new HUD
            BossBar bar = Bukkit.createBossBar(
                "Loading grave location...", 
                BarColor.WHITE, 
                BarStyle.SOLID
            );
            bar.addPlayer(player);
            bar.setVisible(true);
            playerBars.put(player.getUniqueId(), bar);
            player.sendMessage("§aGrave HUD enabled!");
            return true;
        }
    }

    private void startUpdateTask() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                for (UUID playerId : new HashSet<>(playerBars.keySet())) {
                    Player player = plugin.getServer().getPlayer(playerId);
                    if (player == null || !player.isOnline()) {
                        removeHUD(playerId);
                        continue;
                    }
                    updateHUD(player);
                }
            }
        };
        updateTask.runTaskTimer(plugin, updateInterval, updateInterval);
    }

    private void updateHUD(Player player) {
        BossBar bar = playerBars.get(player.getUniqueId());
        if (bar == null) return;

        Grave nearestGrave = plugin.getNearestGrave(player);
        if (nearestGrave == null) {
            bar.setTitle("§cNo graves found");
            bar.setProgress(0.0);
            return;
        }

        Location playerLoc = player.getLocation();
        Location graveLoc = nearestGrave.getLocation();
        
        // Calculate distance and direction
        int distance = (int) playerLoc.distance(graveLoc);
        if (distance > maxDistance) {
            bar.setTitle("§cGrave too far away");
            bar.setProgress(0.0);
            return;
        }

        String direction = getDirection(playerLoc, graveLoc);
        String compass = getCompassDirection(playerLoc.getYaw());
        
        // Format message
        String message = displayFormat
            .replace("{direction}", directionArrows.get(direction))
            .replace("{distance}", String.valueOf(distance))
            .replace("{compass}", compass);

        bar.setTitle(message);
        
        // Update progress bar based on distance
        double progress = 1.0 - (Math.min(distance, maxDistance) / (double) maxDistance);
        bar.setProgress(Math.max(0.0, Math.min(1.0, progress)));
    }

    public void removeHUDForGrave(UUID playerId) {
        removeHUD(playerId);
    }

    private void removeHUD(UUID playerId) {
        BossBar bar = playerBars.remove(playerId);
        if (bar != null) {
            bar.setVisible(false);
            bar.removeAll();
        }
    }

    private String getDirection(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double angle = Math.atan2(dz, dx);
        
        // Convert to degrees and normalize
        double degrees = Math.toDegrees(angle) - from.getYaw();
        degrees = (degrees + 360) % 360;
        
        // Convert degrees to 8-point compass direction
        int index = (int) Math.round(degrees / 45) % 8;
        String[] directions = {"EAST", "NORTH_EAST", "NORTH", "NORTH_WEST", 
                             "WEST", "SOUTH_WEST", "SOUTH", "SOUTH_EAST"};
        return directions[index];
    }

    private String getCompassDirection(float yaw) {
        yaw = (yaw + 360) % 360;
        if (yaw < 22.5) return "N";
        if (yaw < 67.5) return "NE";
        if (yaw < 112.5) return "E";
        if (yaw < 157.5) return "SE";
        if (yaw < 202.5) return "S";
        if (yaw < 247.5) return "SW";
        if (yaw < 292.5) return "W";
        if (yaw < 337.5) return "NW";
        return "N";
    }

    public void disable() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        // Clean up all boss bars
        for (UUID playerId : new HashSet<>(playerBars.keySet())) {
            removeHUD(playerId);
        }
    }
}
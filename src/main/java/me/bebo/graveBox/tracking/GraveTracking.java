package me.bebo.graveBox.tracking;

import me.bebo.graveBox.GraveBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.*;

public class GraveTracker {
    private final GraveBox plugin;
    private final Map<UUID, List<Location>> playerGraves;
    private final Map<UUID, GraveTracking> activeTracking;
    private final Map<UUID, GraveTracking> graveRegistry;

    public GraveTracker(GraveBox plugin) {
        this.plugin = plugin;
        this.playerGraves = new HashMap<>();
        this.activeTracking = new HashMap<>();
        this.graveRegistry = new HashMap<>();
    }

    // Methods used by CompassListener
    public boolean isTracking(Player player) {
        return activeTracking.containsKey(player.getUniqueId());
    }

    public GraveTracking getTrackedGrave(Player player) {
        return activeTracking.get(player.getUniqueId());
    }

    public void updateTracking(Player player, GraveTracking tracking) {
        if (tracking != null) {
            activeTracking.put(player.getUniqueId(), tracking);
            player.setCompassTarget(tracking.getLocation());
        }
    }

    // Method used by GraveGUI
    public List<Location> getGraves(Player player) {
        UUID playerId = player.getUniqueId();
        return playerGraves.getOrDefault(playerId, new ArrayList<>());
    }

    // Registration methods
    public void registerGrave(UUID graveId, UUID playerId, String playerName, Location location) {
        GraveTracking tracking = new GraveTracking(graveId, playerId, playerName, location);
        graveRegistry.put(graveId, tracking);
        playerGraves.computeIfAbsent(playerId, k -> new ArrayList<>()).add(location);
    }

    public GraveTracking findLatestGrave(UUID playerId) {
        return graveRegistry.values().stream()
                .filter(tracking -> tracking.getPlayerId().equals(playerId))
                .max(Comparator.comparingLong(GraveTracking::getTimestamp))
                .orElse(null);
    }

    // Cleanup method
    public void cleanup() {
        activeTracking.clear();
        playerGraves.clear();
        graveRegistry.clear();
    }

    // Additional utility methods
    public void stopTracking(Player player) {
        activeTracking.remove(player.getUniqueId());
    }

    public void removeGrave(UUID graveId) {
        GraveTracking tracking = graveRegistry.remove(graveId);
        if (tracking != null) {
            UUID playerId = tracking.getPlayerId();
            List<Location> graves = playerGraves.get(playerId);
            if (graves != null) {
                graves.remove(tracking.getLocation());
                if (graves.isEmpty()) {
                    playerGraves.remove(playerId);
                }
            }
        }
    }

    public boolean hasActiveGraves(Player player) {
        List<Location> graves = playerGraves.get(player.getUniqueId());
        return graves != null && !graves.isEmpty();
    }
}
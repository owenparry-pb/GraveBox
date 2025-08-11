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

    public List<Location> getGraves(Player player) {
        return playerGraves.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

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

    public void cleanup() {
        activeTracking.clear();
        playerGraves.clear();
        graveRegistry.clear();
    }
}
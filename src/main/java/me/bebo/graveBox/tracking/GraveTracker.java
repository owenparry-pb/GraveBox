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

    public void registerGrave(UUID graveId, UUID playerId, String playerName, Location location) {
        GraveTracking tracking = new GraveTracking(graveId, playerId, playerName, location);
        graveRegistry.put(graveId, tracking);
        addGrave(playerId, location);
    }

    public void addGrave(UUID playerId, Location location) {
        playerGraves.computeIfAbsent(playerId, k -> new ArrayList<>()).add(location);
    }

    public void addGrave(Player player, Location location) {
        addGrave(player.getUniqueId(), location);
    }

    public void startTracking(Player player, GraveTracking tracking) {
        activeTracking.put(player.getUniqueId(), tracking);
    }

    public void stopTracking(Player player) {
        activeTracking.remove(player.getUniqueId());
    }

    public GraveTracking getActiveTracking(Player player) {
        return activeTracking.get(player.getUniqueId());
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

    // ... (keep existing methods)
}
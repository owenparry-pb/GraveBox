package me.bebo.graveBox.tracking;

import me.bebo.graveBox.GraveBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GraveTracker {
    private final GraveBox plugin;
    private final Map<UUID, List<Location>> playerGraves;

    public GraveTracker(GraveBox plugin) {
        this.plugin = plugin;
        this.playerGraves = new HashMap<>();
    }

    public void addGrave(Player player, Location location) {
        UUID playerId = player.getUniqueId();
        playerGraves.computeIfAbsent(playerId, k -> new ArrayList<>()).add(location);
    }

    public List<Location> getGraves(Player player) {
        return playerGraves.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public boolean removeGrave(Player player, Location location) {
        UUID playerId = player.getUniqueId();
        List<Location> graves = playerGraves.get(playerId);
        if (graves != null) {
            boolean removed = graves.remove(location);
            if (graves.isEmpty()) {
                playerGraves.remove(playerId);
            }
            return removed;
        }
        return false;
    }

    public void clearGraves(Player player) {
        playerGraves.remove(player.getUniqueId());
    }

    public boolean hasGraves(Player player) {
        List<Location> graves = playerGraves.get(player.getUniqueId());
        return graves != null && !graves.isEmpty();
    }
}
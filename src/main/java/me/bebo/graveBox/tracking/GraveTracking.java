package me.bebo.graveBox.tracking;

import org.bukkit.Location;
import java.util.UUID;

public class GraveTracking {
    private final UUID graveId;
    private final UUID playerId;
    private final String playerName;
    private final Location location;
    private final long timestamp;

    public GraveTracking(UUID graveId, UUID playerId, String playerName, Location location) {
        this.graveId = graveId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.location = location;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getGraveId() {
        return graveId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Location getLocation() {
        return location;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
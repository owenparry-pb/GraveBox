package me.bebo.graveBox;

import org.bukkit.Location;
import java.util.UUID;

public class Grave {
    private final UUID id;
    private final UUID ownerId;
    private final Location location;

    public Grave(UUID id, UUID ownerId, Location location) {
        this.id = id;
        this.ownerId = ownerId;
        this.location = location;
    }

    public UUID getId() { 
        return id; 
    }

    public UUID getOwnerId() { 
        return ownerId; 
    }

    public Location getLocation() { 
        return location; 
    }
}
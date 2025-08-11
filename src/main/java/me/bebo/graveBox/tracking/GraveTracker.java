package me.bebo.graveBox.tracking;

import me.bebo.graveBox.GraveBox;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class GraveTracker {
    private final GraveBox plugin;
    private final Map<UUID, GraveTracking> activeGraves;

    public GraveTracker(GraveBox plugin) {
        this.plugin = plugin;
        this.activeGraves = new HashMap<>();
    }

    // Implementation continues...
}
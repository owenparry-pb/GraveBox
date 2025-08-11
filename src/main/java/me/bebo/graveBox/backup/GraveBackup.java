package me.bebo.graveBox.backup;

import me.bebo.graveBox.GraveBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;
import java.util.*;

public class GraveBackup {
    private final GraveBox plugin;
    private final Map<UUID, List<ItemStack>> backupInventories;

    public GraveBackup(GraveBox plugin) {
        this.plugin = plugin;
        this.backupInventories = new HashMap<>();
    }

    public void createBackup(Player player) {
        UUID playerId = player.getUniqueId();
        ItemStack[] contents = player.getInventory().getContents();
        backupInventories.put(playerId, Arrays.asList(contents));
    }

    public boolean restoreBackup(Player player) {
        UUID playerId = player.getUniqueId();
        List<ItemStack> backup = backupInventories.get(playerId);
        if (backup == null) {
            return false;
        }
        player.getInventory().setContents(backup.toArray(new ItemStack[0]));
        backupInventories.remove(playerId);
        return true;
    }
}
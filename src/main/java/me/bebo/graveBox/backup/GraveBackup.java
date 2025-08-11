package me.bebo.graveBox.backup;

import me.bebo.graveBox.GraveBox;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GraveBackup {
    private final GraveBox plugin;
    private final Map<UUID, GraveInventory> inventoryBackups;

    public GraveBackup(GraveBox plugin) {
        this.plugin = plugin;
        this.inventoryBackups = new HashMap<>();
    }

    public void backupInventory(Player player, Location graveLocation) {
        ItemStack[] inventory = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();
        float exp = player.getExp();
        int level = player.getLevel();

        GraveInventory graveInventory = new GraveInventory(inventory, armor, exp, level, graveLocation);
        inventoryBackups.put(player.getUniqueId(), graveInventory);

        // Clear player's inventory after backup
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setExp(0);
        player.setLevel(0);
    }

    public boolean restoreInventory(Player player) {
        GraveInventory backup = inventoryBackups.get(player.getUniqueId());
        if (backup != null) {
            player.getInventory().setContents(backup.getInventory());
            player.getInventory().setArmorContents(backup.getArmor());
            player.setExp(backup.getExp());
            player.setLevel(backup.getLevel());
            
            inventoryBackups.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    public boolean hasBackup(Player player) {
        return inventoryBackups.containsKey(player.getUniqueId());
    }

    public Location getGraveLocation(Player player) {
        GraveInventory backup = inventoryBackups.get(player.getUniqueId());
        return backup != null ? backup.getGraveLocation() : null;
    }

    private static class GraveInventory {
        private final ItemStack[] inventory;
        private final ItemStack[] armor;
        private final float exp;
        private final int level;
        private final Location graveLocation;

        public GraveInventory(ItemStack[] inventory, ItemStack[] armor, float exp, int level, Location graveLocation) {
            this.inventory = inventory;
            this.armor = armor;
            this.exp = exp;
            this.level = level;
            this.graveLocation = graveLocation;
        }

        public ItemStack[] getInventory() { return inventory; }
        public ItemStack[] getArmor() { return armor; }
        public float getExp() { return exp; }
        public int getLevel() { return level; }
        public Location getGraveLocation() { return graveLocation; }
    }
}
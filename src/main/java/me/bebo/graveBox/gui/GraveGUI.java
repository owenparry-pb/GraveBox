package me.bebo.graveBox.gui;

import me.bebo.graveBox.GraveBox;
import me.bebo.graveBox.tracking.GraveTracker;
import me.bebo.graveBox.backup.GraveBackup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraveGUI {
    private final GraveBox plugin;
    private final GraveTracker graveTracker;
    private final GraveBackup graveBackup;
    private static final String GUI_TITLE = "§8GraveBox Management";
    private static final int GUI_SIZE = 54; // 6 rows

    public GraveGUI(GraveBox plugin, GraveTracker graveTracker, GraveBackup graveBackup) {
        this.plugin = plugin;
        this.graveTracker = graveTracker;
        this.graveBackup = graveBackup;
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, GUI_SIZE, GUI_TITLE);
        List<Location> graves = graveTracker.getGraves(player);

        // Add grave locations
        int slot = 0;
        for (Location grave : graves) {
            if (slot >= GUI_SIZE - 9) break; // Reserve bottom row for controls
            
            ItemStack graveItem = createGraveItem(grave);
            gui.setItem(slot++, graveItem);
        }

        // Add control buttons in the bottom row
        int bottomRow = GUI_SIZE - 9;
        gui.setItem(bottomRow + 4, createMenuItem(Material.COMPASS, "§6Teleport to Nearest Grave", 
            "§7Click to teleport to your", "§7nearest grave location"));

        if (graveBackup.hasBackup(player)) {
            gui.setItem(bottomRow + 8, createMenuItem(Material.CHEST, "§aRestore Last Inventory", 
                "§7Click to restore your", "§7last saved inventory"));
        }

        player.openInventory(gui);
    }

    private ItemStack createGraveItem(Location location) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§cGrave Location");
            
            List<String> lore = new ArrayList<>();
            lore.add("§7World: " + location.getWorld().getName());
            lore.add("§7X: " + location.getBlockX());
            lore.add("§7Y: " + location.getBlockY());
            lore.add("§7Z: " + location.getBlockZ());
            lore.add("");
            lore.add("§eClick to teleport");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(line);
            }
            
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }

    public static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}
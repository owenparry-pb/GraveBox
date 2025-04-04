package me.bebo.graveBox;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public final class GraveBox extends JavaPlugin implements Listener {

    private final HashMap<UUID, Location> graveLocations = new HashMap<>();
    private Material graveMaterial;
    private boolean explosionProtection;
    private boolean indestructible;
    private boolean autoRemove;
    private boolean dropItemsOnDestroy;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();

        // Load config values
        loadConfigValues();

        // Register events
        Bukkit.getPluginManager().registerEvents(this, this);

        printBanner();
    }

    private void loadConfigValues() {
        graveMaterial = Material.valueOf(getConfig().getString("grave.material", "CHEST"));
        explosionProtection = getConfig().getBoolean("grave.explosion-protection", true);
        indestructible = getConfig().getBoolean("grave.indestructible", true);
        autoRemove = getConfig().getBoolean("grave.auto-remove", true);
        dropItemsOnDestroy = getConfig().getBoolean("grave.drop-items-on-destroy", false);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUniqueId();
        Location deathLocation = player.getLocation().getBlock().getLocation();

        event.getDrops().clear(); // Prevent item drops

        // Create grave
        deathLocation.getBlock().setType(graveMaterial);
        graveLocations.put(playerUUID, deathLocation);

        // Set custom name if configured
        String customName = getConfig().getString("grave.custom-name");
        if (customName != null && !customName.isEmpty()) {
            BlockState state = deathLocation.getBlock().getState();
            if (state instanceof Container) {
                Container container = (Container) state;
                container.setCustomName(customName.replace("{player}", player.getName()));
                container.update();
            }
        }

        // Store items
        BlockState state = deathLocation.getBlock().getState();
        if (state instanceof Container) {
            Container container = (Container) state;
            Inventory graveInventory = container.getInventory();
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null) {
                    graveInventory.addItem(item);
                }
            }
        }

        // Send message to player
        String message = getConfig().getString("messages.grave-created", "&aYour items have been stored in a grave at {x}, {y}, {z}")
                .replace("{x}", String.valueOf(deathLocation.getBlockX()))
                .replace("{y}", String.valueOf(deathLocation.getBlockY()))
                .replace("{z}", String.valueOf(deathLocation.getBlockZ()));
        player.sendMessage(message);
    }

    @EventHandler
    public void onChestOpen(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof Container)) return;

        Container container = (Container) event.getInventory().getHolder();
        Location chestLocation = container.getLocation();
        UUID ownerUUID = getGraveOwner(chestLocation);

        if (ownerUUID == null) return; // Not a grave chest

        Player player = (Player) event.getPlayer();
        if (!player.getUniqueId().equals(ownerUUID)) {
            player.sendMessage(getConfig().getString("messages.not-your-grave", "&cThis is not your grave!"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof Container)) return;

        Container container = (Container) event.getInventory().getHolder();
        Location chestLocation = container.getLocation();
        UUID ownerUUID = getGraveOwner(chestLocation);

        if (ownerUUID == null) return; // Not a grave chest

        Player player = (Player) event.getPlayer();
        if (autoRemove && player.getUniqueId().equals(ownerUUID) && event.getInventory().isEmpty()) {
            chestLocation.getBlock().setType(Material.AIR);
            graveLocations.remove(ownerUUID);
            player.sendMessage(getConfig().getString("messages.grave-emptied", "&aYou've retrieved all items from your grave"));
        }
    }

    @EventHandler
    public void onChestBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() != graveMaterial) return;

        Location blockLocation = block.getLocation();
        UUID ownerUUID = getGraveOwner(blockLocation);

        if (ownerUUID == null) return; // Not a grave chest

        if (indestructible) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(getConfig().getString("messages.cannot-destroy", "&cGraves cannot be destroyed!"));
            return;
        }

        if (dropItemsOnDestroy && block.getState() instanceof Container) {
            Container container = (Container) block.getState();
            for (ItemStack item : container.getInventory().getContents()) {
                if (item != null) {
                    block.getWorld().dropItemNaturally(blockLocation, item);
                }
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!explosionProtection) return;
        event.blockList().removeIf(block -> graveLocations.containsValue(block.getLocation()));
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!explosionProtection) return;
        event.blockList().removeIf(block -> graveLocations.containsValue(block.getLocation()));
    }

    private UUID getGraveOwner(Location location) {
        for (UUID uuid : graveLocations.keySet()) {
            if (graveLocations.get(uuid).equals(location)) {
                return uuid;
            }
        }
        return null;
    }

    private void printBanner() {
        Bukkit.getLogger().info("\n" +
                "██████╗ ███████╗██████╗  ██████╗      ██████╗ ██████╗ ██████╗ ███████╗ \n" +
                "██╔══██╗██╔════╝██╔══██╗██╔═══██╗    ██╔════╝██╔═══██╗██╔══██╗██╔════╝ \n" +
                "██████╔╝█████╗  ██████╔╝██║   ██║    ██║     ██║   ██║██║  ██║█████╗   \n" +
                "██╔══██╗██╔══╝  ██╔══██╗██║   ██║    ██║     ██║   ██║██║  ██║██╔══╝  \n" +
                "██████╔╝███████╗██████╔╝╚██████╔╝    ╚██████╗╚██████╔╝██████╔╝███████╗\n" +
                "╚═════╝ ╚══════╝╚═════╝  ╚═════╝      ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝\n" +
                "               StoneGrave Plugin by BOLA NAIEM FARID\n" +
                "                    Version 1.0 | Developed by Bebo\n" +
                "                GitHub: github.com/BeboNaiem | Discord: @BeboNaiem\n" +
                "============================================================================\n" +
                "                    Plugin loaded successfully! Enjoy! 🚀\n"
        );
    }
}
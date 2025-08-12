package me.bebo.graveBox.commands;

import me.bebo.graveBox.GraveBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GraveHUDCommand implements CommandExecutor {
    private final GraveBox plugin;

    public GraveHUDCommand(GraveBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        if (!player.hasPermission("gravebox.hud")) {
            player.sendMessage("§cYou don't have permission to use the grave HUD!");
            return true;
        }

        plugin.getGraveHUD().toggleHUD(player);
        return true;
    }
}
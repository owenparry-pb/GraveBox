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
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        // TODO: Add your command logic here
        // For example:
        // plugin.getGraveHUD().toggleHUD(player);
        return true;
    }
}
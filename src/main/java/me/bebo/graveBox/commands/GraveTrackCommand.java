package me.bebo.graveBox.commands;

import me.bebo.graveBox.GraveBox;
import me.bebo.graveBox.tracking.GraveTracking;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GraveTrackCommand implements CommandExecutor, TabCompleter {
    private final GraveBox plugin;

    public GraveTrackCommand(GraveBox plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.tl("messages.player-only"));
            return true;
        }

        if (!player.hasPermission("gravebox.track")) {
            player.sendMessage(plugin.tl("messages.commands.tracking.no-permission"));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("stop")) {
            plugin.getGraveTracker().stopTracking(player);
            player.sendMessage(plugin.tl("messages.tracking.stopped"));
            return true;
        }

        GraveTracking tracking = plugin.getGraveTracker().findLatestGrave(player.getUniqueId());
        if (tracking == null) {
            player.sendMessage(plugin.tl("messages.tracking.not-found"));
            return true;
        }

        plugin.getGraveTracker().startTracking(player, tracking);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("stop");
        }
        return completions;
    }
}
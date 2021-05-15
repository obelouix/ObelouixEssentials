package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.files.PlayerConfig;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class VanishCommand implements CommandExecutor, TabCompleter, Listener {

    private boolean isVanished = false;
    private Player player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (IPermission.test(sender, "obelouix.vanish") && sender instanceof Player) {
            PlayerConfig.load(Objects.requireNonNull(((Player) sender).getPlayer()));
            player = (Player) sender;
            for (Player otherPlayers : Bukkit.getOnlinePlayers()) {
                if (!otherPlayers.hasPermission("obelouix.vanish.seevanished")) {
                    if (!PlayerConfig.get().getBoolean("vanished")) {
                        if (!isVanished) isVanished = true;
                        otherPlayers.hidePlayer(Essentials.getInstance(), (Player) sender);
                    } else {
                        if (isVanished) isVanished = false;
                        otherPlayers.hidePlayer(Essentials.getInstance(), (Player) sender);
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    private void modifyServerList(ServerListPingEvent event) {
        if (isVanished) {
            event.iterator().forEachRemaining(player1 -> player.remove());

        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

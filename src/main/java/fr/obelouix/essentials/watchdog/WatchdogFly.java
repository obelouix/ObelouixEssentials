package fr.obelouix.essentials.watchdog;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class WatchdogFly implements Listener {

    private final Essentials plugin = Essentials.getInstance();

    @EventHandler(ignoreCancelled = true)
    public void checkPlayer(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final boolean protectCreativePlayers = Config.getRoot().node("watchdog", "fly", "protect-creative-mode").getBoolean();

        if (player.getGameMode() == GameMode.CREATIVE && !protectCreativePlayers) {
            takeAction(player);
        } else if(player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL){
            takeAction(player);
        }
    }

    private void takeAction(Player player) {
        if (player.isFlying() && !plugin.getServer().getAllowFlight() && !IPermission.simpleTest(player, "obelouix.fly")) {
            if (Objects.requireNonNull(Config.getRoot().node("watchdog", "fly", "action").getString()).equalsIgnoreCase("ban")) {
                Watchdog.getInstance().Ban(player, "flying without permission");
            } else {
                Watchdog.getInstance().Kick(player, "flying without permission");
            }
        }
    }

}

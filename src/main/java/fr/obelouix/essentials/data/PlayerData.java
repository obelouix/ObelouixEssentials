package fr.obelouix.essentials.data;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.utils.IPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLocaleChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerData implements Listener {

    private static String playerLocale;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                playerLocale = IPlayer.getPlayerLocaleString(event.getPlayer());
            }
        };
        //execute the task 10 ticks ( = 500 ms) after player logged in
        bukkitRunnable.runTaskLater(Essentials.getInstance(), 10L);
    }

    @EventHandler
    public void onPlayerChangeLocale(PlayerLocaleChangeEvent event) {
        final BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                playerLocale = IPlayer.getPlayerLocaleString(event.getPlayer());
            }
        };
        //execute the task 10 ticks ( = 500 ms) after player logged in
        bukkitRunnable.runTaskLater(Essentials.getInstance(), 10L);
    }

    public String getPlayerLocale() {
        return playerLocale;
    }
}

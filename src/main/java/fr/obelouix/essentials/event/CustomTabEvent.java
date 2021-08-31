package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CustomTabEvent implements Listener {

    private BukkitRunnable runnable;
    private final Essentials plugin = Essentials.getInstance();

    @EventHandler
    public void setupTabOnPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                player.playerListName(setPlayerPingComponent(player));
            }
        };
        runnable.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!runnable.isCancelled()) runnable.cancel();
    }

    private Component setPlayerPingComponent(Player player) {
        Component ping = Component.text(player.getPing());

        if (player.getPing() < 50) {
            ping = ping.color(TextColor.color(0, 240, 0));
        } else if (player.getPing() < 100) {
            ping = ping.color(TextColor.color(255, 239, 23));
        } else if (player.getPing() < 150) {
            ping = ping.color(TextColor.color(255, 143, 17));
        } else if (player.getPing() < 200) {
            ping = ping.color(TextColor.color(248, 4, 0));
        } else {
            ping = ping.color(TextColor.color(215, 0, 0));
        }
        return Component.text("[").color(TextColor.color(162, 162, 162))
                .append(ping).append(Component.text(" ms]").color(TextColor.color(162, 162, 162)))
                .append(Component.text(player.getName()).color(TextColor.color(255, 255, 255)));
    }

}

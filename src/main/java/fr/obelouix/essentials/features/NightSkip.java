package fr.obelouix.essentials.features;

import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;

import java.util.ArrayList;
import java.util.List;

import static fr.obelouix.essentials.config.Config.requiredSleepingPlayerPercentage;

public class NightSkip implements Listener {

    private static boolean bossBarShownToAllPlayers = false;
    private final List<Player> playerList = new ArrayList<>();
    private final BossBar bossBar = BossBar.bossBar(Component.text("", TextColor.color(1), TextDecoration.BOLD), 0, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);

    @EventHandler
    public void countSleepingPlayers(PlayerDeepSleepEvent event) {

        final Player player = event.getPlayer();
        if (player.isSleeping()) {
            if (!player.isOp() || !player.hasPermission("obelouix.nightskipping.exempt")) {
                playerList.add(player);
            }

            for (final Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("Sleeping");
            }
        } else {
            playerList.remove(player);
            for (final Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("No longer sleeping");
            }
        }
    }

/*    @EventHandler
    public void skipNight(PlayerDeepSleepEvent event) {
        final int percentageSleeping = (playerList.size() / Bukkit.getOnlinePlayers().size()) * 100;
        final Player player = event.getPlayer();
        if (percentageSleeping >= Essentials.getInstance().getConfig().getInt("player-sleep-percentage")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(I18n.getInstance().sendTranslatedMessage(player, "message.everyone_is_sleeping"));
                p.showBossBar(bossBar);
            }
            Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getWorld()
                    .setTime(18000);
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.hideBossBar(bossBar);
            }
        }
    }*/

    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        float requiredPlayers = (float) Math.ceil((float) Bukkit.getOnlinePlayers().size() * (requiredSleepingPlayerPercentage) / 100);
        System.out.println(requiredPlayers);
        if (!playerList.contains(event.getPlayer())) playerList.add(event.getPlayer());
        if (!bossBarShownToAllPlayers && event.getPlayer().getWorld().getTime() >= 12610) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.showBossBar(bossBar);
            }
            bossBarShownToAllPlayers = true;
        }
        bossBar.name(Component.text(playerList.size() + "/" + (int) requiredPlayers))
                .progress(playerList.size() / requiredPlayers);
    }

    @EventHandler
    public void onPlayerLeaveBed(PlayerBedLeaveEvent event) {
        float requiredPlayers = (float) Math.ceil((float) Bukkit.getOnlinePlayers().size() * (requiredSleepingPlayerPercentage) / 100);
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerList.remove(event.getPlayer());
            if (event.getPlayer().getWorld().getTime() < 12610) {
                player.hideBossBar(bossBar);
            }
            bossBar.name(Component.text(playerList.size() + "/" + (int) requiredPlayers))
                    .progress(playerList.size() / requiredPlayers);
        }
        bossBarShownToAllPlayers = false;
    }

}

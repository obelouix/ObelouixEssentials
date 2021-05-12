package fr.obelouix.essentials.features;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NightSkip implements Listener {

    private final List<Player> playerList = new ArrayList<>();
    private final BossBar bossBar = BossBar.bossBar(Component.text("test", TextColor.color(1), TextDecoration.BOLD), 0, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);

    @EventHandler
    public void countSleepingPlayers(PlayerDeepSleepEvent event) {

        final Player player = event.getPlayer();
        if (player.isSleeping()) {
            if (!player.isOp() || !player.hasPermission("obelouix.nightskipping.exempt")) {
                playerList.add(player);
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendMessage("Sleeping");
            }
        }
        else{
            playerList.remove(player);
            for (Player p: Bukkit.getOnlinePlayers()) {
                p.sendMessage("No longer sleeping");
            }
        }
    }
    @EventHandler
    public void skipNight(PlayerDeepSleepEvent event){
        final int percentageSleeping = (playerList.size() / Bukkit.getOnlinePlayers().size()) * 100;
        final Player player = event.getPlayer();
        if(percentageSleeping >= Essentials.getInstance().getConfig().getInt("player-sleep-percentage")){
            for (Player p: Bukkit.getOnlinePlayers()) {
                p.sendMessage(I18n.getInstance().getMessage("message.everyone_is_sleeping"));
                p.showBossBar(bossBar);
            }
            if(Objects.equals(Essentials.getInstance().getConfig().getString("type-of-skip"), "progressive")){
                Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getWorld()
                        .setTime(player.getWorld().getTime() + 100L * percentageSleeping);
            }
            else{
                Objects.requireNonNull(Bukkit.getPlayer(player.getName())).getWorld()
                        .setTime(0);
            }
        }
        else{
            for (Player p: Bukkit.getOnlinePlayers()) {
                p.hideBossBar(bossBar);
            }
        }
    }

}

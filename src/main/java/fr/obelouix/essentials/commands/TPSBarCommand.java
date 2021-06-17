package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TPSBarCommand extends Command implements Listener {

    //a list that contains every player who have successfully typed the command
    private static final ArrayList<String> playerList = new ArrayList<>();
    private final DecimalFormat df = new DecimalFormat("#.##");
    private double[] TPS = Bukkit.getTPS();
    private Component BarComponent = Component.text("");
    private Component TPSValueComponent = Component.text("");
    private final BossBar bossBar = BossBar.bossBar(BarComponent, (float) Math.max(Math.min(TPS[0] / 20.0D, 1.0D), 0.0D), BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
    private Component MSPTValueComponent = Component.text("");

    public TPSBarCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (IPermission.test(player, "obelouix.commands.tpsbar")) {
                if (!playerList.contains(player.getName())) {
                    updateBar().runTaskTimer(Essentials.getInstance(), 0, 20L);
                    player.showBossBar(bossBar);
                    playerList.add(player.getName());
                } else {
                    player.hideBossBar(bossBar);
                    playerList.remove(player.getName());
                }
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be run by a player");
        }
        return true;
    }

    /**
     * @return a {@link BukkitRunnable} task
     */
    private BukkitRunnable updateBar() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                //refresh the TPS at every run
                TPS = Bukkit.getTPS();

                BarComponent = Component.text("TPS: ").color(TextColor.color(0xFFBA06));
                TPSValueComponent = Component.text(df.format(TPS[0]));
                setTPSValueColor();
                BarComponent = BarComponent.append(TPSValueComponent)
                        .append(Component.text(" MSPT: ").color(TextColor.color(0xFFBA06)));

                setMSPTValueComponent();
                BarComponent = BarComponent.append(MSPTValueComponent);
                bossBar.name(BarComponent);
                bossBar.progress((float) Math.max(Math.min(TPS[0] / 20.0D, 1.0D), 0.0D));
            }
        };
    }

    /**
     * update the {@link TextColor} of the TPS based on his value
     */
    private void setTPSValueColor() {
        if (TPS[0] >= 18) {
            TPSValueComponent = TPSValueComponent.color(TextColor.color(0, 255, 0));
            bossBar.color(BossBar.Color.GREEN);
        } else if (TPS[0] >= 16 && TPS[0] < 18) {
            TPSValueComponent = TPSValueComponent.color(TextColor.color(0xFFBA06));
            bossBar.color(BossBar.Color.YELLOW);
        } else {
            TPSValueComponent = TPSValueComponent.color(TextColor.color(248, 4, 0));
            bossBar.color(BossBar.Color.RED);
        }
    }

    /**
     * update the {@link TextColor} of the MSPT based on his value
     */
    private void setMSPTValueComponent() {
        if (Bukkit.getServer().getAverageTickTime() <= 40) {
            MSPTValueComponent = Component.text(df.format(Bukkit.getServer().getAverageTickTime())).color(TextColor.color(0, 255, 0));
        } else if (Bukkit.getServer().getAverageTickTime() > 40 && Bukkit.getServer().getAverageTickTime() < 50) {
            MSPTValueComponent = Component.text(df.format(Bukkit.getServer().getAverageTickTime())).color(TextColor.color(255, 239, 23));
        } else {
            MSPTValueComponent = Component.text(df.format(Bukkit.getServer().getAverageTickTime())).color(TextColor.color(248, 4, 0));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (playerList.contains(player.getName())) {
            player.hideBossBar(bossBar);
            playerList.remove(player.getName());
        }
    }

}

package fr.obelouix.essentials.commands;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TPSBar extends @NotNull Command {

    private boolean isBossBarShown = false;

    protected TPSBar(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("obelouix.tpbsbar")) {

                final double[] TPS = Bukkit.getTPS();
                final Component component = Component.text("TPS: " + Math.round(TPS[0]));
                final BossBar bossBar = BossBar.bossBar(component, 1F, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);

                if (!isBossBarShown) {
                    player.showBossBar(bossBar);
                    isBossBarShown = true;
                } else {
                    player.hideBossBar(bossBar);
                    isBossBarShown = false;
                }
            }
        }
        return true;
    }
}

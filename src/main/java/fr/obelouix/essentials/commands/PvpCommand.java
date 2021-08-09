package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PvpCommand implements TabExecutor, Listener {

    private static final List<Player> pvpPlayers = new ArrayList<>();
    private final I18n i18n = I18n.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (IPermission.test(player, "obelouix.commands.pvp")) {
                Component message;
                if (args.length == 0) {
                    if (!pvpPlayers.contains(player.getPlayer())) {

                        pvpPlayers.add(player.getPlayer());

                        final Component defaultDisplayName = player.displayName();
                        player.displayName(Component.text("[pvp]").append(defaultDisplayName));

                        message = Component.text(i18n.sendTranslatedMessage(player, "obelouix.commands.pvp.enabled")).color(TextColor.color(183, 0, 0));
                    } else {
                        pvpPlayers.remove(player.getPlayer());
                        message = Component.text(i18n.sendTranslatedMessage(player, "obelouix.commands.pvp.disabled")).color(TextColor.color(0, 213, 0));
                    }
                    player.sendMessage(message);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    @EventHandler
    public void onPlayerAttacked(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity damagedEntity = event.getEntity();
        if (attacker instanceof Player && damagedEntity instanceof Player) {
            if (!pvpPlayers.contains(((Player) attacker).getPlayer()) || !pvpPlayers.contains(((Player) damagedEntity).getPlayer())) {
                event.setCancelled(true);
            }
        }
    }

}

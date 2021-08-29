package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Locale;

public class CommandPreprocessorEvent implements Listener {

    private final Essentials pluginInstance = Essentials.getInstance();
    private final I18n i18n = I18n.getInstance();

    @EventHandler
    public void preprocessPlayerCommand(PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage().toLowerCase(Locale.ROOT);
        /* if a player with the permission of reloading the server run this command
           he will be shown this message to ask him to not reload to avoid problems */
        if (message.equals("reload confirm") && event.getPlayer().hasPermission("bukkit.command.reload")) {
            event.getPlayer().sendMessage(ChatColor.DARK_RED +
                    i18n.getTranslation(event.getPlayer(), "avoid_reload"));
            pluginInstance.setReloading(true);
        }
    }

    @EventHandler
    public void preprocessConsoleCommand(ServerCommandEvent event) {
        final String command = event.getCommand().toLowerCase(Locale.ROOT);
        /* if the console run the reload command show this
           message to ask to not reload to avoid problems */
        if (command.equals("reload confirm")) {
            event.getSender().sendMessage(ChatColor.DARK_RED +
                    i18n.getTranslation(event.getSender(), "avoid_reload"));
            pluginInstance.setReloading(true);
        }
    }
}

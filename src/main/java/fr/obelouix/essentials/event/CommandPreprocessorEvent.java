package fr.obelouix.essentials.event;

import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Locale;

public class CommandPreprocessorEvent implements Listener {

    private final I18n i18n = I18n.getInstance();

    @EventHandler
    public void preprocessPlayerCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage().toLowerCase(Locale.ROOT);
        /* if a player with the permission of reloading the server run this command
           he will be shown this message to ask him to not reload to avoid problems */
        if (message.startsWith("/reload") && event.getPlayer().hasPermission("bukkit.command.reload")) {
            event.getPlayer().sendMessage(ChatColor.DARK_RED +
                    i18n.sendTranslatedMessage(event.getPlayer(), "avoid_reload"));
        }
    }

}

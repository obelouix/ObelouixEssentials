package fr.obelouix.essentials.security;

import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.i18n.I18n;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;

public class ChatSecurity implements Listener {

    private final I18n i18n = I18n.getInstance();

    @EventHandler
    public void onPlayerSendLink(AsyncChatEvent event) {
        if (!Config.allowHTTPURL) {
            final Player player = event.getPlayer();
            //force the message tp lowercase and remove all spaces so the player can't bypass url checking
            final String chatMessage = PlainComponentSerializer.plain().serialize(event.message()).replace(" ", "").toLowerCase(Locale.ROOT);
            if (chatMessage.contains("http://")) {
                player.sendMessage(ChatColor.DARK_RED + i18n.sendTranslatedMessage(player, "chat.security.http.forbidden"));
                event.setCancelled(true);
            }
        }
    }

}

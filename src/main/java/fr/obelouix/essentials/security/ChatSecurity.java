package fr.obelouix.essentials.security;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.i18n.I18n;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class ChatSecurity implements Listener {

    private static String lastMessage = "";
    private final I18n i18n = I18n.getInstance();
    private int identicalLastMessage = 0;

    @EventHandler
    public void onPlayerSendLink(AsyncChatEvent event) {
        if (!Config.allowHTTPURL) {
            final Player player = event.getPlayer();
            //force the message tp lowercase and remove all spaces so the player can't bypass url checking
            final String chatMessage = PlainTextComponentSerializer.plainText().serialize(event.message()).replace(" ", "").toLowerCase(Locale.ROOT);
            final Predicate<String> matches = Pattern.compile("^((http://|ftp://|)(www.|)[a-zA-Z0-9]+(\\.[a-zA-Z]+)+.*)$").asMatchPredicate();
            if (matches.test(chatMessage)) {
                player.sendMessage(ChatColor.DARK_RED + i18n.sendTranslatedMessage(player, "chat.security.http.forbidden"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void kickOnSpam(AsyncChatEvent event) {
        final String chatMessage = PlainTextComponentSerializer.plainText().serialize(event.message());
        if (lastMessage.equalsIgnoreCase(chatMessage.replace(" ", ""))) {
            identicalLastMessage += 1;
            //this will allow to kick the player if he continue to spam after relogging
            if (identicalLastMessage >= Config.spamThreshold - 1) {
                Bukkit.getScheduler().runTask(Essentials.getInstance(),
                        () -> event.getPlayer().kick(Component.text(i18n.sendTranslatedMessage(event.getPlayer(), "kick.spam")).color(TextColor.color(139, 0, 0))));
            }
        }
        lastMessage = chatMessage;
    }

}

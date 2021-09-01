package fr.obelouix.essentials.event;

import fr.obelouix.essentials.components.PlayerComponent;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.utils.IPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.regex.Pattern;

public class ChatEvent implements Listener {

    private final PlayerComponent playerComponent = new PlayerComponent();

    @EventHandler
    public void formatChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String prefix = ChatColor.translateAlternateColorCodes('&', IPlayer.getPrefix(player));
        String suffix = ChatColor.translateAlternateColorCodes('&', IPlayer.getSuffix(player));

//        Audiences.getAdminAudience().sendMessage(Component.text("coucou"));
        for (Player online : Bukkit.getOnlinePlayers()) {
            event.renderer((player1, displayName, message, audience) ->
                    displayName
                            .replaceText(builder -> builder.once().match(Pattern.compile(" ")).replacement(""))
                            .replaceText(builder -> builder.match(PlainTextComponentSerializer.plainText().serialize(displayName))
                                    .replacement(playerComponent.player(online, player1)))
                            .append(Component.text(Config.chatSeparator + " "))
                            .append(message));
        }

/*        player.displayName(Component.text(prefix + " ")
                        .replaceText(TextReplacementConfig.builder()
                                .match("<")
                                .replacement("")
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("&")
                                .replacement(Component.text("", format(player.displayName())))
                                .build())
                .append(Component.text(player.getName())
                        .replaceText(TextReplacementConfig.builder()
                                .match(player.getName())
                                .replacement(playerComponent.player(player, player))
                                .build()))
                .append(Component.text(" " + suffix))
        );*/
    }

    private Style format(Component component) {
        String color = PlainTextComponentSerializer.plainText().serialize(component);
        if (color.contains("&") && !color.contains("&#")) {
            return Style.style(TextColor.color(color.charAt(color.indexOf("&") + 1)));
        }
        return Style.style(TextColor.color(255, 255, 255));
    }
}

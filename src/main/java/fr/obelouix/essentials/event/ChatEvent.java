package fr.obelouix.essentials.event;

import fr.obelouix.essentials.components.PlayerComponent;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.utils.IPlayer;
import fr.obelouix.essentials.utils.TextColorFormatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class ChatEvent implements Listener {

    private final PlayerComponent playerComponent = new PlayerComponent();
    private Component playerMessage;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void formatChat(AsyncChatEvent event) {
        Player player = event.getPlayer();


/*        player.displayName(Component.text(prefix + " ")
                .replaceText(TextReplacementConfig.builder()
                        .match("&")
                        .replacement(Component.text("", format(player.displayName())))
                        .build())
                .append(Component.text(player.getName()))
                .append(Component.text(" " + suffix))
        );*/

        event.renderer(this::chatRender);

//        Audiences.getAdminAudience().sendMessage(Component.text("coucou"));
/*        for (Player online : Bukkit.getOnlinePlayers()) {
            event.renderer((player1, displayName, message, audience) ->
                    displayName
                            .replaceText(builder -> builder.once().match(Pattern.compile(" ")).replacement(""))
                            .replaceText(builder -> builder.match("&").replacement(Component.text("", format(player.displayName()))))
                            .replaceText(builder -> builder.match(PlainTextComponentSerializer.plainText().serialize(displayName))
                                    .replacement(playerComponent.player(online, player1)))
                            .append(Component.text(Config.chatSeparator + " "))
                            .append(message));
        }*/
    }

    @EventHandler
    public void setupOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        final String prefix = ChatColor.translateAlternateColorCodes('&', IPlayer.getPrefix(player));
        final String suffix = ChatColor.translateAlternateColorCodes('&', IPlayer.getSuffix(player));

        if (!IPlayer.getGroup(player).equals("default")) {
            player.displayName(Component.text(prefix + " " + player.getName() + " " + suffix));
        } else {
            player.displayName(Component.text(player.getName(), TextColor.color(117, 117, 117)));
        }
    }

    private @NotNull Component chatRender(@NotNull Player player, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience audience) {
        final @NotNull TextComponent prefix = Component.text(IPlayer.getPrefix(player));
        final @NotNull TextComponent suffix = Component.text(IPlayer.getSuffix(player));
        @NotNull Component chatFormat = Component.text(Config.chatFormat);
        @NotNull String serializedFormat = PlainTextComponentSerializer.plainText().serialize(chatFormat)
                .replaceFirst("\\{displayname}", player.getName())
                .replaceFirst("\\{message}", PlainTextComponentSerializer.plainText().serialize(message));
        playerMessage = message;

        if (!IPlayer.getGroup(player).equals("default")) {
            player.displayName(prefix.append(Component.text(player.getName() + " ").append(suffix)));
        } else {
            player.displayName(Component.text(player.getName(), TextColor.color(117, 117, 117)));
            playerMessage = message.color(TextColor.color(117, 117, 117));
        }

        return TextColorFormatter.colorFormatter(PlainTextComponentSerializer.plainText().deserialize(serializedFormat));
    }

}

package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import fr.obelouix.essentials.utils.TextColorFormatter;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class ChatEvent implements Listener {

    private final Essentials plugin = Essentials.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void formatChat(AsyncChatEvent event) {
        event.renderer(this::chatRender);

    }

    private @NotNull Component chatRender(@NotNull Player player, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience audience) {
        Component msg = message;

        // If the message sender doesn't have the permission to format his message, remove all formatting codes
        if (!IPermission.simpleTest(player, "obelouix.chat.formatting")) {
            msg = msg.replaceText(builder -> builder.match(Pattern.compile("(&)?&([0-9a-fk-orA-FK-OR])"))
                            .replacement(""))
                    .replaceText(builder -> builder.match(Pattern.compile("(&)?&#([0-9a-fA-F]{6})")).replacement(""));
        }
        if (plugin.getLuckPermsAPI() != null) {
            final @NotNull TextComponent prefix = Component.text(IPlayer.getPrefix(player));
            final @NotNull TextComponent suffix = Component.text(IPlayer.getSuffix(player));
            final @NotNull Component chatFormat = Component.text(Config.chatFormat.get(IPlayer.getGroup(player)));


            final @NotNull String serializedFormat = PlainTextComponentSerializer.plainText().serialize(chatFormat)
                    .replaceFirst("\\{world}", player.getWorld().getName())
                    .replaceFirst("\\{prefix}", PlainTextComponentSerializer.plainText().serialize(prefix))
                    .replaceFirst("\\{displayname}", player.getName())
                    .replaceFirst("\\{suffix}", PlainTextComponentSerializer.plainText().serialize(suffix))
                    .replaceFirst("\\{message}", PlainTextComponentSerializer.plainText().serialize(msg));

            return TextColorFormatter.colorFormatter(PlainTextComponentSerializer.plainText().deserialize(serializedFormat));
        }
        return sourceDisplayName.append(Component.text(": ")).append(msg);
    }

}

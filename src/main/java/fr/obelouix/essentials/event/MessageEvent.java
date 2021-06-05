package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MessageEvent implements Listener {

    private final Essentials pluginInstance = Essentials.getInstance();

    @EventHandler
    public void onPlayerSendMessage(AsyncChatEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && Objects.requireNonNull(pluginInstance.getConfig().getString("op-color")).length() > 0) {
            player.displayName(Component.text(player.getName())
                    .color(TextColor.color(Objects.requireNonNull(
                            TextColor.fromHexString(Objects.requireNonNull(
                                    pluginInstance.getConfig().getString("op-color")))))));
        }
        }
}

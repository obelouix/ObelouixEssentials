package fr.obelouix.essentials.event;

import fr.obelouix.essentials.utils.IPlayer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatEvent implements Listener {

    @EventHandler
    public void formatChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String prefix = IPlayer.getPrefix(player);
        player.displayName(Component.text(prefix).append(Component.text(event.getPlayer().getName())));
    }

}

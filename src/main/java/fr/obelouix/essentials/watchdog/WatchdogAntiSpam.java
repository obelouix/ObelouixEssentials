package fr.obelouix.essentials.watchdog;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import fr.obelouix.essentials.config.Config;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.Map;

public class WatchdogAntiSpam implements Listener, WatchdogAction {

    private static final Multimap<Player, String> chatHistory = ArrayListMultimap.create();
    private String message;

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        message = PlainTextComponentSerializer.plainText().serialize(event.message());
        chatHistory.put(player, message);
        System.out.println(chatHistory);
        takeAction(player);

        /*
            Every 10 minutes, the first message and all the others keys that contains the first message
            are removed from the MultiMap
         */
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            String firstValue = String.valueOf(chatHistory.values().stream().findFirst());
            for (Map.Entry<Player, String> entry : chatHistory.entries()) {
                String s = entry.getValue();
                if (s.equals(firstValue)) {
                    chatHistory.remove(entry.getKey(), s);
                }
            }
        }, 12000L, 12000L);
    }

    private int countDuplicatedMessages(String message) {
        return Collections.frequency(chatHistory.values(), message);
    }

    @Override
    public void takeAction(Player player) {
        if (countDuplicatedMessages(message) >= Config.getSpamThreshold()) {
            for (Player playerToPunish : Bukkit.getOnlinePlayers()) {
                // Take action on every player who spammed the same message
                if (chatHistory.containsKey(playerToPunish)) {
                    plugin.getServer().getScheduler().runTask(plugin, () -> WatchdogAction.kick(playerToPunish, "spamming the same message"));
                }

            }
        }
    }
}

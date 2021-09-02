package fr.obelouix.essentials.watchdog;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.event.EventRegistry;
import fr.obelouix.essentials.utils.TextColorFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Watchdog {

    private final Essentials plugin = Essentials.getInstance();
    private static Watchdog instance;

    public void register(){
        if(Config.isIsWatchdogEnabled()){
            plugin.getLOGGER().info("Enabling Watchdog...");
            EventRegistry.getInstance().registerEvent(new WatchdogFly());
            plugin.getLOGGER().info("Watchdog successfully enabled");
        }
    }

    public static Watchdog getInstance() {
        if(instance == null){
            instance = new Watchdog();
        }
        return instance;
    }

    protected void Ban(final Player player, final String reason){
        Objects.requireNonNull(plugin.getServer()
                .getPlayer(player.getUniqueId())).banPlayer(ChatColor.DARK_RED + "[Watchdog]" +
                ChatColor.RESET + " You've been automatically banned for: " + reason + "\n\n"
         + ChatColor.AQUA + "If you think it's an error please contact a server administrator/moderator");
        plugin.getLOGGER().info("Watchdog banned " + player.getName() + " for: " + reason);
    }

    protected void Kick(final Player player, final String reason){
        Objects.requireNonNull(plugin.getServer()
                .getPlayer(player.getUniqueId())).kick(TextColorFormatter
                .colorFormatter(Component.text("&4[Watchdog]&r kicked you for: " + reason)));
        plugin.getLOGGER().info("Watchdog kicked " + player.getName() + " for: " + reason);
    }
}

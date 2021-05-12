package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.files.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerConfig.create(player);

        //Use a Runnable to get the player locale that will run once, when player join the server
        final BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!Objects.equals(PlayerConfig.get().getString("locale"), player.locale().toString())) {
                    //registering player locale in his file
                    PlayerConfig.load(player);
                    PlayerConfig.get().set("locale", player.locale().toString());
                    PlayerConfig.save();
                }
            }
        };

        task.runTaskLater(Essentials.getInstance(), 10L);

    }

}

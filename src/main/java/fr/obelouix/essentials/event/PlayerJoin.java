package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.files.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String UUID = player.getUniqueId().toString();
        final LocalDateTime now = LocalDateTime.now();
        try {
            //store player name and his UUID in the database when he join
            ObelouixEssentialsDB.getInstance().executeQuery(
                    "INSERT INTO players (name, uuid) \n" +
                            "SELECT '" + player.getName() + "', '" + UUID + "' \n" +
                            "WHERE NOT EXISTS(SELECT 1 FROM players WHERE uuid = '" +
                            UUID + "');");
            ObelouixEssentialsDB.getInstance().executeQuery(
                    "INSERT into connection_history(UUID,logon) \n"
                            + "SELECT '" + UUID + "','" + now + "'\n"
                            + "WHERE NOT EXISTS(SELECT uuid,logon from connection_history WHERE uuid='" + UUID + "' and logon = '" + now + "');");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
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
        PlayerConfig.get().set("vanished", false);
        PlayerConfig.save();
    }

}

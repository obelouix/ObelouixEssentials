package fr.obelouix.essentials.event;

import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.files.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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

            final String ecoUUID = ObelouixEssentialsDB.getInstance().getString("SELECT uuid FROM economy WHERE uuid = '" + UUID + "';");
            ObelouixEssentialsDB.getInstance().close();

            if (ecoUUID.equals("") || LocalDate.ofInstant(Instant.ofEpochMilli(player.getFirstPlayed()), ZoneId.systemDefault()).equals(LocalDate.now())) {
                ObelouixEssentialsDB.getInstance().executeQuery("INSERT INTO economy VALUES('" + UUID + "', 0.00);");
            }

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        PlayerConfig.create(player);

/*
        //Use a Runnable to get the player locale that will run once, when player join the server
        final BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!Objects.equals(PlayerConfig.get().getString("locale"), IPlayer.getPlayerLocaleString(player))) {
                    //registering player locale in his file
                    PlayerConfig.load(player);
                    PlayerConfig.get().set("locale", IPlayer.getPlayerLocaleString(player));
                    PlayerConfig.save();
                }
            }
        };
        task.runTaskLater(Essentials.getInstance(), 10L);
*/
        PlayerConfig.get().set("vanished", false);
        PlayerConfig.save();
    }

}

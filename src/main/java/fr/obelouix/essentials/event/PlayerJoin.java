package fr.obelouix.essentials.event;

import fr.obelouix.essentials.components.PlayerComponent;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.files.PlayerConfig;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class PlayerJoin implements Listener {

    private final I18n i18n = I18n.getInstance();

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

            /*if (ecoUUID.equals("") || LocalDate.ofInstant(Instant.ofEpochMilli(player.getFirstPlayed()), ZoneId.systemDefault()).equals(LocalDate.now())) {
                ObelouixEssentialsDB.getInstance().executeQuery("INSERT INTO economy VALUES('" + UUID + "', 0.00);");
            }*/

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

    @EventHandler(priority = EventPriority.LOW)
    public void changeJoinMessage(PlayerJoinEvent event) {

        final PlayerComponent playerComponent = new PlayerComponent();
        final Player target = event.getPlayer();

        for (final Player player : Bukkit.getOnlinePlayers()) {
            final Component joinMessage = Component.text(i18n.getTranslation(player, "obelouix.joinmessage"))
                    .color(TextColor.color(255, 239, 23))
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(playerComponent.player(player, target))
                            .build());

            event.joinMessage(joinMessage);
        }

    }

}

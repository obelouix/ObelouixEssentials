package fr.obelouix.essentials.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class Audiences implements Listener {

    private final List<Player> adminList = new ArrayList<>();
    private final List<Player> moderatorList = new ArrayList<>();
    private final List<Player> defaultList = new ArrayList<>();
    // This audience will receive everything
    private Audience adminAudience;
    // This audience will receive moderation things
    private Audience moderatorAudience;
    // This audience will receive basic things
    private Audience globalAudience;

    @EventHandler(ignoreCancelled = true)
    public void setupAudiences(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("obelouix.audience.admin")) {
            adminList.add(player);
        }
        if (player.hasPermission("obelouix.audience.moderator")) {
            moderatorList.add(player);
        }

        defaultList.add(player);
        adminAudience = Audience.audience(adminAudience);
        moderatorAudience = Audience.audience(moderatorAudience, adminAudience);
        globalAudience = Audience.audience(defaultList);
    }

    /**
     * The Admin audience is the audience with the highest level, moderators are excluded from this one
     *
     * @return the admin audience
     */
    public Audience getAdminAudience() {
        return adminAudience;
    }

    /**
     * This audience is for sending things only to people who are moderators <b>and</b> administrators
     *
     * @return the moderator audience
     */
    public Audience getModeratorAudience() {
        return moderatorAudience;
    }

    /**
     * This audience is for sending things to the whole server
     *
     * @return the global audience
     */
    public Audience getGlobalAudience() {
        return globalAudience;
    }
}

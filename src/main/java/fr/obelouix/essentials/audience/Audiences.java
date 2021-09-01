package fr.obelouix.essentials.audience;

import net.kyori.adventure.audience.Audience;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Audiences implements Listener {

    // This audience will receive everything
    private static Audience adminAudience;
    // This audience will receive moderation things
    private static Audience moderatorAudience;
    // This audience will receive basic things
    private static Audience globalAudience;
    private final List<UUID> adminList = new ArrayList<>();
    private final List<UUID> moderatorList = new ArrayList<>();
    private final List<UUID> defaultList = new ArrayList<>();

    /**
     * The Admin audience is the audience with the highest level, moderators are excluded from this one
     *
     * @return the admin audience
     */
    public static Audience getAdminAudience() {
        return adminAudience;
    }

    /**
     * This audience is for sending things only to people who are moderators <b>and</b> administrators
     *
     * @return the moderator audience
     */
    public static Audience getModeratorAudience() {
        return moderatorAudience;
    }

    /**
     * This audience is for sending things to the whole server
     *
     * @return the global audience
     */
    public static Audience getGlobalAudience() {
        return globalAudience;
    }
/*

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void setupAudiences(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("obelouix.audience.admin")) {
            adminList.add(player.getUniqueId());
        } else if (player.hasPermission("obelouix.audience.moderator")) {
            moderatorList.add(player.getUniqueId());
        }

        defaultList.add(player.getUniqueId());
        adminAudience = Audience.audience(adminAudience);
        moderatorAudience = Audience.audience(moderatorAudience, adminAudience);
        globalAudience = Audience.audience((Audience) defaultList);
    }
*/

/*    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void updateAudience(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("obelouix.audience.admin") && !adminList.contains(player)) {
            adminList.add(player.getUniqueId());
        } else if (player.hasPermission("obelouix.audience.moderator") && !moderatorList.contains(player)) {
            moderatorList.add(player.getUniqueId());
        }

        if (adminList.contains(player.getUniqueId()) && !player.hasPermission("obelouix.audience.admin")) {
            adminList.remove(player.getUniqueId());
            adminAudience = Audience.audience((Audience) adminList);
        } else if (moderatorList.contains(player.getUniqueId()) && !player.hasPermission("obelouix.audience.moderator")) {
            moderatorList.remove(player.getUniqueId());
            moderatorAudience = Audience.audience((Audience) moderatorList);
        }

    }*/
}

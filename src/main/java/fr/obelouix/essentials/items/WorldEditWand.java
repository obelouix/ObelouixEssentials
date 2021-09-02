package fr.obelouix.essentials.items;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Locale;

public class WorldEditWand implements Listener {

    private static ItemStack worldEditSelectionWand;
    private final Essentials plugin = Essentials.getInstance();
    private final I18n i18n = I18n.getInstance();

    public static ItemStack getWorldEditSelectionWand() {
        return worldEditSelectionWand;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractWithSelectionWand(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
//        com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt(player);
        final BaseItemStack wand = BukkitAdapter.adapt(worldEditSelectionWand);
        setupMeta(player, worldEditSelectionWand);

//        SessionManager sessionManager = new SessionManager(plugin.getFAWEProvider().getWorldEdit());
        final com.sk89q.worldedit.entity.Player wePlayer = BukkitAdapter.adapt(player);
        final LocalSession sessionManager = plugin.getFAWEProvider().getWorldEdit().getSessionManager().get(wePlayer);
        final EditSession editSession = sessionManager.createEditSession(wePlayer);
        if (player.getInventory().getItemInMainHand().equals(worldEditSelectionWand)) {
            Region regionSelector = sessionManager.getSelection(wePlayer.getWorld());
            regionSelector.flush();
        }
//        sessionManager.get(wePlayer).setTool(wand, Actor::checkAction, wePlayer);

        /* WorldEdit.getInstance().getEventBus().register(new Object() *//* [1] *//* {
            // Make sure you import WorldEdit's @Subscribe!
            @Subscribe
            public void onEditSessionEvent(EditSessionEvent editSessionEvent) {
                com.sk89q.worldedit.entity.Player wePlayer = (com.sk89q.worldedit.entity.Player) editSessionEvent.getActor();
                SessionManager sessionManager = new SessionManager(plugin.getFAWEProvider().getWorldEdit());
                sessionManager.get(wePlayer).setTool(wand, null, wePlayer);
*//*                if (IPermission.simpleTest(player, "worldedit.selection.pos")
                        && !wePlayer.getItemInHand(HandSide.MAIN_HAND).equals(BukkitAdapter.adapt(worldEditSelectionWand))
                        && wePlayer.getItemInHand(HandSide.MAIN_HAND).equals(BukkitAdapter.adapt(new ItemStack(Material.valueOf(
                        StringUtils.substringAfter(
                                plugin.getFAWEProvider().getWorldEdit().getConfiguration().wandItem.toUpperCase(Locale.ROOT), ":")))))) {
                    event.getAction()
                    editSessionEvent.setCancelled(true);
                }*//*
            }
        });*/

    }

    public void setupSelectionWand() {
        if (plugin.isFawePresent()) {

            worldEditSelectionWand = new ItemStack(
                    Material.valueOf(
                            StringUtils.substringAfter(
                                    plugin.getFAWEProvider().getWorldEdit().getConfiguration().wandItem.toUpperCase(Locale.ROOT), ":")));

        }
    }

    public void setupMeta(Player player, ItemStack item) {
        CustomItem.setItemName(item, Component.text(i18n.getTranslation(player, "obelouix.worldedit.selection_wand"),
                TextColor.color(255, 143, 17), TextDecoration.BOLD));

        CustomItem.addLore(item, List.of(Component.text(i18n.getTranslation(player, "obelouix.worldedit.selection_wand.lore"),
                TextColor.color(0, 210, 220)).decoration(TextDecoration.ITALIC, false)));
    }
}

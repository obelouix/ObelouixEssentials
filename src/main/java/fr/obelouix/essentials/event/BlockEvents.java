package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;

import java.util.logging.Logger;

public class BlockEvents implements Listener {

    private final Logger PLUGIN_LOGGER = Essentials.getInstance().getLOGGER();
    private final I18n i18n = I18n.getInstance();

    /**
     * This event will fire when a player will try to break a block and cancel the block breaking if he hasn't
     * the permission for the given block
     *
     * @param event
     */
    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (!IPermission.simpleTest(player, "obelouix.break." + event.getBlock().getTranslationKey())) {
            final TranslatableComponent blockComponent = Component.translatable(event.getBlock().getTranslationKey());
            final Component component = Component.text(i18n.sendTranslatedMessage(player, "obelouix.break.disallowed"))
                    .color(TextColor.color(183, 0, 0))
                    .append(blockComponent.color(TextColor.color(215, 0, 0)));
            player.sendMessage(component);
            PLUGIN_LOGGER.warning(player.getName() + " tried to break " + PlainTextComponentSerializer.plainText().serialize(blockComponent)
                    + " in " + event.getPlayer().getWorld().getName() + " at X:" + event.getBlock().getLocation().getBlockX()
                    + " Y:" + event.getBlock().getLocation().getBlockY()
                    + " Z:" + event.getBlock().getLocation().getBlockZ());
            event.setCancelled(true);
        }
    }

    /**
     * This event will fire when a player will try to place a block and cancel the block placing if he hasn't
     * the permission for the given block
     *
     * @param event
     */
    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (!IPermission.simpleTest(player, "obelouix.place." + event.getBlock().getTranslationKey())) {
            final TranslatableComponent blockComponent = Component.translatable(event.getBlock().getTranslationKey());
            final Component component = Component.text(i18n.sendTranslatedMessage(player, "obelouix.place.disallowed"))
                    .color(TextColor.color(183, 0, 0))
                    .append(blockComponent.color(TextColor.color(215, 0, 0)));
            player.sendMessage(component);
            PLUGIN_LOGGER.warning(player.getName() + " tried to place " + PlainTextComponentSerializer.plainText().serialize(blockComponent)
                    + " in " + event.getPlayer().getWorld().getName() + " at X:" + event.getBlock().getLocation().getBlockX()
                    + " Y:" + event.getBlock().getLocation().getBlockY()
                    + " Z:" + event.getBlock().getLocation().getBlockZ());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent event) {
        final Player player = event.getPlayer();
        if (!IPermission.simpleTest(player, "obelouix.sleep")) {
            final Component component = Component.text(i18n.sendTranslatedMessage(player, "obelouix.not_allowed_to_sleep"))
                    .color(TextColor.color(183, 0, 0));
            player.sendMessage(component);
            event.setCancelled(true);
        }
    }

/*
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        if ((action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.PHYSICAL)) &&
                !IPermission.simpleTest(player, "obelouix.interact." + Objects.requireNonNull(event.getClickedBlock()).getTranslationKey())) {
            for (Material material : interactiveBlocks) {
                if (event.getClickedBlock().getType() == material) {
                    event.setCancelled(true);
                }
            }
        }
    }
*/

}

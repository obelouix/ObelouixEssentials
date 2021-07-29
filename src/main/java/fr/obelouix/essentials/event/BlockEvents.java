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
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockEvents implements Listener {

    /**
     * This event will fire when a player will try to break a block and cancel the block breaking if he hasn't
     * the permission for the given block
     *
     * @param event
     */
    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (!IPermission.canBreak(player, "obelouix.break." + event.getBlock().getTranslationKey())) {
            final TranslatableComponent blockComponent = Component.translatable(event.getBlock().getTranslationKey());
            final Component component = Component.text(I18n.getInstance().sendTranslatedMessage(player, "obelouix.break.disallowed"))
                    .color(TextColor.color(183, 0, 0))
                    .append(blockComponent.color(TextColor.color(215, 0, 0)));
            player.sendMessage(component);
            Essentials.getInstance().getLOGGER().warning(player.getName() + " tried to break " + PlainTextComponentSerializer.plainText().serialize(blockComponent));
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
        if (!IPermission.canBreak(player, "obelouix.place." + event.getBlock().getTranslationKey())) {
            final TranslatableComponent blockComponent = Component.translatable(event.getBlock().getTranslationKey());
            final Component component = Component.text(I18n.getInstance().sendTranslatedMessage(player, "obelouix.place.disallowed"))
                    .color(TextColor.color(183, 0, 0))
                    .append(blockComponent.color(TextColor.color(215, 0, 0)));
            player.sendMessage(component);
            Essentials.getInstance().getLOGGER().warning(player.getName() + " tried to place " + PlainTextComponentSerializer.plainText().serialize(blockComponent));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    }

}

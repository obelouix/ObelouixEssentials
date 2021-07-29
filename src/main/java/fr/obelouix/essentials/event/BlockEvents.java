package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class BlockEvents implements Listener {

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!IPermission.canBreak(player, "obelouix.break." + event.getBlock().getTranslationKey())) {
            player.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(player, "obelouix.break.disallowed"));
            @NotNull TranslatableComponent blockComponent = Component.translatable("").key(event.getBlock().getTranslationKey());
            Essentials.getInstance().getLOGGER().info(player.getName() + " tried to break " + PlainTextComponentSerializer.plainText().serialize(blockComponent));
            event.setCancelled(true);
        }
    }

}

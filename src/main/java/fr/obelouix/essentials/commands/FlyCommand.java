package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import fr.obelouix.essentials.utils.TextColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FlyCommand extends BukkitCommand {

    private final Essentials plugin = Essentials.getInstance();
    private final I18n i18n = I18n.getInstance();

    public FlyCommand(String name) {
        super(name);
        this.setDescription("Enable/disable your fly");
        this.setUsage("/fly [player]");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length <= 1) {
            final boolean allowFlight;
            if (args.length == 0) {
                if (sender instanceof Player player && IPermission.test(sender, "obelouix.commands.flyy")) {
                    allowFlight = player.getAllowFlight();
                    player.setAllowFlight(!allowFlight);
                    sendFlyMessage(sender, !allowFlight, args);
                } else
                    plugin.getLOGGER().info("You can only enable fly of other players as console. Use: fly <player>");
            } else {
                if (IPermission.test(sender, "obelouix.commands.fly.others")) {
                    final String target = args[0];
                    if (IPlayer.isOnline(target, sender)) {
                        allowFlight = Objects.requireNonNull(plugin.getServer().getPlayer(target)).getAllowFlight();
                        Objects.requireNonNull(plugin.getServer().getPlayer(target)).setAllowFlight(!allowFlight);
                        sendFlyMessage(sender, !allowFlight, args);
                    }
                }

            }
        } else CommandManager.wrongCommandUsage(sender, this);
        return true;
    }

    private void sendFlyMessage(CommandSender sender, boolean allowFlight, String[] args) {
        Component message;
        Component player;

        if (args.length == 0) {
            if (allowFlight) {
                message = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.fly.enabled"))
                        .color(TextColors.GREEN.getTextColor());
            } else {
                message = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.fly.disabled"))
                        .color(TextColors.DARK_RED.getTextColor());
            }
        } else {
            player = Component.text(args[0]).color(TextColors.AQUA.getTextColor());
            if (allowFlight) {

                message = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.fly.target.enabled"))
                        .color(TextColors.GREEN.getTextColor())
                        .replaceText(TextReplacementConfig.builder().matchLiteral("{0}").replacement(player).build());
            } else {
                message = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.fly.target.disabled"))
                        .color(TextColors.DARK_RED.getTextColor())
                        .replaceText(TextReplacementConfig.builder().matchLiteral("{0}").replacement(player).build());
            }

        }
        sender.sendMessage(message);
    }


    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        final List<String> playerCollection = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("obelouix.commands.fly.others")) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                playerCollection.add(player.getName());
            }
        }
        return playerCollection;
    }
}

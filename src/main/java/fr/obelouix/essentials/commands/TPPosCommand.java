package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.NumericValue;
import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TPPosCommand extends BukkitCommand {

    private final I18n i18n = I18n.getInstance();

    public TPPosCommand(String name) {
        super(name);
        this.setUsage("/tppos <x> <y> <z>");
        this.setDescription("Teleport yourself to coordinates");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.tppos")) {
            if (sender instanceof Player player && args.length == 3) {

                for (final String argument : args) {
                    if (!NumericValue.isNumeric(argument)) {

                        final Component error = Component.text(StringUtils.capitalize(StringUtils.replace(i18n.sendTranslatedMessage(player,
                                        "obelouix.teleportion_aborted_error"), "{argument}", argument)))
                                .color(TextColor.color(255, 0, 0));

                        player.sendMessage(error);
                        return false;
                        }
                    }
                    final Location teleportLocation = new Location(((Player) sender).getWorld(),
                            Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));

                    player.sendMessage(Component.text(StringUtils.capitalize(i18n.sendTranslatedMessage(player, "obelouix.teleporting")))
                            .color(TextColor.color(255, 186, 6)));

                    PaperLib.teleportAsync(Objects.requireNonNull(player.getPlayer()), teleportLocation);

                } else CommandManager.wrongCommandUsage(sender, this);
        }
        return true;
    }
}

package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SettingsCommand extends BukkitCommand {


    protected SettingsCommand(@NotNull String name) {
        super(name);
        this.setUsage("/settings <commandname> <on|off>");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.settings")) {
            if (args.length <= 1) {
                CommandManager.wrongCommandUsage(sender, this);
            } else {

            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        if (args.length == 1) {
            return Config.getCommandList().asList();
        }
        if (args.length == 2) {
            return ImmutableList.of("on", "off");
        }
        return super.tabComplete(sender, alias, args, location);
    }

}

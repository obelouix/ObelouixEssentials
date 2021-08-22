package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DayCommand extends BukkitCommand {

    protected DayCommand(@NotNull String name) {
        super(name);
        this.setDescription("set the day in you current world");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player && IPermission.test(player, "obelouix.commands.day")) {
            player.getWorld().setTime(0);
            final TimeCommand timeCommand = new TimeCommand();
            timeCommand.sendPlayerTimeMessage(player, 0);
        }
        return true;
    }
}

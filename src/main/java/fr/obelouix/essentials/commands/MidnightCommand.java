package fr.obelouix.essentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MidnightCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
/*    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && IPermission.test(player, "obelouix.commands.time.midnight")) {
            player.getWorld().setTime(18000);
            final TimeCommand timeCommand = new TimeCommand(Essentials.getInstance(), (CommandManager) Essentials.getPaperCommandManager());
            timeCommand.sendPlayerTimeMessage(player, 18000);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }*/
}

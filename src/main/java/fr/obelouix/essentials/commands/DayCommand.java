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



  /*
    @Override
    public void execute(@NonNull CommandContext<CommandSender> commandContext) {
        paperCommandManager.command(
                paperCommandManager.commandBuilder("day", ArgumentDescription.of("set the day to your current world"))
                        .handler(context -> {
                            if(commandContext.getSender() instanceof Player player && IPermission.test(player, "obelouix.commands.time.day")){
                                player.getWorld().setTime(0);
                                final TimeCommand timeCommand = new TimeCommand();
                                timeCommand.sendPlayerTimeMessage(player, 0);
                            }
                        }).build()
        );
    }
*/

   /* @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && IPermission.test(player, "obelouix.commands.time.day")) {
            player.getWorld().setTime(0);
            final TimeCommand timeCommand = new TimeCommand();
            timeCommand.sendPlayerTimeMessage(player, 0);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }*/

}

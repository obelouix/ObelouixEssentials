package fr.obelouix.essentials.commands;

import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * class relating to command registration
 */
public final class CommandManager extends PaperCommandManager<CommandSender> {

    public CommandManager(final @NotNull Essentials plugin) throws Exception {
        super(plugin, CommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(), Function.identity());

        if (this.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }

        new DayCommand(plugin, this).register();
    }

    private void wrongCommandUsage(@NotNull CommandSender sender, @NotNull Command command) {
        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "wrong_command_usage") + "\n" + ChatColor.RED + command.getUsage());
    }
}

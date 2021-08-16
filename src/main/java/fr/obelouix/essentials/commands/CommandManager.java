package fr.obelouix.essentials.commands;

import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.audience.Audience;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * class relating to command registration
 */
public final class CommandManager extends PaperCommandManager<CommandSender> {

    private Audience audience;

    public CommandManager(final @NotNull Essentials plugin) throws Exception {
        super(plugin, AsynchronousCommandExecutionCoordinator.simpleCoordinator(),
                Function.identity(), Function.identity());

        audience = Audience.audience(this.audience);
        final boolean isBrigadierPresent = this.queryCapability(CloudBukkitCapabilities.COMMODORE_BRIGADIER) ||
                this.queryCapability(CloudBukkitCapabilities.BRIGADIER);
        if (this.queryCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
            this.registerBrigadier();
            System.out.println("Hi");
        }

        if (this.queryCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            this.registerAsynchronousCompletions();
        }
        registerCommands(plugin);
    }

    private void registerCommands(Essentials plugin) {
        new DayCommand(plugin, this).register();
        new TimeCommand(plugin, this).register();
    }

    private void wrongCommandUsage(@NotNull CommandSender sender, @NotNull Command command) {
        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "wrong_command_usage") + "\n" + ChatColor.RED + command.getUsage());
    }
}

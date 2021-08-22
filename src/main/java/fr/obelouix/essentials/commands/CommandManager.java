package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.audience.Audience;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * class relating to command registration
 */
public final class CommandManager {

    private static final Map<String, Boolean> commandStates = new HashMap<>();
    private Audience audience;

    public CommandManager(final @NotNull Essentials plugin) throws ReflectiveOperationException {
        audience = Audience.audience(this.audience);

        if (commandStates.get("day")) registerCommand(new DayCommand("day"), plugin);
        registerCommand(new SettingsCommand("settings"), plugin);
        registerCommand(new HelpCommand("help"), plugin);
        registerCommand(new AdminCommand("admin"), plugin);
        registerCommand(new OpenInventoryCommand("openinv"), plugin);
    }

    public static Map<String, Boolean> getCommandStates() {
        return commandStates;
    }

    public static void wrongCommandUsage(@NotNull CommandSender sender, @NotNull Command command) {
        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "wrong_command_usage") + "\n" + ChatColor.RED + command.getUsage());
    }

    private void registerCommand(Command command, Essentials plugin)
            throws ReflectiveOperationException {
        //Getting command map from CraftServer
        final Method commandMap = plugin.getServer().getClass().getMethod("getCommandMap", (Class<?>[]) null);
        //Invoking the method and getting the returned object (SimpleCommandMap)
        final Object cmdMap = commandMap.invoke(plugin.getServer(), (Object[]) null);
        //getting register method with parameters String and Command from SimpleCommandMap
        final Method register = cmdMap.getClass().getMethod("register", String.class, Command.class);
        //Registering the command provided above
        register.invoke(cmdMap, "obelouix", command);
        //All the exceptions thrown above are due to reflection, They will be thrown if any of the above methods
        //and objects used above change location or turn private. IF they do, let me know to update the thread!
    }

}

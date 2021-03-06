package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.utils.TextColors;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
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
        registerCommand(new EnderchestCommand("enderchest"), plugin);
        registerCommand(new TPPosCommand("tppos"), plugin);
        registerCommand(new FlyCommand("fly"), plugin);
        registerCommand(new GamemodeCommand("gamemode"), plugin);
        registerCommand(new DifficultyCommand("difficulty"), plugin);
        registerCommand(new ExperienceCommand("xp"), plugin);
    }

    public static Map<String, Boolean> getCommandStates() {
        return commandStates;
    }

    /**
     * send a message when the user used wrongly a command
     *
     * @param sender  the {@link CommandSender}
     * @param command the {@link Command} used
     */
    public static void wrongCommandUsage(@NotNull CommandSender sender, @NotNull Command command) {
        sender.sendMessage(Component.text(I18n.getInstance().getTranslation(sender, "wrong_command_usage") + "\n")
                .color(TextColors.DARK_RED.getTextColor())
                .append(Component.text(command.getUsage())
                        .color(TextColors.RED.getTextColor())
                        .clickEvent(ClickEvent.suggestCommand(command.getUsage()))));
    }

    /**
     * allow registering of commands in the Bukkit CommandMap instead of using the plugin.yml
     *
     * @param command the command to register
     * @param plugin  the {@link org.bukkit.plugin.java.JavaPlugin} where the command belong
     * @throws ReflectiveOperationException
     */
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
        //and objects used above change location or turn private
    }

}

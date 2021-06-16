package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * class relating to command registration
 */
public class CommandRegistrar {

    private static CommandRegistrar instance;
    private final Essentials PluginInstance = Essentials.getInstance();

    private CommandRegistrar() {
    }

    /**
     * @return instance of {@link CommandRegistrar} class
     */
    public static CommandRegistrar getInstance() {
        if (instance == null) {
            instance = new CommandRegistrar();
        }
        return instance;
    }

    public void init() {
        registerCommand("gamemode", new GamemodeCommand());
        registerCommand("time", new TimeCommand());
        registerCommand("day", new DayCommand());
        registerCommand("night", new NightCommand());
        registerCommand("morning", new MorningCommand());
        registerCommand("midday", new MiddayCommand());
        registerCommand("noon", new NoonCommand());
        registerCommand("midnight", new MidnightCommand());
        registerCommand("enderchest", new EnderchestCommand());
        registerCommand("openinv", new OpenInventoryCommand());
        registerCommand("freeze", new FreezeCommand());
        registerCommand("effect", new EffectCommand());
        registerCommand("vanish", new VanishCommand());
        registerCommand("clearchat", new ClearChatCommand());
        registerCommand("eco", new EconomyCommand());
        registerCommand("fly", new FlyCommand());

        //add the Pupur TPS bar if not using Pupur
        if (!PluginInstance.SERVER_VERSION.contains("Purpur")) {
            registerCommand("tpsbar", TPSBarCommand.class);
        }
        registerCommand("workbench", new WorkbenchCommand());
        registerCommand("ping", new PingCommand());
    }

    /**
     * Register commands using the plugin.yml
     *
     * @param command  the command string
     * @param executor class executor of the command
     */
    private void registerCommand(@NotNull String command, @NotNull CommandExecutor executor) {
        Objects.requireNonNull(PluginInstance.getCommand(command)).setExecutor(executor);
        Objects.requireNonNull(PluginInstance.getCommand(command)).setTabCompleter((TabCompleter) executor);
    }

    /**
     * This method allow to register commands without adding commands in the plugin.yml
     *
     * @param command command name
     * @param c       class of the command
     */
    private void registerCommand(@NotNull String command, @NotNull Class c) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            final Constructor[] constructors = c.getDeclaredConstructors();
            Constructor constructor = null;
            for (final Constructor value : constructors) {
                constructor = value;
                if (constructor.getGenericParameterTypes().length == 0) break;
            }
            assert constructor != null;
            constructor.setAccessible(true);
            commandMap.register("Obelouix", (Command) constructor.newInstance(command));

        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    protected void wrongCommandUsage(@NotNull CommandSender sender, @NotNull Command command) {
        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "wrong_command_usage") + "\n" + ChatColor.RED + command.getUsage());
    }
}

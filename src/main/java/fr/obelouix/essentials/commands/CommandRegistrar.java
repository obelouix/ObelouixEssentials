package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandRegistrar {

    private static CommandRegistrar instance;

    private CommandRegistrar() {
    }

    public static CommandRegistrar getInstance() {
        if (instance == null) {
            instance = new CommandRegistrar();
        }
        return instance;
    }

    public void init() {
        registerCommand("gamemode", new CommandGameMode());
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

    }

    private void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setExecutor(executor);
        Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setTabCompleter((TabCompleter) executor);
        Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setPermissionMessage(ChatColor.DARK_RED + I18n.getInstance().getMessage("no_permission"));

    }

    protected void wrongCommandUsage(CommandSender sender, Command command) {
        final Player player = (Player) sender;
        player.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(player, "wrong_command_usage") + "\n" + ChatColor.RED + command.getUsage());
    }

}

package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
        registerCommand("help", new HelpCommand());
        setAlias("help", I18n.getInstance().setI18NAliases("command.help.alias"));
    }

    private void registerCommand(String command, CommandExecutor executor) {
        Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setExecutor(executor);
        Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setTabCompleter((TabCompleter) executor);
        Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setPermissionMessage(ChatColor.DARK_RED + I18n.getInstance().getMessage("no_permission"));

    }

    private void setAlias(String command, List<String> aliases){
        Objects.requireNonNull(Objects.requireNonNull(Essentials.getInstance().getCommand(command)).setAliases(aliases));
    }

    protected void wrongCommandUsage(CommandSender sender, Command command) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getMessage("wrong_command_usage") + "\n" + ChatColor.RED + command.getUsage());
    }

}

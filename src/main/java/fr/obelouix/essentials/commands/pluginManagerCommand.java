package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class pluginManagerCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        command.setAliases(Collections.singletonList("plm"));
        if (sender.hasPermission("obelouix.plugins") || sender.isOp()) {
            if (args.length == 1) {
                if (Objects.equals(args[0], "list")) {
                    sender.sendMessage("Plugins" + getPluginList());
                }
            }
            if (args.length == 2) {
                if (Objects.equals(args[0], "unload")) {
                    for (int i = 0; i < getPluginList().size(); i++) {
                        if(i == getPluginList().size() - 1 && !Objects.equals(args[1], getPluginList().get(i).toString())){
                            sender.sendMessage(ChatColor.DARK_RED
                                    + MessageFormat.format(I18n.getInstance().getMessage("plugin_not_found"), getPluginList().get(i).toString()));
                        }
                        else{
                            Essentials.getInstance().getPluginLoader().disablePlugin(getPluginList().get(i));
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    private ArrayList<Plugin> getPluginList() {
        ArrayList<Plugin> pluginList = new ArrayList<>();
        Collections.addAll(pluginList, Bukkit.getPluginManager().getPlugins());
        return pluginList;
    }

}

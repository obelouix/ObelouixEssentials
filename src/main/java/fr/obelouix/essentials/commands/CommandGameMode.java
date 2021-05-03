package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class CommandGameMode implements CommandExecutor, TabCompleter {

    private final List<String> OPTIONS = ImmutableList.of("0", "survival", "1", "creative", "2", "adventure", "3", "spectator");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player target = getServer().getPlayer(args[0]);
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp() || player.hasPermission("obelouix.gamemode")) {
                if (args.length == 1) {
                    if (target == null) {
                        boolean isPlayer = true;
                        for (String opt : OPTIONS) {
                            if (Objects.equals(args[0], opt)) {
                                isPlayer = false;
                                setGameMode(command, args[0], player);
                                player.sendMessage(ChatColor.GREEN + I18n.getInstance().getMessage("ownGameModeUpdated") + " "
                                        + ChatColor.AQUA + I18n.getInstance().getMessage(args[0]));
                                break;
                            }
                        }
                        if (isPlayer) playerNotOnline(player, args);
                    } else {
                        String currentGameMode = null;
                        switch (target.getGameMode()) {
                            case CREATIVE -> currentGameMode = I18n.getInstance().getMessage("creative");
                            case SURVIVAL -> currentGameMode = I18n.getInstance().getMessage("survival");
                            case ADVENTURE -> currentGameMode = I18n.getInstance().getMessage("adventure");
                            case SPECTATOR -> currentGameMode = I18n.getInstance().getMessage("spectator");
                        }
                        player.sendMessage(MessageFormat.format(I18n.getInstance().getMessage("current_gamemode"),
                                ChatColor.AQUA + target.getName() + ChatColor.GREEN,
                                ChatColor.AQUA + currentGameMode));
                    }
                    return true;
                }
                if (args.length == 2) {

                    if (target == null) {
                        playerNotOnline(player, args);
                        return true;
                    } else {
                        setGameMode(command, args[1], target);
                        player.sendMessage(MessageFormat.format(I18n.getInstance().getMessage("otherGameModeUpdated"),
                                ChatColor.AQUA + target.getName() + ChatColor.GREEN,
                                ChatColor.AQUA + I18n.getInstance().getMessage(target.getGameMode().toString().toLowerCase())));
                        return true;
                    }
                } else {
                    CommandRegistrar.getInstance().wrongCommandUsage(player, command);
                    return true;
                }
            }
        }
        return true;
    }

    private boolean setGameMode(@NotNull Command command, @NotNull String args, Player player) {
        if (args.equals("0") || args.equalsIgnoreCase("survival")) {
            player.setGameMode(GameMode.SURVIVAL);
        } else if (args.equals("1") || args.equalsIgnoreCase("creative")) {
            player.setGameMode(GameMode.CREATIVE);
        } else if (args.equals("2") || args.equalsIgnoreCase("adventure")) {
            player.setGameMode(GameMode.ADVENTURE);
        } else if (args.equals("3") || args.equalsIgnoreCase("spectator")) {
            player.setGameMode(GameMode.SPECTATOR);
        } /*else {
            CommandRegistrar.getInstance().wrongCommandUsage(player, command);
        }*/
        return true;
    }

    private void sendGameModeUpdatedMessage(Player player, String GameMode) {
        player.sendMessage(ChatColor.GREEN + I18n.getInstance().getMessage("ownGameModeUpdated") + " "
                + ChatColor.AQUA + I18n.getInstance().getMessage(GameMode));
    }

    private void playerNotOnline(Player player, @NotNull String[] args) {
        player.sendMessage(ChatColor.GOLD + args[0] + " "
                + ChatColor.DARK_RED + I18n.getInstance().getMessage("player_not_online"));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completion = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], OPTIONS, completion);
        if(args.length == 1)
        {
            Collections.sort(completion);
            for (Player online : getServer().getOnlinePlayers()) {
                completion.add(online.getName());
            }
        }

        if(args.length == 2){
            completion.clear();
            StringUtil.copyPartialMatches(args[1], OPTIONS, completion);
        }

        return completion;
    }
}

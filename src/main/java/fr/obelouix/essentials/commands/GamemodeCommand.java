package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
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
import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class GamemodeCommand implements CommandExecutor, TabCompleter {

    private final List<String> OPTIONS = ImmutableList.of("0", "survival", "1", "creative", "2", "adventure", "3", "spectator");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0 && args.length < 3) {
            final Player target = getServer().getPlayer(args[0]);
            if (args.length == 1) {
                if (IPermission.test(sender, "obelouix.gamemode")) {

                    if (target == null) {

                        boolean isPlayer = true;
                        boolean wasSentByConsole = false;
                        for (final String opt : OPTIONS) {
                            if (Objects.equals(args[0], opt)) {
                                if (sender instanceof Player) {
                                    updateOwnGameMode((Player) sender, args[0]);
                                } else {
                                    sender.sendMessage(ChatColor.DARK_RED + "You can't do that as console !");
                                    wasSentByConsole = true;
                                    break;
                                }
                                isPlayer = false;
                                break;
                            }
                        }
                        if (isPlayer && !wasSentByConsole) {
                            IPlayer.isOnline(args[0], sender);
                        }

                    } else {
                        getCurrentTargetGameMode(sender, target);
                    }

                }
                return true;
            } else if (args.length == 2) {
                if (IPlayer.isOnline(args[0], sender)) {
                    updateOtherGameMode(sender, target, args[1]);
                }
            } else {
                CommandRegistrar.getInstance().wrongCommandUsage(sender, command);
            }
            return true;
        } else {
            CommandRegistrar.getInstance().wrongCommandUsage(sender, command);
        }
        return true;
    }

    private boolean setGameMode(@NotNull String args, Player player) {
        if (args.equals("0") || args.equalsIgnoreCase("survival")) {
            player.setGameMode(GameMode.SURVIVAL);
        } else if (args.equals("1") || args.equalsIgnoreCase("creative")) {
            player.setGameMode(GameMode.CREATIVE);
        } else if (args.equals("2") || args.equalsIgnoreCase("adventure")) {
            player.setGameMode(GameMode.ADVENTURE);
        } else if (args.equals("3") || args.equalsIgnoreCase("spectator")) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            return false;
        }
        return true;
    }

    private void updateOwnGameMode(Player player, String args) {
        setGameMode(args, player);
        player.sendMessage(ChatColor.GREEN + I18n.getInstance().sendTranslatedMessage(player, "ownGameModeUpdated") + " "
                + ChatColor.AQUA + I18n.getInstance().sendTranslatedMessage(player, player.getGameMode().toString().toLowerCase(Locale.ROOT)));
    }

    private void updateOtherGameMode(CommandSender sender, Player target, String args) {
        if (setGameMode(args, target)) {
            sender.sendMessage(MessageFormat.format(I18n.getInstance().sendTranslatedMessage(sender, "otherGameModeUpdated"),
                    ChatColor.AQUA + target.getName() + ChatColor.GREEN,
                    ChatColor.AQUA + I18n.getInstance().sendTranslatedMessage(sender, target.getGameMode().toString().toLowerCase())));
            if (target.hasPermission("obelouix.gamemmode.notify")) {
                target.sendMessage(MessageFormat.format(I18n.getInstance().sendTranslatedMessage(target, "command.gamemode.notify_on_change.author"),
                        ChatColor.DARK_RED + sender.getName() + ChatColor.GRAY, ChatColor.GREEN + args));
            } else {
                target.sendMessage(ChatColor.GRAY + MessageFormat.format(I18n.getInstance().sendTranslatedMessage(target, "command.gamemode.notify_on_change"),
                        ChatColor.GREEN + args));
            }
        } else {
            CommandRegistrar.getInstance().wrongCommandUsage(sender, Objects.requireNonNull(getServer().getPluginCommand("gamemode")));
        }
    }

    private void getCurrentTargetGameMode(CommandSender sender, Player target) {
        String currentGameMode = null;
        switch (target.getGameMode()) {
            case CREATIVE -> currentGameMode = I18n.getInstance().sendTranslatedMessage(sender, "creative");
            case ADVENTURE -> currentGameMode = I18n.getInstance().sendTranslatedMessage(sender, "adventure");
            case SPECTATOR -> currentGameMode = I18n.getInstance().sendTranslatedMessage(sender, "spectator");
            default -> currentGameMode = I18n.getInstance().sendTranslatedMessage(sender, "survival");
        }
        sender.sendMessage(MessageFormat.format(I18n.getInstance().sendTranslatedMessage(sender, "current_gamemode"),
                ChatColor.AQUA + target.getName() + ChatColor.GREEN,
                ChatColor.AQUA + currentGameMode));
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completion = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], OPTIONS, completion);
        if (args.length == 1) {
            Collections.sort(completion);
            for (final Player online : getServer().getOnlinePlayers()) {
                completion.add(online.getName());
            }
        }

        if (args.length == 2) {
            completion.clear();
            StringUtil.copyPartialMatches(args[1], OPTIONS, completion);
        }

        return completion;
    }
}

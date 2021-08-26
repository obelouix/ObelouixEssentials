package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.components.PlayerComponent;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.utils.IPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class GamemodeCommand extends BukkitCommand {

    private final List<String> OPTIONS = ImmutableList.of("survival", "creative", "adventure", "spectator");
    private final I18n i18n = I18n.getInstance();
    private final PlayerComponent playerComponent = new PlayerComponent();

    public GamemodeCommand(@NotNull String name) {
        super(name);
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length > 0 && args.length <= 2) {
            Component message;
            final Player target = getServer().getPlayer(args[0]);
            if (args.length == 1) {
                if (isArgumentAPlayer(args[0]) && IPlayer.isOnline(args[0], sender)) {
                    sender.sendMessage(Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.gamemode.get"))
                            .color(TextColor.color(85, 255, 85))
                            .replaceText(TextReplacementConfig.builder()
                                    .matchLiteral("{0}")
                                    .replacement(playerComponent.player(sender, Objects.requireNonNull(target))
                                            .color(TextColor.color(85, 255, 255)))
                                    .build())
                            .replaceText(TextReplacementConfig.builder()
                                    .matchLiteral("{1}")
                                    .replacement(Component.translatable("selectWorld.gameMode." + target.getGameMode().name().toLowerCase())
                                            .color(TextColor.color(85, 255, 255)))
                                    .build()));
                } else {
                    if (!isArgumentAPlayer(args[0]) && sender instanceof Player player) {
                        changeGamemode(args[0], player);
                        sender.sendMessage(messageOwnGamemodeChanged(sender, args[0]));
                    }
                }
            } else if (args.length == 2) {
                if (isArgumentAPlayer(args[0]) && IPlayer.isOnline(args[0], sender)) {
                    for (String gameMode : OPTIONS) {
                        if (gameMode.equalsIgnoreCase(args[1])) {
                            changeGamemode(gameMode, target);
                            if (Objects.requireNonNull(target).getName().equalsIgnoreCase(sender.getName())) {
                                sender.sendMessage(messageOwnGamemodeChanged(sender, args[1]));
                            } else {
                                sender.sendMessage(messageTargetGamemodeChanged(sender, target, args[1]));
                            }
                            break;
                        }
                    }
                }

            } else CommandManager.wrongCommandUsage(sender, this);
        } else CommandManager.wrongCommandUsage(sender, this);
        return false;
    }

    private boolean isArgumentAPlayer(String arg) {
        for (final String OPTS : OPTIONS) {
            if (arg.equalsIgnoreCase(OPTS)) {
                return false;
            }
        }
        return true;
    }


    private boolean changeGamemode(@NotNull String args, Player player) {
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

    private Component messageOwnGamemodeChanged(CommandSender sender, String gamemode) {
        return Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.gamemode.updated.self"))
                .color(TextColor.color(85, 255, 85))
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(Component.translatable("selectWorld.gameMode." + gamemode)
                                .color(TextColor.color(85, 255, 255)))
                        .build());
    }

    private Component messageTargetGamemodeChanged(CommandSender sender, Player target, String gamemode) {
        return Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.gamemode.updated"))
                .color(TextColor.color(85, 255, 85))
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{0}")
                        .replacement(playerComponent.player(sender, target)
                                .color(TextColor.color(85, 255, 255)))
                        .build())
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("{1}")
                        .replacement(Component.translatable("selectWorld.gameMode." + gamemode)
                                .color(TextColor.color(85, 255, 255)))
                        .build());
    }

    private void updateOtherGameMode(CommandSender sender, Player target, String args) {
        if (changeGamemode(args, target)) {
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
            // CommandManager.getInstance().wrongCommandUsage(sender, Objects.requireNonNull(getServer().getPluginCommand("gamemode")));
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
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

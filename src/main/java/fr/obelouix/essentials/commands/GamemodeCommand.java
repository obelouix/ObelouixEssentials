package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.components.PlayerComponent;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static org.bukkit.Bukkit.getServer;

public class GamemodeCommand extends BukkitCommand {

    private final Essentials plugin = Essentials.getInstance();
    private final List<String> OPTIONS = ImmutableList.of("survival", "creative", "adventure", "spectator");
    private final I18n i18n = I18n.getInstance();
    private final PlayerComponent playerComponent = new PlayerComponent();

    public GamemodeCommand(@NotNull String name) {
        super(name);
        this.setUsage("/gamemode [<player>|<gamemode>] [<gamemode>]");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 1) {
            if (sender instanceof Player player) {
                changeGamemode(player, args[0], player, "obelouix.commands.gamemode");
            }
        } else if (args.length == 2) {
            if (IPlayer.isOnline(args[1], sender)) {
                final Player target = Bukkit.getPlayer(args[1]);
                if (Objects.requireNonNull(target).getName().equalsIgnoreCase(sender.getName())) {
                    if (sender instanceof Player player) {
                        changeGamemode(sender, args[0], player, "obelouix.commands.gamemode");
                    }
                } else {
                    changeGamemode(sender, args[0], target, "obelouix.commands.gamemode.others");
                }

            }
        } else CommandManager.wrongCommandUsage(sender, this);
        return true;
    }


    private void changeGamemode(CommandSender sender, @NotNull String gamemode, Player target, String basePermission) {
        Component message;
        boolean hasPermission = false;
        switch (gamemode.toLowerCase(Locale.ROOT)) {
            case "adventure" -> {
                if (IPermission.test(sender, basePermission + ".adventure")) {
                    hasPermission = true;
                    target.setGameMode(GameMode.ADVENTURE);
                }
            }
            case "creative" -> {
                if (IPermission.test(sender, basePermission + ".creative")) {
                    hasPermission = true;
                    target.setGameMode(GameMode.CREATIVE);
                }
            }
            case "spectator" -> {
                if (IPermission.test(sender, basePermission + ".spectator")) {
                    hasPermission = true;
                    target.setGameMode(GameMode.SPECTATOR);
                }
            }
            case "survival" -> {
                if (IPermission.test(sender, basePermission + ".survival")) {
                    hasPermission = true;
                    target.setGameMode(GameMode.SURVIVAL);
                }
            }
            default -> {
                if (IPermission.test(sender, "obelouix.commands.gamemode")) {
                    hasPermission = true;
                    CommandManager.wrongCommandUsage(sender, this);
                }
            }
        }
        if (hasPermission) {
            if (sender.getName().equalsIgnoreCase(target.getName())) {
                message = messageOwnGamemodeChanged(sender, gamemode);
            } else {
                message = messageTargetGamemodeChanged(sender, target, gamemode);
            }
            sender.sendMessage(message);
        }

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

    /**
     * This check if the {@linkplain CommandSender} has a specific {@linkplain Permission} for changing gamemode
     * and return a {@link List} with the possibilities he has
     *
     * @param sender     the {@linkplain CommandSender}
     * @param permission the {@linkplain Permission} to check
     * @return a {@link List}
     */
    private List<String> checkGamemodePermission(CommandSender sender, String permission) {
        final List<String> completion = new ArrayList<>();
        for (final String gamemode : OPTIONS) {
            if (sender.hasPermission(permission + gamemode)) {
                completion.add(gamemode);
            }
        }
        return completion;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> completion = new ArrayList<>();
        if (args.length == 1) {
            completion = checkGamemodePermission(sender, "obelouix.commands.gamemode.");
        }

        if (args.length == 2) {
            if (sender.hasPermission("obelouix.commands.gamemode.others")) {
                for (final Player online : getServer().getOnlinePlayers()) {
                    completion.add(online.getName());
                }
            }
        }
        return completion;
    }
}

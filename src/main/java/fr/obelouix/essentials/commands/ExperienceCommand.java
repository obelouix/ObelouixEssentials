package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.components.PlayerComponent;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class ExperienceCommand extends BukkitCommand {

    private final I18n i18n = I18n.getInstance();
    private CommandSender sender;

    public ExperienceCommand(String name) {
        super(name);
        this.setUsage("/xp <player> [give|set|remove] [number[<l>]]");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (IPermission.test(sender, "bukkit.command.xp")) {
            if (args.length <= 3 && args.length != 0) {

                this.sender = sender;

                // the message that will be sent if the command succeed
                Component message;

                // the player tot check
                Player player = null;
                boolean isPlayerOnline = false;

                if (IPlayer.isOnline(args[0], sender)) {
                    player = Bukkit.getPlayer(args[0]);
                    isPlayerOnline = true;
                }
                // if the player is (not) online and there is only 2 arguments instead of 3 then warn the command sender
                if ((isPlayerOnline && args.length == 2) || (!isPlayerOnline && args.length == 2)) {
                    CommandManager.wrongCommandUsage(sender, this);
                }
                if (isPlayerOnline) {
                    if (args.length == 3) {

                        //if the 2nd argument contains ^ , we remove it
                        if (args[2].contains("^")) {
                            args[2] = args[2].replaceAll("\\^", "");
                        }
                        //remove any characters that is not equal to a number or l
                        args[2] = args[2].replaceAll("[^0-9^l]", "");

                        final String level = args[2].toLowerCase(Locale.ROOT);

                        switch (args[1].toLowerCase(Locale.ROOT)) {
                            case "give" -> {
                                if (isArgumentALevel(args[2])) {
                                    Objects.requireNonNull(player).giveExpLevels(Integer.parseInt(level.replace("l", "")));
                                } else {
                                    Objects.requireNonNull(player).giveExp(Integer.parseInt(level));
                                }
                            }
                            case "set" -> {
                                if (isArgumentALevel(args[2])) {
                                    Objects.requireNonNull(player).setLevel(Integer.parseInt(level.replace("l", "")));
                                } else {
                                    Objects.requireNonNull(player).setLevel(0);
                                    Objects.requireNonNull(player).giveExp(Integer.parseInt(level));
                                }
                            }
                            case "remove" -> {
                                if (isArgumentALevel(args[2])) {
                                    Objects.requireNonNull(player).giveExpLevels(-Integer.parseInt(level.replace("l", "")));
                                } else {
                                    Objects.requireNonNull(player).giveExp(-Integer.parseInt(level));
                                }
                            }
                        }
                    }

                    message = setMessage(player, args);
                    sender.sendMessage(message);

                }

            } else CommandManager.wrongCommandUsage(sender, this);
        }
        return false;
    }

    /**
     * This will define what message we have to send to  the command sender
     *
     * @param player the {@link Player} that the command is targeting
     * @param args   the command argumentts
     * @return {@link Component}
     */
    private Component setMessage(Player player, String[] args) {
        final PlayerComponent playerComponent = new PlayerComponent();
        Component component = Component.text("");
        if (args.length == 1) {
            if (player.getLevel() <= 1) {
                component = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.xp.get"), TextColor.color(TextColor.color(162, 162, 162)));
            } else {
                component = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.xp.get.multiple_levels"), TextColor.color(TextColor.color(162, 162, 162)));
            }

            component = component.replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(playerComponent.player(sender, player).color(TextColor.color(255, 200, 0)))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{1}")
                            .replacement(Component.text(player.getLevel(), TextColor.color(0, 240, 0)))
                            .build());

        } else if (args.length == 3) {
            switch (args[1].toLowerCase(Locale.ROOT)) {
                case "give" -> {
                    component = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.xp.give"), TextColor.color(TextColor.color(162, 162, 162)));
                }
                case "set" -> {
                    component = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.xp.set"), TextColor.color(TextColor.color(162, 162, 162)));
                }
                case "remove" -> {
                    component = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.xp.remove"), TextColor.color(TextColor.color(162, 162, 162)));
                }
            }

            if (isArgumentALevel(args[2])) {
                if (Integer.parseInt(args[2].toLowerCase(Locale.ROOT).replace("l", "")) <= 1) {
                    component = component.replaceText(TextReplacementConfig.builder()
                            .matchLiteral(i18n.sendTranslatedMessage(sender, "obelouix.levels"))
                            .replacement(Component.text(i18n.sendTranslatedMessage(sender, "obelouix.level")))
                            .build());
                }
            } else {
                component = component.replaceText(TextReplacementConfig.builder()
                        .matchLiteral(i18n.sendTranslatedMessage(sender, "obelouix.levels"))
                        .replacement(Component.text("xp"))
                        .build());

            }

            if (args[2].toLowerCase(Locale.ROOT).contains("l")) {
                args[2] = args[2].toLowerCase(Locale.ROOT).replace("l", "");
            }
            component = component.replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{0}")
                            .replacement(Component.text(args[2], TextColor.color(0, 240, 0)))
                            .build())
                    .replaceText(TextReplacementConfig.builder()
                            .matchLiteral("{1}")
                            .replacement(playerComponent.player(sender, player).color(TextColor.color(255, 200, 0)))
                            .build());
        }

        return component;
    }

    /**
     * This simply check if the passed argument contains a "l"
     *
     * @param levelArgument the argument that contains the levels
     * @return {@code true} or {@code false}
     */
    private boolean isArgumentALevel(String levelArgument) {
        return levelArgument.toLowerCase(Locale.ROOT).contains("l");
    }
}

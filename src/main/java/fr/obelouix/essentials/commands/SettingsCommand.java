package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsCommand extends BukkitCommand {

    private final Essentials plugin = Essentials.getInstance();
    private final I18n i18n = I18n.getInstance();
    private final CommandMap commandMap = plugin.getServer().getCommandMap();

    protected SettingsCommand(@NotNull String name) {
        super(name);
        this.setUsage("/settings <commandname> <on|off>");
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.settings")) {
            if (args.length != 2) {
                CommandManager.wrongCommandUsage(sender, this);
            } else {
                if (Config.getCommandList().contains(args[0])) {
                    Component commandAlready = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.settings.already") + " ")
                            .color(TextColor.color(255, 0, 0));
                    switch (args[1]) {
                        case "on" -> {
                            if (!CommandManager.getCommandStates().get(args[0])) {
                                Config.getRoot().node("commands", args[0]).raw(true);
                                Config.save(Config.getRoot());
                                sender.sendMessage(message(args[0], sender, true));
                            } else {
                                commandAlready = commandAlready.append(
                                        Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.settings.enabled"))
                                                .color(TextColor.color(255, 0, 0)));
                                sender.sendMessage(commandAlready);
                            }

                        }
                        case "off" -> {
                            if (CommandManager.getCommandStates().get(args[0])) {
                                Config.getRoot().node("commands", args[0]).raw(false);
                                Config.save(Config.getRoot());
                                sender.sendMessage(message(args[0], sender, false));
                            } else {
                                commandAlready = commandAlready.append(
                                        Component.text(i18n.sendTranslatedMessage(sender, "obelouix.commands.settings.disabled"))
                                                .color(TextColor.color(255, 0, 0)));
                                sender.sendMessage(commandAlready);
                            }

                        }
                        default -> CommandManager.wrongCommandUsage(sender, this);
                    }
                }
            }
        }
        return true;
    }

    private Component message(String command, CommandSender sender, boolean enabled) {
        final Matcher matcher = Pattern.compile("\\{command}")
                .matcher(i18n.sendTranslatedMessage(sender, "obelouix.commands.settings.base"));

        Component baseMessage = Component.text(matcher.replaceAll(command) + " ")
                .color(TextColor.color(159, 162, 166))
                .hoverEvent(HoverEvent.showText(Component.text(command)
                        .color(TextColor.color(0, 240, 0))
                        .append(Component.text("\n\n"
                                        + i18n.sendTranslatedMessage(sender, "obelouix.commands." + command + ".description"))
                                .color(TextColor.color(35, 219, 240)))

                ));

        if (enabled)
            baseMessage = baseMessage.append(Component.text(I18n.getInstance().sendTranslatedMessage(sender, "obelouix.commands.settings.enabled"))
                    .color(TextColor.color(35, 255, 5)));
        else
            baseMessage = baseMessage.append(Component.text(I18n.getInstance().sendTranslatedMessage(sender, "obelouix.commands.settings.disabled"))
                    .color(TextColor.color(255, 54, 30)));

        return baseMessage.append(Component.text("\n" + i18n.sendTranslatedMessage(sender, "obelouix.commands.settings.effective_next_restart"))
                        .color(TextColor.color(255, 54, 30)))
                .append(Component.text(" (" + i18n.sendTranslatedMessage(sender, "obelouix.commands.settings.no_reload") + ")")
                        .color(TextColor.color(255, 54, 30)).decorate(TextDecoration.BOLD));
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        if (sender.hasPermission("obelouix.commands.settings")) {
            if (args.length == 1) {
                return Config.getCommandList().asList();
            }
            if (args.length == 2) {
                return ImmutableList.of("on", "off");
            }
        }

        return super.tabComplete(sender, alias, args, location);
    }

}

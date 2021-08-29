package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends BukkitCommand {

    private final Essentials plugin = Essentials.getInstance();
    private final CommandMap commandMap = plugin.getServer().getCommandMap();
    private @NotNull
    final Map<String, Command> knownCommands = commandMap.getKnownCommands();
    final int maxPages = ((commandMap.getKnownCommands().keySet().size() / 18) / 3) + 1;
    private final I18n i18n = I18n.getInstance();
    private final HashMap<String, Component> helpMap = new HashMap<>();

    public HelpCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (sender instanceof Player player) {

            for (Map.Entry<String, Command> command : knownCommands.entrySet()) {
                if (commandMap.getCommand(command.getKey()).getPermission() != null) {
                    if (sender.hasPermission(commandMap.getCommand(command.getKey()).getPermission())) {
                        helpMap.put(commandMap.getCommand(command.getKey()).getName(),
                                Component.text("/" + commandMap.getCommand(command.getKey()).getName() + ":")
                                        .color(TextColor.color(35, 219, 240))
                                        .hoverEvent(HoverEvent.showText(Component.text(
                                                        i18n.getTranslation(sender, "obelouix.usage")
                                                                + ": ").color(TextColor.color(255, 186, 6))
                                                .append(Component.text(commandMap.getCommand(command.getKey()).getUsage())
                                                        .color(TextColor.color(255, 255, 255))
                                                        .append(Component.text("\n"
                                                                        + i18n.getTranslation(sender, "obelouix.commands.help.prefill"))
                                                                .color(TextColor.color(35, 255, 5)))
                                                )))
                                        .clickEvent(ClickEvent.suggestCommand("/" + commandMap.getCommand(command.getKey()).getName()))
                                        .append(Component.text(" " + commandMap.getCommand(command.getKey()).getDescription() + "\n")
                                                .color(TextColor.color(255, 255, 255))));
                    }
                }
            }


            if (args.length == 0 || args[0].equals("1")) {
                player.sendMessage(Component.text(i18n.getTranslation(sender, "obelouix.commands.help.tip"))
                        .color(TextColor.color(255, 186, 6)));
                player.sendMessage(helpChat(1, player));
            } else {
                player.sendMessage(helpChat(Integer.parseInt(args[0]), player));
            }
        }
        return true;
    }

    private Component helpChat(int page, CommandSender sender) {
        Component component = Component.text("");
        Component navbar = Component.text("");
        if (page == 1) {
            navbar = Component.text("==================== ")
                    .color(TextColor.color(66, 213, 49))
                    .append(Component.text("page " + page + "/" + maxPages)
                            .color(TextColor.color(52, 81, 200)))
                    .append(Component.text(" =================== ")
                            .color(TextColor.color(66, 213, 49)))
                    .append(Component.text(">>>" + "\n")
                            .color(TextColor.color(52, 81, 200))
                            .clickEvent(ClickEvent.runCommand("/help " + (page + 1)))
                            .hoverEvent(HoverEvent.showText(
                                    Component.text(i18n.getTranslation(sender, "obelouix.commands.help.next_page")))));
        } else if (page > 1 && page < maxPages) {
            navbar = Component.text("<<<")
                    .color(TextColor.color(52, 81, 200))
                    .clickEvent(ClickEvent.runCommand("/help " + (page - 1)))
                    .hoverEvent(HoverEvent.showText(
                            Component.text(i18n.getTranslation(sender, "obelouix.commands.help.previous_page"))))
                    .append(Component.text(" ================= ")
                            .color(TextColor.color(66, 213, 49)))
                    .append(Component.text("page " + page + "/" + helpMap.size() / 18)
                            .color(TextColor.color(52, 81, 200)))
                    .append(Component.text(" =================== ")
                            .color(TextColor.color(66, 213, 49)))
                    .append(Component.text(">>>" + "\n")
                            .color(TextColor.color(52, 81, 200))
                            .clickEvent(ClickEvent.runCommand("/help " + (page + 1)))
                            .hoverEvent(HoverEvent.showText(
                                    Component.text(i18n.getTranslation(sender, "obelouix.commands.help.next_page")))));

        } else if (page == maxPages) {
            navbar = Component.text("<<<")
                    .color(TextColor.color(52, 81, 200))
                    .clickEvent(ClickEvent.runCommand("/help " + (page - 1)))
                    .hoverEvent(HoverEvent.showText(
                            Component.text(i18n.getTranslation(sender, "obelouix.commands.help.previous_page"))))
                    .append(Component.text(" ================= ")
                            .color(TextColor.color(66, 213, 49)))
                    .append(Component.text("page " + page + "/" + helpMap.size() / 18)
                            .color(TextColor.color(52, 81, 200)))
                    .append(Component.text(" ======================\n")
                            .color(TextColor.color(66, 213, 49)));
        }
        for (int i = 0; i < helpMap.size(); i++) {
            int j = 0;
            if (i < 16) {
                if (page == 1) {
                    component = component.append((Component) helpMap.values().toArray()[i]);
                } else {
                    component = component.append((Component) helpMap.values().toArray()[i * page + j]);
                    j += 1;
                }
            } else break;
        }
        return navbar.append(component).append(navbar);
    }

}

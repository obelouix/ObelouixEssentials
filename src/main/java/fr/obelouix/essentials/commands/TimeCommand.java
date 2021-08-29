package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class TimeCommand implements CommandExecutor {

    private final I18n i18n = I18n.getInstance();
    private final List<String> times = Arrays.asList("morning", "midday", "noon", "midnight", "day", "night");
    private final DecimalFormat format = new DecimalFormat("00");
    private long worldHour;
    private long worldMinute;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    protected void sendPlayerTimeMessage(CommandSender sender, int time) {
        final Player player = (Player) sender;
        worldHour = time / 1000 + 6;
        worldMinute = (time % 1000) * 60 / 1000;

        if (worldHour > 23) {
            worldHour = worldHour - 24;
        }
        sender.sendMessage(ChatColor.GOLD + MessageFormat.format(i18n.getTranslation(player, "command.time.set"),
                ChatColor.RED + player.getWorld().getName() + ChatColor.GOLD,
                ChatColor.RED + format.format(worldHour) + "h" + format.format(worldMinute) + ChatColor.GOLD
        ));
    }

/*

    @Contract(pure = true)
    public static @NotNull String getCommandName() {
        return "time";
    }

    @Override
    public void register() {
        this.commandManager.command(
                        this.commandManager.commandBuilder("time")
                                .argument(StringArgument.optional("time"), ArgumentDescription.of("Time of day"))
                                .handler(commandContext -> {
                                    if (commandContext.getSender() instanceof Player player && IPermission.test(player, "obelouix.commands.time")) {
                                        Optional<Object> time = commandContext.getOptional("time");
                                        if (time.isEmpty()) {

                                            worldHour = player.getWorld().getTime() / 1000 + 6;
                                            worldMinute = ((player.getWorld().getTime()) % 1000) * 60 / 1000;

                                            if (worldHour > 23) {
                                                worldHour = worldHour - 24;
                                            }

                                            player.sendMessage(MessageFormat.format(ChatColor.GOLD + i18n.sendTranslatedMessage(player, "current_world_time"),
                                                    ChatColor.RED + format.format(worldHour) + "h" + format.format(worldMinute) + ChatColor.GOLD,
                                                    ChatColor.RED + ((Player) commandContext.getSender()).getWorld().getName() + ChatColor.GOLD));
                                        } else {
                                            switch (time.get().toString()) {
                                                case "day" -> {
                                                    player.getWorld().setTime(0);
                                                    sendPlayerTimeMessage(player, 0);
                                                }
                                                case "morning" -> {
                                                    player.getWorld().setTime(2000);
                                                    sendPlayerTimeMessage(player, 2000);
                                                }
                                                case "midday" -> {
                                                    player.getWorld().setTime(6000);
                                                    sendPlayerTimeMessage(player, 6000);
                                                }
                                                case "noon" -> {
                                                    player.getWorld().setTime(9000);
                                                    sendPlayerTimeMessage(player, 9000);
                                                }
                                                case "night" -> {
                                                    player.getWorld().setTime(13188);
                                                    sendPlayerTimeMessage(player, 13188);
                                                }
                                                case "midnight" -> {
                                                    player.getWorld().setTime(18000);
                                                    sendPlayerTimeMessage(player, 18000);
                                                }
                                                default -> player.sendMessage(i18n.sendTranslatedMessage(player, "obelouix.commands.time.incorrect_argument"));
                                            }
                                        }
                                    }
                                }))
                .setCommandSuggestionProcessor((preprocessingContext, strings) ->
                        Objects.requireNonNull(onTabComplete(preprocessingContext.getCommandContext().getSender()), (Supplier<String>) strings));
    }


    protected void sendPlayerTimeMessage(CommandSender sender, int time) {
        final Player player = (Player) sender;
        worldHour = time / 1000 + 6;
        worldMinute = (time % 1000) * 60 / 1000;

        if (worldHour > 23) {
            worldHour = worldHour - 24;
        }

        sender.sendMessage(ChatColor.GOLD + MessageFormat.format(i18n.sendTranslatedMessage(player, "command.time.set"),
                ChatColor.RED + player.getWorld().getName() + ChatColor.GOLD,
                ChatColor.RED + format.format(worldHour) + "h" + format.format(worldMinute) + ChatColor.GOLD
        ));
    }

    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender) {
        final List<String> completion = new ArrayList<>();
        for (final String time : times) {
            if (sender.hasPermission("obelouix.commands.time." + time)) {
                completion.add(time);
            }
        }
        return completion;
    }
*/

/*    public @NonNull List<@NonNull String> suggestions(
            final @NonNull CommandPreprocessingContext<CommandSender> commandContext,
            final @NonNull List<String> input
    ) {
        final List<String> completion = new ArrayList<>();
        for (final String time : times) {
            CommandSender sender = (CommandSender) commandContext.getSender();
            if (sender.hasPermission("obelouix.commands.time." + time)) {
                completion.add(time);
            }
        }
        return completion;
    }*/

}

package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EconomyCommand implements TabExecutor {

    private final ObelouixEssentialsDB dbInstance = ObelouixEssentialsDB.getInstance();
    private CommandSender sender;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.eco")) {
            this.sender = sender;
            if (args.length == 3) {
                @NotNull final String target = args[1];
                final float amount = Float.parseFloat(args[2]);
                //check if the amount entered by the sender is not negative
                if (amount < 0 && !args[0].equalsIgnoreCase("set")) {
                    sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender,
                            "obelouix.commands.eco.negative_balance_not_allowed"));

                } else {
                    switch (args[0].toLowerCase(Locale.ROOT)) {
                        case "give" -> modifyPlayerAccount(target, "give", amount);
                        case "set" -> modifyPlayerAccount(target, "set", amount);
                        case "remove" -> modifyPlayerAccount(target, "remove", amount);
                        default -> CommandRegistrar.getInstance().wrongCommandUsage(sender, command);
                    }
                }

            } else {
                CommandRegistrar.getInstance().wrongCommandUsage(sender, command);
            }
        }
        return true;
    }

    private void modifyPlayerAccount(String target, String operation, float amount) {
        String UUID = "";
        try {
            UUID = dbInstance.getString("SELECT uuid FROM players WHERE name='" + target + "'");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        if (!UUID.equals("")) {
            try {
                final float SQLamount = Float.parseFloat(dbInstance.getString("SELECT money FROM economy WHERE uuid='" + UUID + '"'));
                if (operation.equals("give")) {
                    dbInstance.executeQuery("UPDATE economy SET money=money+" + amount + " WHERE uuid = '" + UUID + "'");
                } else if (operation.equals("set")) {
                    if (Config.allowNegativeBalance && amount < 0) {
                        dbInstance.executeQuery("UPDATE economy SET money=" + amount + " WHERE uuid = '" + UUID + "'");
                    } else if (amount < 0) {
                        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender,
                                "obelouix.commands.eco.negative_balance_not_allowed"));
                    } else {
                        dbInstance.executeQuery("UPDATE economy SET money=" + amount + " WHERE uuid = '" + UUID + "'");
                    }
                    //this part is for the remove argument
                } else {
                    dbInstance.executeQuery("UPDATE economy SET money=money-" + amount + " WHERE uuid = '" + UUID + "'");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

        } else {
            sender.sendMessage(MessageFormat.format(ChatColor.DARK_RED +
                            I18n.getInstance().sendTranslatedMessage(sender, "database.player.not_found"),
                    ChatColor.RED + target + ChatColor.DARK_RED));
        }

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completion = new ArrayList<>();
        if (sender.hasPermission("obelouix.commands.eco")) {
            if (args.length == 1) {
                completion.add("give");
                completion.add("set");
                completion.add("remove");
            } else if (args.length == 2) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    completion.add(player.getName());
                }
            } else if (args.length == 3) {
                for (int i = 1; i < 1e9; i *= 10) {
                    if (i == 1) completion.add(String.valueOf(i));
                    completion.add(String.valueOf(i * 10));
                }
                Collections.sort(completion);
            }
        }
        return completion;
    }
}

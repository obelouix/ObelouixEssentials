package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.NumericValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EffectCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.effect")) {
            if (args.length == 1) {
                if (sender instanceof Player player) {
                    for (final PotionEffectType effectType : PotionEffectType.values()) {
                        if (args[0].equalsIgnoreCase(effectType.getName())) {
                            player.addPotionEffect(effectType.createEffect(600, 0));
                        }
                    }
                    if (args[0].equalsIgnoreCase("clear")) {
                        for (final PotionEffectType effectType : PotionEffectType.values()) {
                            if (player.hasPotionEffect(effectType)) {
                                player.removePotionEffect(effectType);
                            }
                        }
                    }
                } else {
                    for (final PotionEffectType effectType : PotionEffectType.values()) {
                        if (args[0].equalsIgnoreCase(effectType.getName())) {
                            sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getTranslation(sender, "command.effect.sender.console.cant_apply"));
                        }
                    }
                }
            }
            if (args.length > 1 && args.length < 5) {

                final Player target = Essentials.getInstance().getServer().getPlayer(args[0]);
                int duration = 600;
                int amplifier = 0;

                if (args.length >= 3 && NumericValue.isNumeric(args[2])) {
                        duration = Integer.parseInt(args[2]);
                }
                if (args.length == 4 && NumericValue.isNumeric(args[3])) {
                        amplifier = Integer.parseInt(args[3]);
                }

                if (target != null) {
                    if (args[1].equalsIgnoreCase("clear")) {
                        for (final PotionEffectType effectType : PotionEffectType.values()) {
                            if (target.hasPotionEffect(effectType)) {
                                //don't remove effects if player is frozen
                                if (FreezeCommand.getFrozenPlayers().get(FreezeCommand.getFrozenPlayers().indexOf(target.getName())) != null) {
                                    sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getTranslation(sender, "command.effect.clear.failed.cause.frozen"));
                                    break;
                                } else {
                                    target.removePotionEffect(effectType);
                                }
                            }
                        }
                    }
                    for (final PotionEffectType effectType : PotionEffectType.values()) {
                        if (args[1].equalsIgnoreCase(effectType.getName())) {
                            target.addPotionEffect(effectType.createEffect(duration, amplifier));
                            sender.sendMessage(ChatColor.GREEN +
                                    MessageFormat.format(I18n.getInstance().getTranslation(sender, "command.effect.applied.other"),
                                            ChatColor.AQUA + effectType.getName() + ChatColor.GREEN,
                                            ChatColor.GOLD + target.getName() + ChatColor.GREEN,
                                            ChatColor.RED + String.valueOf(duration * 20)));
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        final List<String> completion = new ArrayList<>();

        if (args.length == 1) {
            completion.add("clear");
            for (final PotionEffectType effect : PotionEffectType.values()) {
                completion.add(effect.getName().toLowerCase(Locale.ROOT));
            }

            if (sender.hasPermission("obelouix.commands.effect.others")) {
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    completion.add(player.getName());
                }
            }
        }
        if (args.length == 2) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                if (args[0].equalsIgnoreCase(player.getName())) {
                    completion.add("clear");
                    break;
                }
            }
            for (final PotionEffectType effect : PotionEffectType.values()) {
                if (!args[0].equalsIgnoreCase(effect.getName())) {
                    completion.add(effect.getName().toLowerCase(Locale.ROOT));
                } else {
                    completion.clear();
                    break;
                }
            }
        }
        return completion;
    }
}

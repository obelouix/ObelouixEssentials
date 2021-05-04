package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
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
        if (sender.hasPermission("obelouix.effects")) {
            if (args.length == 1) {
                if (sender instanceof Player player) {
                    for (PotionEffectType effectType : PotionEffectType.values()) {
                        if (args[0].equalsIgnoreCase(effectType.getName())) {
                            player.addPotionEffect(effectType.createEffect(600, 0));
                        }
                    }
                } else {
                    for (PotionEffectType effectType : PotionEffectType.values()) {
                        if (args[0].equalsIgnoreCase(effectType.getName())) {
                            sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getMessage("command.effect.sender.console.cant_apply"));
                        }
                    }
                }
            }
            if (args.length > 1 && args.length < 5) {

                Player target = Essentials.getInstance().getServer().getPlayer(args[0]);
                int duration = 600;
                int amplifier = 0;

                if (args.length >= 3 && args.length <= 4) {
                    if (NumericValue.isNumeric(args[2])) {
                        duration = Integer.parseInt(args[2]);
                    }
                }
                if (args.length == 4) {
                    if (NumericValue.isNumeric(args[3])) {
                        amplifier = Integer.parseInt(args[3]);
                    }
                }

                if (target != null) {
                    for (PotionEffectType effectType : PotionEffectType.values()) {
                        if (args[1].equalsIgnoreCase(effectType.getName())) {
                            target.addPotionEffect(effectType.createEffect(duration, amplifier));
                            sender.sendMessage(ChatColor.GREEN +
                                    MessageFormat.format(I18n.getInstance().getMessage("command.effect.applied.other"),
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

        List<String> completion = new ArrayList<>();

        if (args.length == 1) {
            for (PotionEffectType effect : PotionEffectType.values()) {
                completion.add(effect.getName().toLowerCase(Locale.ROOT));
            }

            if (sender.hasPermission("obelouix.effects.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completion.add(player.getName());
                }
            }
        }
        if (args.length == 2) {
            for (PotionEffectType effect : PotionEffectType.values()) {
                completion.add(effect.getName().toLowerCase(Locale.ROOT));
            }
        }
        return completion;
    }
}

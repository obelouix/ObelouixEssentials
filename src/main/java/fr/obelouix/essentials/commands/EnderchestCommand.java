package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EnderchestCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            if(args.length == 0){
                if(player.hasPermission("obelouix.enderchest")){
                    player.openInventory(player.getEnderChest());
                }
            } else if(args.length == 1){
                if(player.hasPermission("obelouix.enderchest.others")){
                    Player target = Essentials.getInstance().getServer().getPlayer(args[0]);
                    if(target != null){
                        player.openInventory(target.getEnderChest());
                    }
                    else
                    {
                        player.sendMessage(ChatColor.GOLD + args[0] + " "
                                + ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(player, "player_not_online"));
                    }
                }
            }

        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
       final List<String> completion = new ArrayList<>();
       if(args.length == 1){
           if(sender.hasPermission("obelouix.enderchest.others")){
               for (Player p: Bukkit.getOnlinePlayers()) {
                   if(!p.getName().equals(sender.getName())){
                       completion.add(p.getName());
                   }
               }
           }
       }

        return completion;
    }
}

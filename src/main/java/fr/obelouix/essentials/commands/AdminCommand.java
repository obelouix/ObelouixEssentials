package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.gui.AdminGUI;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class AdminCommand extends BukkitCommand implements Listener {

    public AdminCommand(String name) {
        super(name);
    }


    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player && IPermission.test(player, "obelouix.commands.admin")) {
            new AdminGUI().showInventory(player);
        }
        return true;
    }
}

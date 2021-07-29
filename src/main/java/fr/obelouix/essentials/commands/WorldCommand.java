package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class WorldCommand implements TabExecutor {

    private final Essentials PluginInstance = Essentials.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.world")) {
            if (args[0].equalsIgnoreCase("create")) {
                WorldCreator worldCreator = new WorldCreator(args[1]);
                switch (args[2].toLowerCase(Locale.ROOT)) {
                    case "normal" -> worldCreator.environment(World.Environment.NORMAL);
                    case "nether" -> worldCreator.environment(World.Environment.NETHER);
                    case "the_end", "end" -> worldCreator.environment(World.Environment.THE_END);
                }
                switch (args[3].toLowerCase(Locale.ROOT)) {
                    case "flat" -> worldCreator.type(WorldType.FLAT);
                    case "normal" -> worldCreator.type(WorldType.NORMAL);
                    case "amplified" -> worldCreator.type(WorldType.AMPLIFIED);
                    case "large_biomes", "largebiomes", "large" -> worldCreator.type(WorldType.LARGE_BIOMES);
                }
                //allow to specify a seed
                if (args[4].equalsIgnoreCase("-s")) {
                    worldCreator.seed(Long.parseLong(args[5]));
                }

                worldCreator.createWorld();
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

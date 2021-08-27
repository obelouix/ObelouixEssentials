package fr.obelouix.essentials.commands;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DifficultyCommand extends BukkitCommand {

    private final ImmutableList<String> difficultyList = ImmutableList.of("peaceful", "easy", "normal", "hard");

    public DifficultyCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (IPermission.test(sender, "bukkit.command.difficulty")) {
            Component difficultyComponent = Component.text("");
            String worldName;

            if (args.length <= 1) {

                if (sender instanceof Player player) {
                    worldName = player.getWorld().getName();
                } else {
                    worldName = Bukkit.getWorlds().get(0).getName();
                }
                final World world = Bukkit.getWorld(worldName);
                if (args.length == 0) {
                    difficultyComponent = Component.translatable("commands.difficulty.query", TextColor.color(162, 162, 162));
                    difficultyComponent = setDifficultyComponent(difficultyComponent, world);
                } else {

                    boolean validArgument = false;

                    for (String argument : difficultyList) {
                        if (argument.equalsIgnoreCase(args[0])) {
                            validArgument = true;
                            break;
                        }
                    }
                    if (validArgument) {
                        switch (args[0].toLowerCase(Locale.ROOT)) {
                            case "peaceful" -> Objects.requireNonNull(world).setDifficulty(Difficulty.PEACEFUL);
                            case "easy" -> Objects.requireNonNull(world).setDifficulty(Difficulty.EASY);
                            case "normal" -> Objects.requireNonNull(world).setDifficulty(Difficulty.NORMAL);
                            case "hard" -> Objects.requireNonNull(world).setDifficulty(Difficulty.HARD);
                        }
                        difficultyComponent = Component.translatable("commands.difficulty.success", TextColor.color(162, 162, 162));
                        difficultyComponent = setDifficultyComponent(difficultyComponent, Objects.requireNonNull(world));
                    }
                }
                sender.sendMessage(difficultyComponent);
            }
        }
        return true;
    }

    private Component setDifficultyComponent(Component difficultyComponent, World world) {
        switch (world.getDifficulty()) {
            case PEACEFUL -> difficultyComponent = difficultyComponent.append(Component.translatable(world.getDifficulty().translationKey())
                    .color(TextColor.color(0, 200, 0)));
            case EASY -> difficultyComponent = difficultyComponent.append(Component.translatable(world.getDifficulty().translationKey())
                    .color(TextColor.color(0, 255, 0)));
            case NORMAL -> difficultyComponent = difficultyComponent.append(Component.translatable(world.getDifficulty().translationKey())
                    .color(TextColor.color(255, 143, 17)));
            case HARD -> difficultyComponent = difficultyComponent.append(Component.translatable(world.getDifficulty().translationKey())
                    .color(TextColor.color(255, 0, 0)));
        }
        return difficultyComponent;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (sender.hasPermission("bukkit.command.difficulty")) return difficultyList;
        return super.tabComplete(sender, alias, args);
    }
}

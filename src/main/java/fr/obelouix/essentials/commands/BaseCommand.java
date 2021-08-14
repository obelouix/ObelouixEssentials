package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand {

    protected final Essentials plugin;
    protected final CommandManager commandManager;

    protected BaseCommand(final @NotNull Essentials plugin, final @NotNull CommandManager commandManager) {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    public abstract void register();
}

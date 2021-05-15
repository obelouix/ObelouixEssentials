package fr.obelouix.essentials;

import fr.obelouix.essentials.commands.CommandRegistrar;
import fr.obelouix.essentials.event.EventRegistry;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * main class of the Plugin
 */
public final class Essentials extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("ObelouixEssentials");
    private static Essentials instance;
    public final String SERVER_VERSION = Bukkit.getVersion();

    @Override
    public void onEnable() {
        instance = this;
        LOGGER.info(I18n.getInstance().getMessage("plugin_startup"));
        this.saveDefaultConfig();
        CommandRegistrar.getInstance().init();
        EventRegistry.getInstance().init();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    public Logger getLOGGER() {
        return LOGGER;
    }

    /**
     * @return instance of {@link Essentials} class
     */
    public static Essentials getInstance() {
        return instance;
    }

}

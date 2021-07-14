package fr.obelouix.essentials;

import fr.obelouix.essentials.commands.CommandRegistrar;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.event.EventRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * main class of the Plugin
 */
public final class Essentials extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("ObelouixEssentials");
    private static Essentials instance;
    public final String SERVER_VERSION = Bukkit.getVersion();
    private final ObelouixEssentialsDB dbInstance = ObelouixEssentialsDB.getInstance();

    private boolean isReloading = false;

    /**
     * @return instance of {@link Essentials} class
     */
    public static Essentials getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        if (!Bukkit.getServer().getOnlineMode()) {
            LOGGER.severe("The server is in INSECURE MODE. Don't ask for support if you have any problem");
        }

        this.saveDefaultConfig();
        CommandRegistrar.getInstance().init();
        EventRegistry.getInstance().init();

        try {
            dbInstance.connect();
            dbInstance.close();
/*            final ItemPriceDatabase itemPriceDatabase = new ItemPriceDatabase();
            itemPriceDatabase.setup();*/
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (isReloading) {
            LOGGER.warning("Please restart if the plugin is broken after reloading");
        }

        instance = null;

        /*try {
            ObelouixEssentialsDB.getInstance().closeOnServerReload();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/
    }

    public Logger getLOGGER() {
        return LOGGER;
    }

    public boolean isReloading() {
        return isReloading;
    }

    public void setReloading(boolean reloading) {
        isReloading = reloading;
    }
}

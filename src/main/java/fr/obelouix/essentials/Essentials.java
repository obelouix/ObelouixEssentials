package fr.obelouix.essentials;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import fr.obelouix.essentials.commands.CommandRegistrar;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.event.EventRegistry;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    private boolean isProtocolLibLoaded;
    private ProtocolManager protocolManager;
    private RegisteredServiceProvider<LuckPerms> luckPermsProvider;
    private boolean isLuckPermsEnabled;
    private LuckPerms luckPermsAPI;

    /**
     * @return instance of {@link Essentials} class
     */
    public static Essentials getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        isProtocolLibLoaded = isDepEnabled("ProtocolLib");
        if (isProtocolLibLoaded) {
            protocolManager = ProtocolLibrary.getProtocolManager();
        }
        isLuckPermsEnabled = isDepEnabled("LuckPerms");
        if (isLuckPermsEnabled) {
            luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            getLOGGER().info("LuckPerms found...");
            luckPermsAPI = luckPermsProvider.getProvider();
        }

        this.saveDefaultConfig();
        CommandRegistrar.getInstance().init();
        EventRegistry.getInstance().init();

        try {
            dbInstance.connect();
            dbInstance.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

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
     * Check if a {@link JavaPlugin} is enabled
     *
     * @param plugin name of the plugin <b>(case sensitive)</b>
     * @return {@code true} by default , {@code false} if the target plugin is not enabled
     */
    private boolean isDepEnabled(String plugin) {
        if (!Bukkit.getPluginManager().isPluginEnabled(plugin)) {
            LOGGER.warning(plugin + " not detected. Some functionalities will not work.");
        }
        return true;
    }

    public boolean isProtocolLibLoaded() {
        return isProtocolLibLoaded;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public boolean isLuckPermsEnabled() {
        return isLuckPermsEnabled;
    }

    public LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }
}

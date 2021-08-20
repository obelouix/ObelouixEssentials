package fr.obelouix.essentials;

import co.aikar.timings.lib.MCTiming;
import co.aikar.timings.lib.TimingManager;
import fr.obelouix.essentials.commands.CommandManager;
import fr.obelouix.essentials.config.Config;
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
    private static TimingManager timingManager;
    public final String SERVER_VERSION = Bukkit.getVersion();
    private final ObelouixEssentialsDB dbInstance = ObelouixEssentialsDB.getInstance();
    private final boolean isCommodoreSupported = false;
    private boolean isReloading = false;
    private boolean isWorldGuardEnabled = false;
    private boolean isProtocolLibEnabled = false;
    private LuckPerms luckPermsAPI;

    /**
     * @return instance of {@link Essentials} class
     */
    public static Essentials getInstance() {
        return instance;
    }

    public static MCTiming timing(String name) {
        return timingManager.of(name);
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

    @Override
    public void onEnable() {

        if (!isClassFound("com.destroystokyo.paper.PaperConfig")) {
            LOGGER.severe(
                    """
                                                        
                            *********************************\s

                              This plugin require paper or
                              one of its fork (e.g. Tuinity)

                            *********************************""");

            getServer().getPluginManager().disablePlugin(this);

        } else {
            instance = this;
            timingManager = TimingManager.of(this);
//            this.saveDefaultConfig();

            if (isClassFound("com.comphenix.protocol.ProtocolLib")) {
                LOGGER.info("Found ProtocolLib");
                if (this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) isProtocolLibEnabled = true;
            }

            if (isClassFound("net.luckperms.api.LuckPerms")) {
                LOGGER.info("Found LuckPerms");
                final RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                if (luckPermsProvider != null) {
                    luckPermsAPI = luckPermsProvider.getProvider();
                }
            }

            if (isClassFound("com.sk89q.worldguard.bukkit.WorldGuardPlugin")) {
                LOGGER.info("Found WorldGuard");

                if (this.getServer().getPluginManager().isPluginEnabled("WorldGuard")) isWorldGuardEnabled = true;
            } else {
                if (Config.isLandProtectionModuleEnabled) {
                    LOGGER.warning("Land Protection Module is enabled but WorldGuard was not found. Disabling this module...");
                }
            }
            // We load the config here
            Config.load();

            try {
                new CommandManager(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    /**
     * check if the given class is present in the classpath
     */
    private boolean isClassFound(String classPath) {
        try {
            Class.forName(classPath);
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }

    public LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }

    public boolean isCommodoreSupported() {
        return isCommodoreSupported;
    }

    public boolean isWorldGuardEnabled() {
        return isWorldGuardEnabled;
    }

    public boolean isProtocolLibEnabled() {
        return isProtocolLibEnabled;
    }
}

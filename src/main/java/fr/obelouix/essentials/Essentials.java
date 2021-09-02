package fr.obelouix.essentials;

import co.aikar.timings.lib.MCTiming;
import co.aikar.timings.lib.TimingManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.fastasyncworldedit.core.Fawe;
import fr.obelouix.essentials.commands.CommandManager;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import fr.obelouix.essentials.event.EventRegistry;
import fr.obelouix.essentials.items.WorldEditWand;
import fr.obelouix.essentials.watchdog.Watchdog;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
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
    private final boolean isProtocolLibEnabled = false;
    private RegisteredServiceProvider<LuckPerms> luckPermsProvider;
    private Fawe fawe;
    private boolean isReloading = false;
    private boolean isWorldGuardEnabled = false;
    private ProtocolManager protocolManager;
    private boolean isFawePresent = false;
    private LuckPerms luckPermsAPI;

    /**
     * @return instance of {@link Essentials} class
     */
    public static Essentials getInstance() {
        return instance;
    }

    /**
     * @param name
     * @return
     */
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

            CompletableFuture.runAsync(() -> {
                Config.load();
                try {
                    new CommandManager(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EventRegistry.getInstance().init();
                Watchdog.getInstance().register();
            });

            setupProtocolManager();

            if (isClassFound("net.luckperms.api.LuckPerms")) {
                LOGGER.info("Found LuckPerms");
                final RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
                if (luckPermsProvider != null) {
                    this.luckPermsProvider = luckPermsProvider;
                    luckPermsAPI = luckPermsProvider.getProvider();
                }
            }

            checkWorldEditPresence();

            if (isClassFound("com.sk89q.worldguard.bukkit.WorldGuardPlugin")) {
                LOGGER.info("Found WorldGuard");

                if (this.getServer().getPluginManager().isPluginEnabled("WorldGuard")) isWorldGuardEnabled = true;
            } else {
                if (Config.isLandProtectionModuleEnabled) {
                    LOGGER.warning("Land Protection Module is enabled but WorldGuard was not found. Disabling this module...");
                }
            }


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

    /**
     * Allows to use the plugin logger anywhere
     *
     * @return an instance of the plugin logger
     */
    public Logger getLOGGER() {
        return LOGGER;
    }

    /**
     * Allows to know if the server is in a reloading state
     *
     * @return {@code true} if the server is reloading
     */
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

    private void setupProtocolManager() {
        if (isClassFound("com.comphenix.protocol.ProtocolLib")) {

            LOGGER.info("Found ProtocolLib");
            protocolManager = ProtocolLibrary.getProtocolManager();

        } else {
            LOGGER.severe("ProtocolLib is missing, please install it");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Check the Presence of {@link Fawe} plugin and setup an instance of it if its present <br>
     * <b>This doesn't check the presence of {@link com.sk89q.worldedit.WorldEdit} which is the base of {@link Fawe}</b>
     * <br>Forcing user to use {@link Fawe} and not the standard WorldEdit or other forks of it
     */
    private void checkWorldEditPresence() {

        if (isClassFound("com.fastasyncworldedit.core.Fawe")) {
            LOGGER.info("Found WorldEdit");
            this.fawe = Fawe.get();
            isFawePresent = true;
            new WorldEditWand().setupSelectionWand();
        }
    }

    /**
     * Allows to get an instance of {@link LuckPerms} API
     *
     * @return {@link LuckPerms}
     */
    public LuckPerms getLuckPermsAPI() {
        return luckPermsAPI;
    }

    public boolean isWorldGuardEnabled() {
        return isWorldGuardEnabled;
    }

    public boolean isProtocolLibEnabled() {
        return isProtocolLibEnabled;
    }

    public RegisteredServiceProvider<LuckPerms> getLuckPermsProvider() {
        return luckPermsProvider;
    }

    /**
     * Allow hooking in {@link Fawe} plugin
     *
     * @return an instance of {@link Fawe}
     */
    public Fawe getFAWEProvider() {
        return fawe;
    }

    /**
     * Tell other classes if {@link Fawe} is present on the server
     *
     * @return {@code true} if present else {@code false}
     */
    public boolean isFawePresent() {
        return isFawePresent;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
}

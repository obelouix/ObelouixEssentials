package fr.obelouix.essentials;

import cloud.commandframework.paper.PaperCommandManager;
import co.aikar.timings.lib.TimingManager;
import fr.obelouix.essentials.commands.CommandManager;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.database.ObelouixEssentialsDB;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * main class of the Plugin
 */
public final class Essentials extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("ObelouixEssentials");
    private static Essentials instance;
    private static TimingManager timingManager;
    private static PaperCommandManager<CommandSender> paperCommandManager;
    public final String SERVER_VERSION = Bukkit.getVersion();
    private final ObelouixEssentialsDB dbInstance = ObelouixEssentialsDB.getInstance();
    private final boolean isCommodoreSupported = false;
    private boolean isReloading = false;
    private boolean isWorldGuardEnabled = false;
    private boolean isProtocolLibEnabled = false;

    /**
     * @return instance of {@link Essentials} class
     */
    public static Essentials getInstance() {
        return instance;
    }

    public static PaperCommandManager<CommandSender> getPaperCommandManager() {
        return paperCommandManager;
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
        instance = this;
        timingManager = TimingManager.of(this);
        this.saveDefaultConfig();

        if (isClassFound("com.comphenix.protocol.ProtocolLib")) {
            LOGGER.info("Found ProtocolLib");
            if (this.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) isProtocolLibEnabled = true;
        }

        if (isClassFound("com.sk89q.worldguard.bukkit.WorldGuardPlugin")) {
            LOGGER.info("Found WorldGuard");

            if (this.getServer().getPluginManager().isPluginEnabled("WorldGuard")) isWorldGuardEnabled = true;
        } else {
            if (Config.isLandProtectionModuleEnabled) {
                LOGGER.warning("Land Protection Module is enabled but WorldGuard was not found. Disabling this module...");
                this.getConfig().set("enable-land-protection-module", false);
                try {
                    this.getConfig().save(this.getConfig().getCurrentPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            paperCommandManager = new CommandManager(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //EventRegistry.getInstance().init();

        try {
            dbInstance.connect();
            dbInstance.close();
/*            final ItemPriceDatabase itemPriceDatabase = new ItemPriceDatabase();
            itemPriceDatabase.setup();*/
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
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
     *
     * @param classPath
     * @return
     */
    private boolean isClassFound(String classPath) {
        try {
            if (Class.forName(classPath) != null) return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
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

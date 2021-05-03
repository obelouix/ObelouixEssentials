package fr.obelouix.essentials;

import fr.obelouix.essentials.commands.CommandRegistrar;
import fr.obelouix.essentials.event.EventRegistry;
import fr.obelouix.essentials.i18n.I18n;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Essentials extends JavaPlugin {

    private static final Logger LOGGER = Logger.getLogger("ObelouixEssentials");
    private static Essentials instance;
   //private final RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);

    @Override
    public void onEnable() {
        instance = this;
        LOGGER.info(I18n.getInstance().getMessage("plugin_startup"));
        this.saveDefaultConfig();
        CommandRegistrar.getInstance().init();
        EventRegistry.getInstance().init();

        /*if(luckPermsProvider != null){
            if(this.getConfig().getBoolean("auto-setup-admin-group")){
                new LuckPermsSetup().setup();
            }
        }*/

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    public Logger getLOGGER() {
        return LOGGER;
    }

    public static Essentials getInstance(){
        return instance;
    }

    /*public @NotNull LuckPerms getLuckPermsProvider() {
        return luckPermsProvider.getProvider();
    }*/
}

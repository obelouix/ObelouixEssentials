package fr.obelouix.essentials.watchdog;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.config.Config;
import fr.obelouix.essentials.event.EventRegistry;

public class Watchdog {

    private final Essentials plugin = Essentials.getInstance();
    private static Watchdog instance;

    public void register(){
        if(Config.isIsWatchdogEnabled()){
            plugin.getLOGGER().info("Enabling Watchdog...");
            EventRegistry.getInstance().registerEvent(new WatchdogFly());
            EventRegistry.getInstance().registerEvent(new WatchdogAntiSpam());
            plugin.getLOGGER().info("Watchdog successfully enabled");
        }
    }

    public static Watchdog getInstance() {
        if(instance == null){
            instance = new Watchdog();
        }
        return instance;
    }
}

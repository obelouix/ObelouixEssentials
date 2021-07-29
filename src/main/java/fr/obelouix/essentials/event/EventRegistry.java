package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.commands.FreezeCommand;
import fr.obelouix.essentials.commands.TPSBarCommand;
import fr.obelouix.essentials.commands.VanishCommand;
import fr.obelouix.essentials.data.PlayerData;
import fr.obelouix.essentials.features.NightSkip;
import fr.obelouix.essentials.security.ChatSecurity;
import org.bukkit.event.Listener;

public class EventRegistry {

    private static EventRegistry instance;

    private EventRegistry(){
    }

    public static EventRegistry getInstance(){
        if(instance == null){
            instance = new EventRegistry();
        }
        return instance;
    }

    public void init() {
        registerEvent(new BlockEvents());
        registerEvent(new CommandPreprocessorEvent());
        registerEvent(new PlayerJoin());
        registerEvent(new PlayerData());
        registerEvent(new FreezeCommand());
        if (Essentials.getInstance().getConfig().getBoolean("enable-night-skipping")) {
            registerEvent(new NightSkip());
        }
        registerEvent(new ChatSecurity());
        registerEvent(new MessageEvent());
        registerEvent(new VanishCommand());
        if (!Essentials.getInstance().SERVER_VERSION.contains("Purpur")) {
            registerEvent(new TPSBarCommand("tpsbar"));
        }
    }

    private void registerEvent(Listener listener){
        Essentials.getInstance().getServer().getPluginManager().registerEvents(listener, Essentials.getInstance());
    }

}

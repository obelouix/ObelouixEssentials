package fr.obelouix.essentials.event;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.commands.FreezeCommand;
import fr.obelouix.essentials.files.PlayerConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class FreezeOnJoin implements Listener {

/*    @EventHandler
    public void freezeOnJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(PlayerConfig.get().getBoolean("frozen")){
            Essentials.getInstance().getLOGGER().info("freezing " + player.getName());
            FreezeCommand.addFrozenPlayer(player);
            FreezeCommand.freezePlayer(player);
        }
    }*/

}

package fr.obelouix.essentials.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.files.PlayerConfig;
import fr.obelouix.essentials.permissions.IPermission;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VanishCommand implements CommandExecutor, TabCompleter, Listener {

    private final Essentials pluginInstance = Essentials.getInstance();
    private boolean isVanished = false;
    private Player player;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (IPermission.test(sender, "obelouix.vanish") && sender instanceof Player) {
            PlayerConfig.load(Objects.requireNonNull(((Player) sender).getPlayer()));
            player = (Player) sender;
            for (Player otherPlayers : Bukkit.getOnlinePlayers()) {
                if (!PlayerConfig.get().getBoolean("vanished")) {
                    if (!isVanished) isVanished = true;
                } else {
                    if (isVanished) isVanished = false;
                }
                if (!otherPlayers.hasPermission("obelouix.vanish.seevanished")) {
                    otherPlayers.hidePlayer(Essentials.getInstance(), (Player) sender);
                }
            }
        }
        return true;
    }

    @EventHandler
    private void modifyServerList(ServerListPingEvent event) {
        if (pluginInstance.isProtocolLibLoaded() && isVanished) {
            PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            packetContainer.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
            PlayerInfoData playerInfoData = new PlayerInfoData(WrappedGameProfile.fromPlayer(player), 0,
                    EnumWrappers.NativeGameMode.NOT_SET, null);
            packetContainer.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
            try {
                for (Player others : Bukkit.getOnlinePlayers()) {
                    if (IPermission.test(others, "obelouix.vanish.seevanished")) {
                        pluginInstance.getProtocolManager().sendServerPacket(others, packetContainer);
                    }
                }
            } catch (InvocationTargetException e) {
                pluginInstance.getLOGGER().severe("Failed to send tab list remove packet!" + e);
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}

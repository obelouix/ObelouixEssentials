package fr.obelouix.essentials.components;

import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerComponent {

    public Component player(Player player, Player target) {
        Component component = Component.text(target.getName());

        if (IPermission.simpleTest(player, "obelouix.admin.playerdetails")) {
            final Component IPComponent = Component.text(
                            "IP: ")
                    .color(TextColor.color(255, 186, 6))
                    .append(Component.text(Objects.requireNonNull(target.getAddress()).getHostName() + "\n")
                            .color(TextColor.color(255, 255, 255)));
            System.out.println(target.getAddress().getHostName());

            final Component WorldComponent = Component.text("World: ")
                    .color(TextColor.color(255, 186, 6))
                    .append(Component.text(target.getWorld().getName() + "\n")
                            .color(TextColor.color(255, 255, 255)));

            final Component location = Component.text("Location: ")
                    .color(TextColor.color(255, 186, 6))
                    .append(Component.text(target.getLocation().getBlockX() + " "
                                    + target.getLocation().getBlockY() + " "
                                    + target.getLocation().getBlockZ())
                            .color(TextColor.color(255, 255, 255)));

            component = component.hoverEvent(HoverEvent.showText(IPComponent
                    .append(WorldComponent)
                    .append(location)));
        }
        return component;
    }

}

package fr.obelouix.essentials.components;

import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerComponent {

    private final I18n i18n = I18n.getInstance();

    /**
     * This will show critical information about the targeted player
     * when hovering his name on the chat if the receiver has the
     * permission to see them
     *
     * @param player the {@link Player} that will receive the message (needed to check his permissions)
     * @param target the {@link Player} to modify
     * @return {@link Component}
     */
    public Component player(Player player, Player target) {
        Component component = Component.text(target.getName());

        if (IPermission.simpleTest(player, "obelouix.admin.playerdetails")) {
            final Component IPComponent = Component.text(
                            "IP: ")
                    .color(TextColor.color(255, 186, 6))
                    .append(Component.text(Objects.requireNonNull(target.getAddress()).getHostName() + "\n")
                            .color(TextColor.color(255, 255, 255)));

            final Component WorldComponent = Component.translatable("selectWorld.world")
                    .color(TextColor.color(255, 186, 6))
                    .append(Component.text(": "))
                    .append(Component.text(target.getWorld().getName() + "\n")
                            .color(TextColor.color(255, 255, 255)));

            Component location = Component.text(i18n.sendTranslatedMessage(player, "obelouix.location") + ": ")
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

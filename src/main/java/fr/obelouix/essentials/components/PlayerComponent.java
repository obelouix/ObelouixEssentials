package fr.obelouix.essentials.components;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import fr.obelouix.essentials.utils.IPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerComponent {

    private final Essentials plugin = Essentials.getInstance();
    private final I18n i18n = I18n.getInstance();

    /**
     * This will show critical information about the targeted player
     * when hovering his name on the chat if the receiver has the
     * permission to see them
     *
     * @param sender the {@link CommandSender} that will receive the message (needed to check his permissions)
     * @param target the {@link Player} to modify
     * @return {@link Component}
     */
    public Component player(CommandSender sender, Player target) {
        Component component = Component.text(target.getName());
        Component group;
        HoverEvent<Component> hoverEvent = HoverEvent.showText(Component.text(""));

        if (IPermission.simpleTest(sender, "obelouix.admin.playerdetails")) {
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

            final Component location = Component.text(i18n.sendTranslatedMessage(sender, "obelouix.location") + ": ")
                    .color(TextColor.color(255, 186, 6))
                    .append(Component.text(target.getLocation().getBlockX() + " "
                                    + target.getLocation().getBlockY() + " "
                                    + target.getLocation().getBlockZ())
                            .color(TextColor.color(255, 255, 255)));

            hoverEvent = Objects.requireNonNull(hoverEvent).asHoverEvent(component1 ->
                    component1.append(IPComponent)
                            .append(WorldComponent)
                            .append(location));

            if (plugin.getLuckPermsAPI() != null) {
                group = Component.text("\n" + StringUtils.capitalize(i18n.sendTranslatedMessage(sender, "obelouix.group")) + ": ")
                        .color(TextColor.color(255, 186, 6))
                        .append(Component.text(IPlayer.getGroup(target))
                                .color(TextColor.color(255, 255, 255)));

                hoverEvent = hoverEvent.asHoverEvent(component1 ->
                        component1.append(group));
            }

            component = component.hoverEvent(hoverEvent);

        }

        return component;
    }

}

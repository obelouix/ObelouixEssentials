package fr.obelouix.essentials.gui;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class PlayerManagementBook extends BaseBookGUI {

    private String managedPlayerName;

    @Override
    public Book book(Player player) {
        final Component title = Component.text(managedPlayerName);
        final Component author = Component.text("Server");
        return Book.book(title, author, bookPages(player));
    }

    /**
     * Collections of book pages
     *
     * @return the {@link Collection} of pages
     */
    @Override
    protected Collection<Component> bookPages(Player player) {
        final String indexPageTitle = i18n.getTranslation(player, "obelouix.book.management");
        final String punishmentTitle = i18n.getTranslation(player, "obelouix.punishment");
        //first page of the book
        final Component indexPage = Component.text(indexPageTitle + " " + managedPlayerName + "\n\n")
                .decoration(TextDecoration.UNDERLINED, true)
                .color(TextColor.color(35, 219, 240))
                .append(Component.text("> " + i18n.getTranslation(player, "obelouix.inventory") + "\n")
                        .color(TextColor.color(0, 0, 0))
                        .clickEvent(openTargetInventory()))
                .append(Component.text("\n> ").append(Component.translatable("container.enderchest"))
                        .color(TextColor.color(0, 0, 0))
                        .clickEvent(openTargetEnderChest()))
                .append(Component.text("\n\n> " + punishmentTitle)
                        .color(TextColor.color(0, 0, 0))
                        .clickEvent(ClickEvent.changePage(2)));

        final Component punishmentPage = Component.text(StringUtils.capitalize(punishmentTitle))
                .color(TextColor.color(255, 0, 0))
                .decoration(TextDecoration.BOLD, true);

        return List.of(indexPage, punishmentPage);
    }

    private ClickEvent openTargetEnderChest() {
        return ClickEvent.runCommand("/enderchest " + managedPlayerName);
    }

    private ClickEvent openTargetInventory() {
        return ClickEvent.runCommand("/openinv " + managedPlayerName);
    }

    /**
     * Get the Managed Player
     *
     * @return the managed player
     */
    public String getManagedPlayerName() {
        return managedPlayerName;
    }

    public void setManagedPlayerName(String managedPlayerName) {
        this.managedPlayerName = managedPlayerName;
    }
}

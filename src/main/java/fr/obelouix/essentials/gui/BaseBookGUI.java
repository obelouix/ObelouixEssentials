package fr.obelouix.essentials.gui;

import fr.obelouix.essentials.i18n.I18n;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Collection;

public abstract class BaseBookGUI {

    protected final I18n i18n = I18n.getInstance();

    public abstract Book book(Player player);

    protected abstract Collection<Component> bookPages(Player player);
}

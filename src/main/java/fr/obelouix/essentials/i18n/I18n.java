package fr.obelouix.essentials.i18n;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.files.PlayerConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class I18n {

    private static I18n instance;
    private final String language = Locale.getDefault().getLanguage();
    private final String country = Locale.getDefault().getCountry().toUpperCase(Locale.ROOT);
    private final ResourceBundle messages;
    private ResourceBundle playerMessages;

    private I18n() {
        if (ResourceBundle.getBundle("Messages_" + language + country) == null) {
            Essentials.getInstance().getLOGGER().warning("No translation file found for locale " + language + country
                    + ". Using enUS by default");
            messages = ResourceBundle.getBundle("Messages_enUS");
        } else {
            messages = ResourceBundle.getBundle("Messages_" + language + country);
        }
    }

    public static I18n getInstance(){
        if(instance == null){
              instance = new I18n();
        }
        return instance;
    }

    /**
     * Method for sending a message based on player's Locale
     *
     * @param commandSender the player that will receive the message
     * @param message       a {@link String} ID of the message
     * @return the @param message
     */
    public String sendTranslatedMessage(CommandSender commandSender, String message) {
        if (commandSender instanceof Player) {
            PlayerConfig.load((Player) commandSender);
            final String playerLocale = Objects.requireNonNull(PlayerConfig.get().getString("locale")).replace("_", "");
            if (ResourceBundle.getBundle("Messages_" + playerLocale) == null) {
                //if the plugin doesn't have translations for the player's locale use the default one
                playerMessages = ResourceBundle.getBundle("Messages_enUS");

            } else {
                playerMessages = ResourceBundle.getBundle("Messages_" + playerLocale);
            }
        } else {
            playerMessages = ResourceBundle.getBundle("Messages_enUS");
        }

        return playerMessages.getString(message);
    }
}

package fr.obelouix.essentials.i18n;

import fr.obelouix.essentials.Essentials;
import org.bukkit.entity.Player;

import java.util.Locale;
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

    //Method for sending a message based on player's Locale
    private String translateToPlayerLocale(Player player, String message){
        final String playerLanguage = player.locale().getLanguage();
        final String playerCountry = player.locale().getCountry().toUpperCase();
        if(ResourceBundle.getBundle("Messages_" + playerLanguage + playerCountry) == null){
            playerMessages = ResourceBundle.getBundle("Messages_" + playerLanguage + playerCountry);
        }
        else {
            //if the plugin doesn't have translations for the player's locale use the default one
            playerMessages = ResourceBundle.getBundle("Messages_enUS");
        }
        return playerMessages.getString(message);
    }

    public String getMessage(String message){
        return  messages.getString(message);
    }

}

package fr.obelouix.essentials.i18n;

import com.google.common.net.UrlEscapers;
import fr.obelouix.essentials.Essentials;
import org.bukkit.entity.Player;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Method for sending a message based on player's Locale
     * @param player the player that will receive the message
     * @param message a {@link String} ID of the message
     * @return the @param message
     */
    public String translateToPlayerLocale(Player player, String message){
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
        return messages.getString(message);
    }

    private File[] getResourceFolderFiles () {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("/");
        String path = url.getPath();
        return new File(path).listFiles();
    }

    public List<String> setI18NAliases(String path) {
        List<String> alias = new ArrayList<>();

        if(path.equals("command.help.alias")) alias.add("?");

        for (File file : I18n.getInstance().getResourceFolderFiles()) {
            if (file.getName().startsWith("Messages_") && !file.getName().startsWith("Messages_en")) {

                String messageFile = file.getName().replaceFirst("[.][^.]+$", "");
                ResourceBundle resourceBundle = ResourceBundle.getBundle(messageFile);
                alias.add(resourceBundle.getString(path));

            }
        }
        return alias;
    }

}

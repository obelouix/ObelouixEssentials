package fr.obelouix.essentials.i18n;

import fr.obelouix.essentials.Essentials;

import java.util.Locale;
import java.util.ResourceBundle;

public class I18n {

    private static I18n instance;
    private final String language = Locale.getDefault().getLanguage();
    private final String country = Locale.getDefault().getCountry().toUpperCase(Locale.ROOT);
    private final ResourceBundle messages;

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

    public String getMessage(String message){
        return  messages.getString(message);
    }

}

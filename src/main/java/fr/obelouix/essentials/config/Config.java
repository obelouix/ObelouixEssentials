package fr.obelouix.essentials.config;

import fr.obelouix.essentials.Essentials;

/**
 * This class serve only to get data from the configuration file
 */
public class Config {

    /**
     * Get the status of the economy system (if it is enabled or not)
     */
    public static final boolean isEconomyEnabled = Essentials.getInstance().getConfig().getBoolean("economy.enabled");

}

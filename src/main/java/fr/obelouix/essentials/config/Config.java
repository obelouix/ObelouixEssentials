package fr.obelouix.essentials.config;

import fr.obelouix.essentials.Essentials;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * This class serve only to get data from the configuration file
 */
public class Config {

    private static final @NotNull FileConfiguration pluginConfig = Essentials.getInstance().getConfig();

    /**
     * Get the status of the economy system (if it is enabled or not)
     */
    public static final boolean isEconomyEnabled = pluginConfig.getBoolean("economy.enabled");
    /**
     * Enabling this will allow players to have e.g. <b> -1000 $</b> on their account
     */
    public static final boolean allowNegativeBalance = pluginConfig.getBoolean("economy.allow-negative-balance");
    /**
     * Represent the symbol of the economy e.g. $ or €
     * this can be longer than 1 character
     */
    public static final String economySymbol = pluginConfig.getString("economy.symbol");

    public static final boolean allowHTTPURL = pluginConfig.getBoolean("allow-insecure-url");

}

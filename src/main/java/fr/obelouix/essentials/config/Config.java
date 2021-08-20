package fr.obelouix.essentials.config;

import com.google.common.collect.ImmutableList;
import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.commands.CommandManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.nio.file.Path;

/**
 * This class serve only to get data from the configuration file
 */
public class Config {

    private static final Essentials plugin = Essentials.getInstance();
    private static final ImmutableList<String> commandList = ImmutableList.of("day");

    private static final YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
            .path(Path.of(Essentials.getInstance().getDataFolder().getPath(), "config.yml"))
            .build();
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
    /**
     * Enabling this allow players to send any URLs that are not <b>HTTPS</b>
     */
    public static final boolean allowHTTPURL = pluginConfig.getBoolean("allow-insecure-url");
    /**
     * Threshold for kicking the player
     * Eg: if someone spam 3 times the same message, he will be kicked
     */
    public static final int spamThreshold = pluginConfig.getInt("spam-kick-threshold");
    public static final boolean isLandProtectionModuleEnabled = pluginConfig.getBoolean("enable-land-protection-module");
    public static int requiredSleepingPlayerPercentage = Essentials.getInstance().getConfig().getInt("player-sleep-percentage");
    public static ConfigurationNode debugNode;
    private static CommentedConfigurationNode root;

    public static void load() {
        try {
            root = configLoader.load();
            createFile();

            for (Object command : root.node("commands").childrenMap().keySet()) {
                for (CommentedConfigurationNode booleanValue : root.node("commands").childrenMap().values()) {
                    CommandManager.getCommandStates().put(command.toString(), booleanValue.getBoolean());
                }
            }
            plugin.getLOGGER().info(String.valueOf(CommandManager.getCommandStates()));
            if (!plugin.isReloading()) Essentials.getInstance().getLOGGER().info("Configuration loaded");

        } catch (ConfigurateException e) {
            plugin.getLOGGER().severe("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }
    }

    private static void createFile() throws ConfigurateException {
        final File file = Path.of(Essentials.getInstance().getDataFolder().getPath(), "config.yml").toFile();

        if (!file.exists()) {

            debugNode = root.node("debug").set(false);

            root.node("commands").act(n -> {
                n.commentIfAbsent("Allow you to control which commands you want on your server");
                for (String command : commandList) {
                    n.node(command).raw(true);
                }

            });
            configLoader.save(root);
        }
    }

    public static @NotNull ImmutableList<String> getCommandList() {
        return commandList;
    }

    public static CommentedConfigurationNode getRoot() {
        return root;
    }
}

package fr.obelouix.essentials.files;

import fr.obelouix.essentials.Essentials;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class PlayerConfig {

    private static final File dataFolder = Essentials.getInstance().getDataFolder();
    private static final File userDataFolder = new File(dataFolder.getPath() + File.separator + "userdata");
    private static File configFile;
    private static FileConfiguration config;

    public static void create(Player p) {
        configFile = new File(userDataFolder + File.separator + p.getName() + ".yml");
        if (!userDataFolder.exists()) userDataFolder.mkdir();
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating " + configFile.getName() + "!");
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
            }
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static File getfolder() {
        return userDataFolder;
    }

    public static File getfile() {
        return configFile;
    }

    public static void load(Player p) {
        configFile = new File(dataFolder, "userdata" + File.separator + p.getName() + ".yml");
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void save() {
        try {
            config.save(configFile);
        } catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error saving " + configFile.getName() + "!");
        }
    }

}


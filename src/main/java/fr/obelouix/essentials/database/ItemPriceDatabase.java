package fr.obelouix.essentials.database;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.sql.SQLException;
import java.util.Locale;

public class ItemPriceDatabase {

    private final ObelouixEssentialsDB dbInstance = ObelouixEssentialsDB.getInstance();
    private BlockData blockData;

    public void setup() {

        for (Material material : Material.values()) {
            if (!material.isAir() || !material.isEmpty()) {
                try {
                    if (material.isBlock()) {
                        dbInstance.executeQuery("INSERT INTO item_price(item, value)"
                                + "SELECT'" + material.name().toLowerCase(Locale.ROOT) + "','"
                                + String.format("%.2f", material.getHardness() * material.getBlastResistance() / 2) + "'"
                                + "WHERE NOT EXISTS(SELECT item,value from item_price "
                                + "WHERE item='" + material.name() + "');");
                    } else {
                        dbInstance.executeQuery("INSERT INTO item_price(item, value)"
                                + "SELECT'" + material.name().toLowerCase(Locale.ROOT) + "','"
                                + String.format("%.2f", material.getMaxDurability() * material.getMaxStackSize() / 2.5) + "'"
                                + "WHERE NOT EXISTS(SELECT item,value from item_price "
                                + "WHERE item='" + material.name() + "');");
                    }

                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

}

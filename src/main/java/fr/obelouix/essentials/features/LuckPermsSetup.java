package fr.obelouix.essentials.features;

import fr.obelouix.essentials.Essentials;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.node.Node;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;

import java.util.Objects;

public class LuckPermsSetup {

    private final Essentials instance = Essentials.getInstance();
    private final FileConfiguration configuration = instance.getConfig();

    /*public void setup() {
        if (configuration.getBoolean("auto-setup-admin-group")) {
            Group group = instance.getLuckPermsProvider().getGroupManager().getGroup(Objects.requireNonNull(configuration.getString("admin-group")));
            if (group != null) {
                for (Permission permission : instance.getDescription().getPermissions()) {
                    if(group.getCachedData().getPermissionData().checkPermission(permission.getName()) == null){
                        group.data().add(Node.builder(permission.getName()).build());
                    }
                }
            }
        }
    }*/


}

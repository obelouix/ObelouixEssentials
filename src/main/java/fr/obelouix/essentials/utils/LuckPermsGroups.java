package fr.obelouix.essentials.utils;

import fr.obelouix.essentials.Essentials;
import net.luckperms.api.model.group.Group;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public class LuckPermsGroups {

    private static final Essentials plugin = Essentials.getInstance();

    public static @NonNull @Unmodifiable Set<Group> getGroups() {
        return plugin.getLuckPermsAPI().getGroupManager().getLoadedGroups();
    }

}

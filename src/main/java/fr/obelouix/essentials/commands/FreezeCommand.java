package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.files.PlayerConfig;
import fr.obelouix.essentials.i18n.I18n;
import fr.obelouix.essentials.permissions.IPermission;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FreezeCommand implements CommandExecutor, TabCompleter, Listener {

    //ArrayList to store every frozen player
    private static final ArrayList<String> frozenPlayers = new ArrayList<>();

    //May be used somewhere else
    public static void addFrozenPlayer(Player player) {
        frozenPlayers.add(player.getName());
    }

    public static ArrayList<String> getFrozenPlayers() {
        return frozenPlayers;
    }

    public static void freezePlayer(Player player) {
        //remove all potion effects the player have
        for (final PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        //add infinity slowness, negative jump boost and blindness to player
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 100, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -100, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999999, 100, false, false, false));
        //set the player invulnerable so he can't be killed by other player and monsters or die from starvation
        player.setInvulnerable(true);
        //Change player gamemode to ADVENTURE so he can't edit the terrain
        player.setGameMode(GameMode.ADVENTURE);
        //If player is in creative mode or is allowed to fly, deny him to fly
        if (player.getGameMode() == GameMode.CREATIVE || player.getAllowFlight()) {
            player.setAllowFlight(false);
        }
    }

    /**
     * Block user from editing his inventory when he's frozen
     */
    @EventHandler
    public void lockInventory(InventoryClickEvent event) {
        final ItemStack itemStackClicked = event.getCurrentItem();
        final Player player = (Player) event.getWhoClicked();
        if (frozenPlayers.size() > 0 && player.getName().equalsIgnoreCase(frozenPlayers.get(frozenPlayers.indexOf(player.getName())))
                && (itemStackClicked != null)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(player, "state.frozen.deny.moveitem"));
        }
    }

    /**
     * If player was frozen before quitting , freeze it again when he join
     */
    @EventHandler
    public void freezeOnJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (PlayerConfig.get().getBoolean("frozen")) {
            Essentials.getInstance().getLOGGER().info("freezing " + player.getName());
            addFrozenPlayer(player);
            freezePlayer(player);
        }
    }

    /**
     * Block user from teleporting with ender pearls or chorus fruits when he's frozen
     */
/*    @EventHandler
    public void disableTeleportation(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final TextComponent baseComponent = Component.text(I18n.getInstance().sendTranslatedMessage(player, "command.freeze.deny.use"))
                .color(TextColor.color(0xF80400));
        final Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(5000), Ticks.duration(20));

        if (player.getName().equalsIgnoreCase(frozenPlayers.get(frozenPlayers.indexOf(player.getName())))) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) || event.getCause().equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
                event.setCancelled(true);
            }
            final Title title = Title.title(Component.empty(), baseComponent, times);
            //show the warning in Minecraft's title space instead of chat
            player.showTitle(title);
        }
    }*/

    @EventHandler
    public void disableMovement(PlayerTeleportEvent event){
        final Player player = event.getPlayer();
        final Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(5000), Ticks.duration(20));
        Component activeItem = null;

        if(frozenPlayers.size() > 0 && frozenPlayers.get(frozenPlayers.indexOf(player.getName())) != null){
            event.setTo(event.getFrom());

            if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
                activeItem =  Component.translatable("item.minecraft.ender_pearl").color(TextColor.color(255, 90, 93));
            }
            else {
                activeItem = Component.translatable(Objects.requireNonNull(player.getActiveItem()).getTranslationKey())
                        .color(TextColor.color(255, 90, 93));
            }

            final TextComponent titleMessage = Component.text(
                    I18n.getInstance().sendTranslatedMessage(player, "command.freeze.deny.use"))
                    .color(TextColor.color(0xF80400))
                    .append(Component.text(" (")).color(activeItem.color())
                    .append(activeItem)
                    .append(Component.text(")")).color(activeItem.color());

            final Title title = Title.title(Component.empty(), titleMessage, times);
            //show the warning in Minecraft's title space instead of chat
            player.showTitle(title);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (IPermission.test(sender, "obelouix.commands.freeze")) {
            if (args.length == 1) {
                final Player target = Essentials.getInstance().getServer().getPlayer(args[0]);

                if (target != null && !args[0].equalsIgnoreCase("list")) {
                    //if player is not immune to freeze, execute this
                    if (!target.hasPermission("obelouix.freeze.exempt")) {
                        PlayerConfig.load(target);
                        //unfroze part
                        if (frozenPlayers.contains(target.getName())) {

                            PlayerConfig.get().set("frozen", false);
                            //restore his previous gamemode
                            switch (Objects.requireNonNull(PlayerConfig.get().getString("gamemode_before_frozen"))) {
                                case "ADVENTURE" -> target.setGameMode(GameMode.ADVENTURE);
                                case "CREATIVE" -> target.setGameMode(GameMode.CREATIVE);
                                case "SPECTATOR" -> target.setGameMode(GameMode.SPECTATOR);
                                default -> target.setGameMode(GameMode.SURVIVAL);
                            }
                            target.setInvulnerable(false);
                            PlayerConfig.get().set("gamemode_before_frozen", null);
                            PlayerConfig.save();
                            frozenPlayers.remove(target.getName());

                            for (final PotionEffect potionEffect : target.getActivePotionEffects()) {
                                target.removePotionEffect(potionEffect.getType());
                            }
                            if (sender instanceof Player) {
                                sender.sendMessage(MessageFormat.format(I18n.getInstance().sendTranslatedMessage(sender, "command.freeze.unfreeze.success"),
                                        ChatColor.AQUA + target.getName() + ChatColor.GREEN));
                            }
                            target.sendMessage(ChatColor.GRAY + I18n.getInstance().sendTranslatedMessage(target, "command.freeze.unfreeze.inform"));
                        }
                        //freeze part
                        else {
                            PlayerConfig.get().set("frozen", true);
                            //save the player gamemode before changing it
                            PlayerConfig.get().set("gamemode_before_frozen", target.getGameMode().toString());
                            PlayerConfig.save();
                            frozenPlayers.add(target.getName());
                            freezePlayer(target);
                            sender.sendMessage(MessageFormat.format(I18n.getInstance().sendTranslatedMessage(sender, "command.freeze.success"),
                                    ChatColor.AQUA + target.getName() + ChatColor.GREEN));
                            target.sendMessage(ChatColor.GRAY + I18n.getInstance().sendTranslatedMessage(target, "command.freeze.inform"));
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().sendTranslatedMessage(sender, "command.freeze.exempt"));
                    }

                } //show every frozen player in a list
                else if (target == null && args[0].equalsIgnoreCase("list")) {
                    if (frozenPlayers.size() > 0) {
                        final StringBuilder msg = new StringBuilder(ChatColor.GOLD + I18n.getInstance().sendTranslatedMessage(sender, "command.freeze.list") + ": ");
                        for (final String player : frozenPlayers) {
                            msg.append(ChatColor.DARK_RED).append(player);
                            if (!Objects.equals(player, frozenPlayers.get(frozenPlayers.lastIndexOf(player)))) {
                                msg.append(ChatColor.GOLD + ",");
                            }
                        }
                        sender.sendMessage(String.valueOf(msg));
                    } else {
                        sender.sendMessage(ChatColor.GREEN + I18n.getInstance().sendTranslatedMessage(sender, "command.freeze.list.empty"));
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        final List<String> completion = new ArrayList<>();
        completion.add("list");

        if (sender.hasPermission("obelouix.commands.freeze")) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                //only add players who can be frozen in the completion list
                if (!player.hasPermission("obelouix.freeze.exempt")) completion.add(player.getName());
            }
        }
        return completion;
    }

}

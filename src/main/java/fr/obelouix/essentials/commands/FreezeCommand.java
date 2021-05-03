package fr.obelouix.essentials.commands;

import fr.obelouix.essentials.Essentials;
import fr.obelouix.essentials.files.PlayerConfig;
import fr.obelouix.essentials.i18n.I18n;
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

    private static ArrayList<String> frozenPlayers = new ArrayList<>();

    public static void addFrozenPlayer(Player player) {
        frozenPlayers.add(player.getName());
    }

    public static void freezePlayer(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(potionEffect.getType());
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 9999999, 100, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 9999999, -100, false, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 9999999, 100, false, false, false));
        player.setInvulnerable(true);
        if (player.getGameMode() == GameMode.CREATIVE || player.getAllowFlight()) {
            player.setAllowFlight(false);
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void lockInventory(InventoryClickEvent event) {
        ItemStack itemStackClicked = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        if (player.getName().equalsIgnoreCase(frozenPlayers.get(frozenPlayers.indexOf(player.getName())))) {
            if (itemStackClicked != null) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getMessage("state.frozen.deny.moveitem"));
            }
        }
    }

    @EventHandler
    public void freezeOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (PlayerConfig.get().getBoolean("frozen")) {
            Essentials.getInstance().getLOGGER().info("freezing " + player.getName());
            addFrozenPlayer(player);
            freezePlayer(player);
        }
    }

    @EventHandler
    public void disableTeleportation(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        final TextComponent baseComponent = Component.text(I18n.getInstance().getMessage("command.freeze.deny.use"))
                .color(TextColor.color(0xF80400));
        final Title.Times times = Title.Times.of(Ticks.duration(15), Duration.ofMillis(5000), Ticks.duration(20));

        if (player.getName().equalsIgnoreCase(frozenPlayers.get(frozenPlayers.indexOf(player.getName())))) {
            if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL) || event.getCause().equals(PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
                event.setCancelled(true);
            }
            final Title title = Title.title(Component.empty(), baseComponent, times);
            player.showTitle(title);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("obelouix.freeze"))
            if (args.length == 1) {
                Player target = Essentials.getInstance().getServer().getPlayer(args[0]);

                if (target != null && !args[0].equalsIgnoreCase("list")) {
                    if (!target.hasPermission("obelouix.freeze.exempt")) {
                        PlayerConfig.load(target);
                        //unfroze part
                        if (frozenPlayers.contains(target.getName())) {

                            PlayerConfig.get().set("frozen", false);

                            switch (Objects.requireNonNull(PlayerConfig.get().getString("gamemode_before_frozen"))) {
                                case "ADVENTURE":
                                    target.setGameMode(GameMode.ADVENTURE);
                                    break;
                                case "CREATIVE":
                                    target.setGameMode(GameMode.CREATIVE);
                                    break;
                                case "SPECTATOR":
                                    target.setGameMode(GameMode.SPECTATOR);
                                    break;
                                case "SURVIVAL":
                                    target.setGameMode(GameMode.SURVIVAL);
                                    break;
                            }

                            PlayerConfig.get().set("gamemode_before_frozen", null);
                            PlayerConfig.save();
                            frozenPlayers.remove(target.getName());
                            for (PotionEffect potionEffect : target.getActivePotionEffects()) {
                                target.removePotionEffect(potionEffect.getType());
                            }
                            sender.sendMessage(MessageFormat.format(I18n.getInstance().getMessage("command.freeze.unfreeze.success"),
                                    ChatColor.AQUA + target.getName() + ChatColor.GREEN));
                            target.sendMessage(ChatColor.GRAY + I18n.getInstance().getMessage("command.freeze.unfreeze.inform"));
                        }
                        //freeze part
                        else {
                            PlayerConfig.get().set("frozen", true);
                            PlayerConfig.get().set("gamemode_before_frozen", target.getGameMode().toString());
                            PlayerConfig.save();
                            frozenPlayers.add(target.getName());
                            freezePlayer(target);
                            sender.sendMessage(MessageFormat.format(I18n.getInstance().getMessage("command.freeze.success"),
                                    ChatColor.AQUA + target.getName() + ChatColor.GREEN));
                            target.sendMessage(ChatColor.GRAY + I18n.getInstance().getMessage("command.freeze.inform"));
                        }
                    }
                    else{
                        sender.sendMessage(ChatColor.DARK_RED + I18n.getInstance().getMessage("command.freeze.exempt"));
                    }

                } else if (target == null && args[0].equalsIgnoreCase("list")) {
                    if (frozenPlayers.size() > 0) {
                        StringBuilder msg = new StringBuilder(ChatColor.GOLD + I18n.getInstance().getMessage("command.freeze.list") + ": ");
                        for (String player : frozenPlayers) {
                            msg.append(ChatColor.DARK_RED).append(player);
                            if (!Objects.equals(player, frozenPlayers.get(frozenPlayers.lastIndexOf(player)))) {
                                msg.append(ChatColor.GOLD + ",");
                            }
                        }
                        sender.sendMessage(String.valueOf(msg));
                    } else {
                        sender.sendMessage(ChatColor.GREEN + I18n.getInstance().getMessage("command.freeze.list.empty"));
                    }
                }
            }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completion = new ArrayList<>();
        completion.add("list");
        if (sender.hasPermission("obelouix.freeze")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completion.add(p.getName());
            }
        }
        return completion;
    }

}

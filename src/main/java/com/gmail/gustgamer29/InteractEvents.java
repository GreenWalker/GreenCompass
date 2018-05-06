package com.gmail.gustgamer29;

import com.gmail.gustgamer29.expiring.ExpiringSet;
import com.gmail.gustgamer29.reflection.ReflectionUtils;
import com.google.common.util.concurrent.AtomicDouble;
import com.pgcraft.spectatorplus.SpectateAPI;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InteractEvents implements Listener, CommandExecutor {

    private MainClass mainClass;

    private ItemStack item;

    private ReflectionUtils utils;

    private ClanManager clanManager;

    private ExpiringSet<Player> delay;

    @Inject
    InteractEvents(MainClass mainClass, ReflectionUtils utils, ClanManager clanManager) {
        this.mainClass = mainClass;
        this.utils = utils;
        this.clanManager = clanManager;
        this.delay = new ExpiringSet<>(mainClass.getConfig().getInt("delay"), TimeUnit.SECONDS);

        try {
            Material mat = Material.valueOf(mainClass.getConfig().getString("item"));
            item = new ItemStack(mat);
        } catch (Exception e) {
            mainClass.info(new StringBuilder("&aUm erro ocorreu ao tentar definir o item de interação! causa&7: &c").append(e.getMessage()).append("\n").append("&aDefinindo item padrão."));
            item = new ItemStack(Material.COMPASS);
        }

        item = utils.getNbtVersion().insertNBT(item, "Compass", "bussola");
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(mainClass.getConfig().getStringReplaced("nome"));
        meta.setLore(mainClass.getConfig().getStringList("lore", null));
        item.setItemMeta(meta);
    }

    @EventHandler
    public void onClickInItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if(item == null || !item.getType().equals(this.item.getType())){
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (delay.contains(player)) {
            Map<String, Object> map = new HashMap<>();
            map.put("%delay%", delay.getExpiration(player).getDuration());

            player.sendMessage(mainClass.getConfig().getStringReplaced("em_cooldown", map));
            return;
        }

        if (!item.hasItemMeta() || !utils.getNbtVersion().hasNBTKey(item, "Compass")) {
            System.out.println(utils.getNbtVersion().getNBTValue(item, "Compass"));
            return;
        }

        if(!item.getItemMeta().getDisplayName().equalsIgnoreCase(mainClass.getConfig().getStringReplaced("nome"))){
            return;
        }

        Player nearestPlayer = getNearestPlayer(player);

        if (nearestPlayer == null) {
            player.sendMessage(mainClass.getConfig().getStringReplaced("nenhum_jogador_por_perto"));
            sendSound(player);
            delay.add(player);
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("%player%", nearestPlayer.getName());
        map.put("%distancia%", Math.round(player.getLocation().distance(nearestPlayer.getLocation())));

        double playerY = Math.abs(player.getLocation().getY());
        double targetY = Math.abs(nearestPlayer.getLocation().getY());

        if (targetY > playerY) {
            player.sendMessage(mainClass.getConfig().getStringReplaced("jogador_mais_proximo_acima", map));
        } else if (targetY < playerY) {
            player.sendMessage(mainClass.getConfig().getStringReplaced("jogador_mais_proximo_abaixo", map));
        } else {
            player.sendMessage(mainClass.getConfig().getStringReplaced("jogador_mais_proximo", map));
        }
        setCompassTrack(player, nearestPlayer.getLocation());
        sendSound(player);
        delay.add(player);
    }

    private void setCompassTrack(Player player, Location location) {
        player.setCompassTarget(location);
    }

    private Player getNearestPlayer(Player ofPlayer) {
        double x = mainClass.getConfig().getDouble("distancia.x");
        double y = mainClass.getConfig().getDouble("distancia.y");
        double z = mainClass.getConfig().getDouble("distancia.z");

        final AtomicDouble lastDistance = new AtomicDouble(Double.MAX_VALUE);
        final Player[] result = {null};

        ClanPlayer clan = clanManager.getClanPlayer(ofPlayer); // if player is of the same clan, ignore him.

        ofPlayer.getNearbyEntities(x, y, z).forEach(p -> {

            if (!(p instanceof Player)) {
                return;
            }

            ClanPlayer pclan = clanManager.getClanPlayer((Player) p);

            if (!(clan == null || pclan == null)) {
                Clan clan1 = clan.getClan();
                Clan clan2 = pclan.getClan();
                if(!clan1.equals(clan2)) {
                    return;
                }
            }
            try {
                if (isVanished((Player) p)) {
                    return;
                }
            }catch (Exception ignored){}

            try {
                if (SpectateAPI.getAPI().isSpectator((Player) p)) {
                    return;
                }
            } catch (Exception e) {
                mainClass.info(new StringBuilder("&cAlgo deu errado ao tentar acessar a API do plugin SpectatePlus! causa:&e  ").append(e.getMessage()).append("\n&aSolução, tente atualizar para a versão instável do plugin."));
            }

            if (p.getWorld() != ofPlayer.getWorld()) {
                return;
            }

            double distance = ofPlayer.getLocation().distance(p.getLocation());
            if (distance < lastDistance.get()) {
                lastDistance.set(distance);
                result[0] = (Player) p;
            }

        });
        return result[0];
    }

    private void sendSound(Player player) {
        if (!mainClass.getConfig().getBoolean("som_ao_clicar")) {
            return;
        }

        Sound sound;

        try {

            sound = Sound.valueOf(mainClass.getConfig().getString("som"));

        } catch (Exception e) {
            sound = Sound.ORB_PICKUP;
        }

        player.playSound(player.getLocation(), sound, 1f, 2f);
    }

    private boolean isVanished(Player p) {
        return StaffPlus.get()
                .userManager.get(p.getUniqueId())
                .getVanishType()
                .equals(VanishHandler.VanishType.NONE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            System.out.println("Invalid insertion");
            sender.sendMessage("Invalid insertion.");
            return true;
        }
        Player player = (Player) sender;
        if (args == null || args.length == 0) {
            int i = player.getInventory().firstEmpty();
            if (i < 0) {
                player.sendMessage("§cInventário cheio!");
                return true;
            }
            if(player.getInventory().contains(item)){
                sender.sendMessage("Already contains the item.");
                return true;
            }
            player.getInventory().setItem(i, item);
            return true;
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("§cjogador " + args[0] + " não está online!");
                return true;
            }
            int i = target.getInventory().firstEmpty();
            if (i < 0) {
                player.sendMessage("§cJogador está com o inventário cheio.");
                return true;
            }
            if(target.getInventory().contains(item)){
                return true;
            }
            target.getInventory().setItem(i, item);
            return true;
        }
        sender.sendMessage("Non have another options.");
        return true;
    }
}

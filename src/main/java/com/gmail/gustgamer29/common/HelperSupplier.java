package com.gmail.gustgamer29.common;

import com.gmail.gustgamer29.MainClass;
import net.shortninja.staffplus.StaffPlus;
import net.shortninja.staffplus.player.attribute.mode.handler.VanishHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class HelperSupplier {

    private MainClass main;

    @Inject
    HelperSupplier(MainClass main) {
        this.main = main;
    }

    public void sendSound(Player player) {
        if (!main.getConfig().getBoolean("som_ao_clicar")) {
            return;
        }

        Sound sound;

        try {

            sound = Sound.valueOf(main.getConfig().getString("som"));

        } catch (Exception e) {
            sound = Sound.ORB_PICKUP;
        }

        player.playSound(player.getLocation(), sound, 1f, 2f);
    }

    public boolean isVanished(Player p) {
        return StaffPlus.get()
                .userManager.get(p.getUniqueId())
                .getVanishType()
                .equals(VanishHandler.VanishType.NONE);
    }

}

package com.gmail.gustgamer29.reflection.v1_7_R1;

import com.gmail.gustgamer29.reflection.VersionProtocol;
import org.bukkit.entity.Player;

public class VersionProtocolControl1_7_R1 implements VersionProtocol {

    @Override
    public void sendTitle(Player player, String json, int fadein, int show, int fadeout) {
//        try{
//            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a(json), fadein, show, fadeout);
//            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
//            pc.sendPacket(cht);
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }

    @Override
    public void sendSubTitle(Player player, String json, int fadein, int show, int fadeout) {

    }

    @Override
    public void sendTitle(Player player, String t1, String t2, int fadein, int show, int fadeout) {

    }

    @Override
    public void sendActionBar(Player player, String messageInJson) {

    }

    @Override
    public void disconectPlayer(Player player, String reason) {

    }
}

package com.gmail.gustgamer29.reflection.v1_8_R3;

import com.gmail.gustgamer29.reflection.VersionProtocol;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class VersionProtocolControl1_8_R3 implements VersionProtocol {

    private static VersionProtocolControl1_8_R3 instance;

    private VersionProtocolControl1_8_R3(){
    }

    public static VersionProtocolControl1_8_R3 getInstance(){
        if(null == instance){
            instance = new VersionProtocolControl1_8_R3();
        }
        return instance;
    }

    public void sendActionBar(Player player, String json) {
        try {
            IChatBaseComponent baseComponent = IChatBaseComponent.ChatSerializer.a(json);
            PacketPlayOutChat cht = new PacketPlayOutChat(baseComponent, (byte)2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(cht);
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void disconectPlayer(Player player, String reason) {
        try {
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.disconnect(reason);
        }catch (Exception ex){
            ex. printStackTrace();
        }
    }

    public void sendTitle(Player player, String json, int fadein, int show, int fadeout){
        try{
            PacketPlayOutTitle cht = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(json), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void sendSubTitle(Player player, String json, int fadein, int show, int fadeout) {
        try {
            PacketPlayOutTitle ppot = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(json), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            pc.sendPacket(ppot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void sendTitle(Player player, String t1, String t2, int fadein, int show, int fadeout) {
        try {
            PacketPlayOutTitle cht = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(t1), fadein, show, fadeout);
            PacketPlayOutTitle ppot = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(t2), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            pc.sendPacket(cht);
            pc.sendPacket(ppot);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

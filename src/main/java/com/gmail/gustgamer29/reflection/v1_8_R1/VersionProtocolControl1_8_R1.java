package com.gmail.gustgamer29.reflection.v1_8_R1;

import com.gmail.gustgamer29.reflection.NBTVersion;
import com.gmail.gustgamer29.reflection.NbtFactory;
import com.gmail.gustgamer29.reflection.VersionProtocol;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VersionProtocolControl1_8_R1 implements VersionProtocol, NBTVersion {

    private static VersionProtocolControl1_8_R1 instance;

    private VersionProtocolControl1_8_R1(){
    }

    public static VersionProtocolControl1_8_R1 getInstance(){
        if(null == instance){
            instance = new VersionProtocolControl1_8_R1();
        }
        return instance;
    }

    @Override
    public org.bukkit.inventory.ItemStack insertNBT(org.bukkit.inventory.ItemStack item, String key, String value) {
        net.minecraft.server.v1_8_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound comp = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();
        comp.set(key, new net.minecraft.server.v1_8_R1.NBTTagString(value));
        comp.setString(key, value);
        itemStack.setTag(comp);
        itemStack.save(comp);
        NbtFactory.NbtCompound factory = NbtFactory.createCompound();
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public boolean hasNBTKey(org.bukkit.inventory.ItemStack item, String key) {
        net.minecraft.server.v1_8_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        return itemStack.hasTag() && itemStack.getTag().hasKey(key);
    }

    @Override
    public Object getNBTValue(ItemStack item, String key) {
        net.minecraft.server.v1_8_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        if (!itemStack.hasTag()) {
            return false;
        }
        return itemStack.getTag().getString(key);
    }

    @Override
    public void sendActionBar(Player player, String json) {
        try {
            IChatBaseComponent baseComponent = ChatSerializer.a(json);
            PacketPlayOutChat cht = new PacketPlayOutChat(baseComponent, (byte)2);
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(cht);
        }catch (Throwable ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void disconectPlayer(Player player, String reason) {
        try{
            PlayerConnection pc = ((CraftPlayer) player).getHandle().playerConnection;
            pc.disconnect(reason);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void sendTitle(Player player, String json, int fadein, int show, int fadeout) {
        try{
            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a(json), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void sendSubTitle(Player player, String json, int fadein, int show, int fadeout) {
        try{
            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a(json), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void sendTitle(Player player, String t1, String t2, int fadein, int show, int fadeout) {
        try{
            PacketPlayOutTitle pos = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a(t1), fadein, show, fadeout);
            PacketPlayOutTitle cht = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a(t2), fadein, show, fadeout);
            PlayerConnection pc = ((CraftPlayer)player).getHandle().playerConnection;
            pc.sendPacket(pos);
            pc.sendPacket(cht);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

//    @Override
//    public void sentAllBoties(){
//
//    }

}

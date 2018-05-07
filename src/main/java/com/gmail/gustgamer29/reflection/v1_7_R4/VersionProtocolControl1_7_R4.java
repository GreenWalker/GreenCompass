package com.gmail.gustgamer29.reflection.v1_7_R4;

import com.gmail.gustgamer29.reflection.NBTVersion;
import com.gmail.gustgamer29.reflection.NbtFactory;
import com.gmail.gustgamer29.reflection.VersionProtocol;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagString;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VersionProtocolControl1_7_R4 implements VersionProtocol, NBTVersion {

    private static VersionProtocolControl1_7_R4 instance;

    private VersionProtocolControl1_7_R4() {
    }

    public static VersionProtocolControl1_7_R4 getInstance() {
        if (null == instance) {
            instance = new VersionProtocolControl1_7_R4();
        }
        return instance;
    }

    @Override
    public ItemStack insertNBT(ItemStack item, String key, String value) {
        net.minecraft.server.v1_7_R4.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound comp = itemStack.hasTag() ? itemStack.getTag() : new NBTTagCompound();
        comp.set(key, new NBTTagString(value));
        comp.setString(key, value);
        itemStack.setTag(comp);
        itemStack.save(comp);
        NbtFactory.NbtCompound factory = NbtFactory.createCompound();
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public boolean hasNBTKey(ItemStack item, String key) {
        net.minecraft.server.v1_7_R4.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        return itemStack.hasTag() && itemStack.getTag().hasKey(key);
    }

    @Override
    public Object getNBTValue(ItemStack item, String key) {
        net.minecraft.server.v1_7_R4.ItemStack itemStack = CraftItemStack.asNMSCopy(item);
        if (!itemStack.hasTag()) {
            return false;
        }
        return itemStack.getTag().getString(key);
    }

    @Override
    public void sendTitle(Player player, String json, int fadein, int show, int fadeout) {

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

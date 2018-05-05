package com.gmail.gustgamer29.reflection;

import org.bukkit.inventory.ItemStack;

public interface NBTVersion {

    ItemStack insertNBT(ItemStack item, String key, String value);

    boolean hasNBTKey(ItemStack item, String key);

    Object getNBTValue(ItemStack item, String key);

}

package com.gmail.gustgamer29.common.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class Item {

    private ItemStack item;

    public Item(ItemStack item) {
        this.item = item;
    }

    public Item(Material mat) {
        this(new ItemStack(mat));
    }

    public Item(String matname) {
        this(Material.valueOf(matname));
    }

    public Item setName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name.replaceAll("&", "§"));
        this.item.setItemMeta(meta);
        return this;
    }

    public Item setLore(List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("&", "§"));
        }
        meta.setLore(lore);
        this.item.setItemMeta(meta);
        return this;
    }

    public Item setLore(String... lore) {
        ItemMeta meta = this.item.getItemMeta();
        for (int i = 0; i < lore.length; i++) {
            lore[i] = (lore[i].replaceAll("&", "§"));
        }
        meta.setLore(Arrays.asList(lore));
        this.item.setItemMeta(meta);
        return this;
    }

    public Item setEnchant(Enchantment enchant, int lvl) {
        this.item.addUnsafeEnchantment(enchant, lvl);
        return this;
    }

    public Item setAmount(int amount) {
        if (amount < 1 || amount > 64) {
            amount = 1;
        }
        this.item.setAmount(amount);
        return this;
    }

    public Item hideAttributes() {
        ItemMeta item = this.item.getItemMeta();
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_DESTROYS);
        this.item.setItemMeta(item);
        return this;
    }

    public Item setDurability(int d) {
        this.item.setDurability((short) d);
        return this;
    }

    public String getItemName() {
        if (this.item.hasItemMeta() && this.item.getItemMeta().hasDisplayName()) {
            return this.item.getItemMeta().getDisplayName();
        }
        return "";
    }

    public ItemStack build() {
        return item;

    }

    protected void setItem(ItemStack item) {
        this.item = item;
    }

}

package com.gmail.gustgamer29.common.item;

import com.gmail.gustgamer29.MainClass;
import com.gmail.gustgamer29.reflection.ReflectionUtils;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class ConfigurableItem extends Item {

    @Inject
    private MainClass mainClass;

    @Inject
    private ReflectionUtils utils;

    public ConfigurableItem(String mat) {
        super(mat);

        setName(mainClass.getConfig().getStringReplaced("nome"));
        setLore(mainClass.getConfig().getStringList("lore", null));
        setItem(utils.getNbtVersion().insertNBT(build(), "Compass", "bussola"));
    }

    public ItemStack getItem() {
        return build();
    }
}

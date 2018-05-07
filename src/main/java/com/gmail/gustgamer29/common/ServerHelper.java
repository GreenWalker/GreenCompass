package com.gmail.gustgamer29.common;

import com.gmail.gustgamer29.MainClass;
import com.gmail.gustgamer29.common.messages.Message;
import com.gmail.gustgamer29.common.messages.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import java.util.stream.Stream;

public class ServerHelper {

    @Inject
    private static MainClass main;

    @Inject
    ServerHelper() {
    }

    public static void info(StringBuilder msg) {
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&', msg.toString()));
    }

    public static void info(String msg) {
        StringBuilder builder = new StringBuilder();
        String[] arrayOfStrings = msg.split(" ");
        Stream<String> stringStream = Stream.of(arrayOfStrings);
        stringStream.map(StringUtil::colorize).forEach(builder::append);
        info(builder);
    }

    public static boolean verifyIfPluginIsPresent(Plugin plugin) {
        if (plugin == null) {
            info(MessageHandler.replaceMessage(Message.MISSING_PLUGIN,
                    StringUtil.join(main.getDescription().getDepend(), "&8, ").get(0)));
            desligar();
            return false;
        }
        info(MessageHandler.replaceMessage(Message.PLUGIN_FOUNDED, plugin.getName(), plugin.getDescription().getVersion()));
        return true;
    }

    public static void desligar() {
        main.getPluginLoader().disablePlugin(main);
    }
}

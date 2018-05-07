package com.gmail.gustgamer29;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import com.gmail.gustgamer29.common.ServerHelper;
import com.gmail.gustgamer29.common.item.ConfigurableItem;
import com.gmail.gustgamer29.common.messages.Message;
import com.gmail.gustgamer29.common.messages.MessageHandler;
import com.gmail.gustgamer29.reader.ConfigHandler;
import com.pgcraft.spectatorplus.SpectatorPlus;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainClass extends JavaPlugin{

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName();
    private static final String SMALL_VERSION_FORMAT = SERVER_VERSION.substring(SERVER_VERSION.lastIndexOf(".") + 1);

    private static String pluginBuildNumber = "Desconhecida";
    private static String pluginVersion = "N/D";

    private ConfigHandler config;
    private Injector injector;

    // ignore..
    public MainClass() {
    }

    @Override
    public void onEnable(){
        loadPluginInfo(getDescription().getVersion());

        ServerHelper.info(MessageHandler.replaceMessage(Message.SERVER_VERSION_RUNNING, SMALL_VERSION_FORMAT));

        initialize();

        ServerHelper.info(MessageHandler.replaceMessage(Message.PLUGIN_ACTVATED, getName(), pluginBuildNumber));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll();
    }

    private void initialize() {
        setupConfig();
        verifyDependencies();
        injector = new InjectorBuilder().addDefaultHandlers("com.gmail.gustgamer29").create();
        injector.register(MainClass.class, this);
        injector.register(Server.class, getServer());
        injector.register(String.class, SERVER_VERSION.substring(SERVER_VERSION.lastIndexOf(".") + 1));
        injector.register(ConfigurableItem.class, new ConfigurableItem(getConfig().getString("item")));

        setupListener(injector);
    }


    private void setupListener(Injector injector){
        PluginManager manager = getServer().getPluginManager();
        InteractEvents events = injector.getSingleton(InteractEvents.class);
        manager.registerEvents(events, this);
        getCommand("gbussola").setExecutor(events);
    }

    private void setupConfig(){
        try {
            config = new ConfigHandler(this, "configuracao.yml");
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public ConfigHandler getConfig() {
        return config;
    }

    private void verifyDependencies() {
        StaffPlus staffPlus = (StaffPlus) getServer().getPluginManager().getPlugin("StaffPlus");
        SpectatorPlus spectatorPlus = (SpectatorPlus) getServer().getPluginManager().getPlugin("SpectatorPlus");
        SimpleClans simpleClans = (SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans");

        List<Plugin> plugins = Arrays.asList(staffPlus, simpleClans, spectatorPlus);
        plugins.forEach(ServerHelper::verifyIfPluginIsPresent);

        if (!getDescription().getName().equalsIgnoreCase("GreenCompass")) {
            ServerHelper.desligar();
        }
    }

    private static void loadPluginInfo(String versionRaw) {
        int index = versionRaw.lastIndexOf("-");
        if (index != -1) {
            pluginVersion = versionRaw.substring(0, index);
            pluginBuildNumber = versionRaw.substring(index + 1);
            if (pluginBuildNumber.startsWith("b")) {
                pluginBuildNumber = pluginBuildNumber.substring(1);
            }
        }
    }
}

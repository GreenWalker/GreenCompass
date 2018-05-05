package com.gmail.gustgamer29;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import com.gmail.gustgamer29.reader.ConfigHandler;
import com.gmail.gustgamer29.reflection.ReflectionUtils;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class MainClass extends JavaPlugin{

    private static final String SERVER_VERSION = Bukkit.getServer().getClass().getPackage().getName();

    private static String pluginBuildNumber = "Desconhecida";
    private static String pluginVersion = "N/D";

    private ConfigHandler config;
    private Injector injector;
    private ReflectionUtils reflect;
    private ClanManager clanManager;

    // ignore..
    public MainClass() {
    }

    @Override
    public void onEnable(){
        loadPluginInfo(getDescription().getVersion());
        initialize();
        info(new StringBuilder("&aPlugin ").append("&e").append(getDescription().getName()).append("&a ").append("ativado! build&7:&e").append(pluginBuildNumber));
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
        injector.register(ClanManager.class, SimpleClans.getInstance().getClanManager());
        injector.register(String.class, SERVER_VERSION.substring(SERVER_VERSION.lastIndexOf(".") + 1));

        setupListener(injector);
    }

    private void verifyDependencies() {
        Object staffPlus = getServer().getPluginManager().getPlugin("StaffPlus");
        Object spectatorPlus = getServer().getPluginManager().getPlugin("SpectatorPlus");
        Object simpleClans = getServer().getPluginManager().getPlugin("SimpleClans");

        if(staffPlus == null){
            info(new StringBuilder("&aStaffPlus não foi encontrado no servidor! desligando plugin."));
            desligar();
            return;
        }

        if(spectatorPlus == null){
            info(new StringBuilder("&aSpectatorPlus não foi encontrado no servidor! desligando plugin."));
            desligar();
            return;
        }

        if(simpleClans == null){
            info(new StringBuilder("&aSimpleClans não foi encontrado no servidor! desligando plugin."));
            desligar();
            return;
        }

        if(!getDescription().getName().equalsIgnoreCase("GreenCompass")){
            desligar();
        }
    }

    private void desligar() {
        getServer().getPluginManager().disablePlugin(this);
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

    public void info(StringBuilder msg){
        Bukkit.getConsoleSender()
                .sendMessage(ChatColor.translateAlternateColorCodes('&' , msg.toString()));
    }

    @Override
    public ConfigHandler getConfig() {
        return config;
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

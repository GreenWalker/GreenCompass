package com.gmail.gustgamer29;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import com.gmail.gustgamer29.common.StringUtil;
import com.gmail.gustgamer29.reader.ConfigHandler;
import com.pgcraft.spectatorplus.SpectatorPlus;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import net.shortninja.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        info(new StringBuilder("&bO servidor está rodando na versão &a").append(SMALL_VERSION_FORMAT));
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
        StaffPlus staffPlus = (StaffPlus) getServer().getPluginManager().getPlugin("StaffPlus");
        SpectatorPlus spectatorPlus = (SpectatorPlus) getServer().getPluginManager().getPlugin("SpectatorPlus");
        SimpleClans simpleClans = (SimpleClans) getServer().getPluginManager().getPlugin("SimpleClans");

        List<Plugin> plugins = Arrays.asList(staffPlus, simpleClans, spectatorPlus);
        plugins.forEach(this::verifyIfPluginIsPresent);

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

    private boolean verifyIfPluginIsPresent(Plugin plugin) {
        if (plugin == null) {
            info(new StringBuilder("&aUm plugin necessário para a funcionalidade correta do plugin não foi encontrado no servidor! desligando plugin.")
                    .append("\nCertifique-se de que estes plugins estejam presentes na pasta plugins&7:&f ")
                    .append(StringUtil.join(getDescription().getDepend(), "&8, ")));
            desligar();
            return false;
        }
        info(new StringBuilder("&e").append(plugin.getName()).append(" &afoi encontrado no servidor! versão&7:&e ").append(plugin.getDescription().getVersion()));
        return true;
    }

}

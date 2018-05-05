package com.gmail.gustgamer29.reader;

import org.bukkit.plugin.Plugin;

import java.io.File;

public class JsonReader {

    private File jsonConfigFile;
    private Plugin plugin;

    public JsonReader(Plugin plugin, File jsonConfigFile) {
        this.jsonConfigFile = jsonConfigFile;
        this.plugin = plugin;
    }

    public void load(){
        try{

            for(File f : jsonConfigFile.listFiles()){
               // JSONParser parser =
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

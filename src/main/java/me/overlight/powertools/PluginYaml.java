package me.overlight.powertools;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PluginYaml {
    private final String path;
    private YamlConfiguration yml;

    public PluginYaml(String fileName) throws IOException {
        this.path = "plugins\\PowerToolS\\" + fileName + ".yml";
        yml = new YamlConfiguration();

        if(new File(path).exists())
            loadYaml();
        else
            saveYaml();
    }

    public void saveYaml() throws IOException {
        if(!new File(path).exists())
            new File(path.substring(0, path.lastIndexOf('\\'))).mkdirs();
        yml.save(new File(path));
    }

    public void loadYaml(){
        if(!new File(path).exists()) yml = new YamlConfiguration();
        else yml = YamlConfiguration.loadConfiguration(new File(path));
    }
    public void setYaml(YamlConfiguration yaml){
        this.yml = yaml;
    }
    public YamlConfiguration getYaml(){
        return this.yml;
    }
}

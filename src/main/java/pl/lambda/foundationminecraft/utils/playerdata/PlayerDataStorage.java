package pl.lambda.foundationminecraft.utils.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.io.File;
import java.io.IOException;

public class PlayerDataStorage
{
    private FoundationMinecraft plugin = FoundationMinecraft.getPlugin(FoundationMinecraft.class);
    private FileConfiguration config;
    private File configFile;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            configFile = new File(plugin.getDataFolder(), "playerData.yml");
            if(!configFile.exists())
            {
                Bukkit.getLogger().info("File playerData.yml not exist. creating one...");
                configFile.createNewFile();
                Bukkit.getLogger().info("File playerData.yml has been created and default config was inserted!");
            }
            config = YamlConfiguration.loadConfiguration(configFile);
            Bukkit.getLogger().info("Configuration playerData.yml is ready to use!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("IOException handled in PlayerDataStorage, disabling server...");
            Bukkit.shutdown();
        }

    }

    public void reload()
    {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void save()
    {
        try
        {
            config.save(configFile);
            Bukkit.getLogger().info("File playerData.yml and config has been saved!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("IOException handled when saving playerData.yml file!");
        }
    }

    public FileConfiguration getData()
    {
        return config;
    }
}

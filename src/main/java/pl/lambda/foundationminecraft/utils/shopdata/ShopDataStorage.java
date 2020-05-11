package pl.lambda.foundationminecraft.utils.shopdata;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.io.File;
import java.io.IOException;

public class ShopDataStorage
{
    private FoundationMinecraft plugin = FoundationMinecraft.getPlugin(FoundationMinecraft.class);
    private FileConfiguration config;
    private File configFile;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            configFile = new File(plugin.getDataFolder(), "shopData.yml");
            if(!configFile.exists())
            {
                Bukkit.getLogger().info("File shopData.yml not exist. creating one...");
                configFile.createNewFile();
                Bukkit.getLogger().info("File shopData.yml has been created and default config was inserted!");
            }
            config = YamlConfiguration.loadConfiguration(configFile);
            Bukkit.getLogger().info("Configuration shopData.yml is ready to use!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("IOException handled in ShopDataStorage, disabling server...");
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
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("IOException handled when saving shopData.yml file!");
        }
    }

    public FileConfiguration getData()
    {
        return config;
    }
}

package pl.lambda.foundationminecraft.utils.datastorage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.io.File;
import java.io.IOException;

public class SyncDataStorage implements IDataStorage
{
    FoundationMinecraft plugin = FoundationMinecraft.getPlugin(FoundationMinecraft.class);
    FileConfiguration config;
    File configFile;

    @Override
    public void setup()
    {
        try
        {
            if (!plugin.getDataFolder().exists())
            {
                if(!plugin.getDataFolder().mkdir())
                {
                    throw new IOException();
                }
            }

            configFile = new File(plugin.getDataFolder(), "syncStorage.yml");

            if (!configFile.exists())
            {
                if(!configFile.createNewFile())
                {
                    throw new IOException();
                }
            }

            config = YamlConfiguration.loadConfiguration(configFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void reload()
    {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void save()
    {
        try
        {
            config.save(configFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public FileConfiguration getData()
    {
        return config;
    }
}

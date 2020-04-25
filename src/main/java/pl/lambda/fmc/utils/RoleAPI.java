package pl.lambda.fmc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.lambda.fmc.plugin.FMC;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class RoleAPI
{
    private final String FILENAME = "roles.yml";
    private FMC plugin = FMC.getPlugin(FMC.class);
    private FileConfiguration config;
    private File configFile;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists())
            {
                plugin.getDataFolder().mkdir();
            }

            configFile = new File(plugin.getDataFolder(), FILENAME);

            if(!configFile.exists())
            {
                configFile.createNewFile();
            }

            config = YamlConfiguration.loadConfiguration(configFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void save()
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

    private void reload()
    {
        if(configFile == null)
        {
            setup();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String getRoleUUIDByDiscord(String discordid)
    {
        reload();
        ConfigurationSection keys = config.getConfigurationSection("roles");
        if (keys == null) return null;

        for (String uuid : keys.getKeys(false)) {
            String data = config.getString("roles." + uuid + ".discordid");
            if(data == null) continue;
            if (!data.equalsIgnoreCase(discordid)) continue;
            return uuid;
        }
        return null;
    }

    public String getRoleUUIDByName(String name)
    {
        reload();
        ConfigurationSection keys = config.getConfigurationSection("roles");
        if (keys == null) return null;

        for (String uuid : keys.getKeys(false)) {
            String data = config.getString("roles." + uuid + ".name");
            if(data == null) continue;
            if (!data.equalsIgnoreCase(name)) continue;
            return uuid;
        }
        return null;
    }

    public boolean create(String name, String shortcut, String color, String discordid, boolean isClearance)
    {
        reload();
        if(roleExist(getRoleUUIDByName(name))) return false;
        String uuid = UUID.randomUUID().toString();
        config.set("roles." + uuid + ".name", name);
        config.set("roles." + uuid + ".shortcut", shortcut);
        config.set("roles." + uuid + ".color", color);
        config.set("roles." + uuid + ".discordid", discordid);
        config.set("roles." + uuid + ".location.x", 0);
        config.set("roles." + uuid + ".location.y", 0);
        config.set("roles." + uuid + ".location.z", 0);
        config.set("roles." + uuid + ".location.w", "0");
        save(); return true;
    }

    public boolean delete(String uuid)
    {
        reload();
        if(!roleExist(uuid)) return false;
        config.set("roles." + uuid, null);
        save(); return true;
    }

    public boolean roleExist(String uuid)
    {
        reload();
        String check = config.getString("roles." + uuid + ".discordid");
        if(check == null) return false;
        return true;
    }

    public void setLocation(String uuid, Location l)
    {
        reload();
        if(!roleExist(uuid)) return;

        World w = l.getWorld();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();

        config.set("roles." + uuid + ".location.x", x);
        config.set("roles." + uuid + ".location.y", y);
        config.set("roles." + uuid + ".location.z", z);
        config.set("roles." + uuid + ".location.w", w.getName());
        save();
    }

    public Location getLocation(String uuid)
    {
        reload();
        String world = config.getString("roles." + uuid + ".location.w");
        if(world == null) return null;
        World w = Bukkit.getWorld(world);
        int x = config.getInt("roles." + uuid + ".location.x");
        int y = config.getInt("roles." + uuid + ".location.y");
        int z = config.getInt("roles." + uuid + ".location.z");
        return new Location(w, x, y, z);
    }

    public String getDiscordId(String uuid)
    {
        reload();
        String discord = config.getString("roles." + uuid + ".discordid");
        if(discord == null) return null;
        return discord;
    }

    public char getColor(String uuid)
    {
        reload();
        String color = config.getString("roles." + uuid + ".color");
        if(color == null) return 'g';
        return color.charAt(0);
    }

    public String getShortcut(String uuid)
    {
        reload();
        String shortcut = config.getString("roles." + uuid + ".shortcut");
        if(shortcut == null) return null;
        return shortcut;
    }

    public String getName(String uuid)
    {
        reload();
        String name = config.getString("roles." + uuid + ".name");
        if(name == null) return null;
        return name;
    }
}

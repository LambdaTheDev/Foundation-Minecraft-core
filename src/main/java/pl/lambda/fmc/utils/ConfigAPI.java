package pl.lambda.fmc.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.lambda.fmc.plugin.FMC;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigAPI
{
    private final String FILENAME = "config.yml";
    private FMC plugin = FMC.getPlugin(FMC.class);
    private FileConfiguration config;
    private File configFile;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            configFile = new File(plugin.getDataFolder(), FILENAME);
            if(!configFile.exists())
            {
                configFile.createNewFile();
                defaultConfig();
            }
            config = YamlConfiguration.loadConfiguration(configFile);
        }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void save()
    {
        try { config.save(configFile); }
        catch (IOException e) { e.printStackTrace(); }
    }

    private void reload()
    {
        if(configFile == null) setup();
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    private void defaultConfig()
    {
        reload();
        config.set("config", null);
        config.set("config.discord.bottoken", "token");
        config.set("config.discord.prefix", "+");
        config.set("config.discord.syncchannel", "channelid");
        config.set("config.discord.serverid", "serverid");

        config.set("config.server.discordlink", "https://discord.gg/XYZ");
        config.set("config.server.spawn.x", 0);
        config.set("config.server.spawn.y", 100);
        config.set("config.server.spawn.z", 0);
        config.set("config.server.spawn.w", "world");

        config.set("config.roles.level1role", "levelroleid");
        config.set("config.roles.level2role", "levelroleid");
        config.set("config.roles.level3role", "levelroleid");
        config.set("config.roles.level4role", "levelroleid");
        config.set("config.roles.level5role", "levelroleid");
        config.set("config.roles.sitedirector", "sitedirecorroleid");
        save();
    }

    public void setSpawnLocation(int x, int y, int z, String worldName)
    {
        reload();
        config.set("config.server.spawn.x", x);
        config.set("config.server.spawn.y", y);
        config.set("config.server.spawn.z", z);
        config.set("config.server.spawn.w", worldName);
        save();
    }

    public Location getSpawnLocation()
    {
        reload();
        Location l;
        int x = config.getInt("config.server.spawn.x");
        int y = config.getInt("config.server.spawn.y");
        int z = config.getInt("config.server.spawn.z");
        String w = config.getString("config.server.spawn.w");

        if(w == null)
        {
            return null;
        }

        World world = Bukkit.getWorld(w);

        l = new Location(world, x, y, z);
        return l;
    }

    public String getSiteDirectorRoleID()
    {
        reload();
        String res = config.getString("config.roles.sitedirector");
        if(res == null) defaultConfig();
        return res;
    }

    public String getDiscordPrefix()
    {
        reload();
        String res = config.getString("config.discord.prefix");
        if(res == null) defaultConfig();
        return res;
    }

    public String getDiscordToken()
    {
        reload();
        String res = config.getString("config.discord.bottoken");
        if(res == null) defaultConfig();
        return res;
    }

    public String getDiscordServerId()
    {
        reload();
        String res = config.getString("config.discord.serverid");
        if(res == null) defaultConfig();
        return res;
    }

    public String getServerDiscordlink()
    {
        reload();
        String res = config.getString("config.server.discordlink");
        if(res == null) defaultConfig();
        return res;
    }

    public String getSyncChannelID()
    {
        reload();
        String res = config.getString("config.discord.syncchannel");
        if(res == null) defaultConfig();
        return res;
    }

    public String getRoleClearance(int level)
    {
        reload();
        String res = config.getString("config.roles.level" + level + "role");
        if(res == null) defaultConfig();
        return res;
    }

    public Map<Integer, String> getAllClearanceRoleID()
    {
        Map<Integer, String> result = new HashMap<>();
        result.put(1, getRoleClearance(1));
        result.put(2, getRoleClearance(2));
        result.put(3, getRoleClearance(3));
        result.put(4, getRoleClearance(4));
        result.put(5, getRoleClearance(5));
        return result;
    }

    public static <K, V> K getMapKey(Map<K, V> map, V value)
    {
        return map.keySet().stream().filter(key -> value.equals(map.get(key))).findFirst().get();
    }
}

package pl.ytskagamer.fmc.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.ytskagamer.fmc.plugin.FMC;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersAPI
{
    private final String FILENAME = "users.yml";
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
            FMC.error(e.getMessage());
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
            FMC.error(e.getMessage());
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

    public String getUserUUIDByNick(String nick)
    {
        reload();
        ConfigurationSection keys = config.getConfigurationSection("users");
        if (keys == null) return null;

        for (String uuid : keys.getKeys(false)) {
            String data = config.getString("users." + uuid + ".nick");
            if(data == null) continue;
            if (!data.equalsIgnoreCase(nick)) continue;
            return uuid;
        }
        return null;
    }

    public String getUserUUIDByDiscord(String discordid)
    {
        reload();
        ConfigurationSection keys = config.getConfigurationSection("users");
        if (keys == null) return null;

        for (String uuid : keys.getKeys(false)) {
            String data = config.getString("users." + uuid + ".discordid");
            if(data == null) continue;
            if (!data.equalsIgnoreCase(discordid)) continue;
            return uuid;
        }
        return null;
    }

    public boolean create(String uuid, String name)
    {
        reload();
        if(accountExist(uuid)) return false;
        List<String> set = new ArrayList<>();
        config.set("users." + uuid + ".nick", name.toLowerCase());
        config.set("users." + uuid + ".discordid", "none");
        config.set("users." + uuid + ".currentrole", "none");
        config.set("users." + uuid + ".chat", true);
        config.set("users." + uuid + ".roles", set);
        config.set("users." + uuid + ".clearance", 0);
        save();
        return true;
    }

    public boolean accountExist(String uuid)
    {
        reload();
        String test = config.getString("users." + uuid + ".discordid");
        if(test == null) return false;
        return true;
    }

    public boolean delete(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return false;
        config.set("users." + uuid, null);
        save();
        return true;
    }

    public String getDiscordID(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return null;
        String discordid = config.getString("users." + uuid + ".discordid");
        if(discordid == null) return "none";
        return discordid;
    }

    public void setDiscordID(String uuid, String discordid)
    {
        reload();
        if(!accountExist(uuid)) return;
        config.set("users." + uuid + ".discordid", discordid);
        save();
    }

    public void setChat(String uuid, boolean isglobal)
    {
        reload();
        if(!accountExist(uuid)) return;
        config.set("users." + uuid + ".chat", isglobal);
        save();
    }

    public boolean getChat(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return false;
        return config.getBoolean("users." + uuid + ".chat");
    }

    public boolean addRoles(String uuid, String... roles)
    {
        reload();
        if(!accountExist(uuid)) return false;
        List<String> userRoles = getRoles(uuid);
        for(String role : roles)
        {
            if(userRoles.contains(role)) continue;
            userRoles.add(role);
        }
        config.set("users." + uuid + ".roles", null);
        config.set("users." + uuid + ".roles", userRoles);
        save();
        return true;
    }

    public boolean deleteRoles(String uuid, String... roles)
    {
        reload();
        if(!accountExist(uuid)) return false;
        List<String> userRoles = getRoles(uuid);
        for(String role : roles)
        {
            if(!userRoles.contains(role)) continue;
            userRoles.remove(role);
        }
        config.set("users." + uuid + ".roles", null);
        config.set("users." + uuid + ".roles", userRoles);
        save();
        return true;
    }

    public List<String> getRoles(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return null;
        return config.getStringList("users." + uuid + ".roles");
    }

    public void clearRoles(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return;
        List<String> set = new ArrayList<>();
        config.set("users." + uuid + ".roles", set);
        save();
    }

    public String getCurrentRole(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return null;
        String roleid = config.getString("users." + uuid + ".currentrole");
        if(roleid == null || roleid.equalsIgnoreCase("none"))
        {
            return null;
        }
        return roleid;
    }

    public void setCurrentRole(String uuid, String role)
    {
        reload();
        if(!accountExist(uuid)) return;
        config.set("users." + uuid + ".currentrole", role);
        save();
    }

    public void setClearance(String uuid, int clearance)
    {
        reload();
        if(!accountExist(uuid)) return;
        config.set("users." + uuid + ".clearance", clearance);
        save();
    }

    public int getClearance(String uuid)
    {
        reload();
        if(!accountExist(uuid)) return -1;
        return config.getInt("users." + uuid + ".clearance");
    }
}

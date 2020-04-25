package pl.ytskagamer.fmc.utils;

import net.dv8tion.jda.api.entities.User;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.plugin.FMC;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class SyncAPI
{
    private final String FILENAME = "sync.yml";
    private FMC plugin = FMC.getPlugin(FMC.class);
    private FileConfiguration config;
    private File configFile;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            configFile = new File(plugin.getDataFolder(), FILENAME);
            if(!configFile.exists()) configFile.createNewFile();
            config = YamlConfiguration.loadConfiguration(configFile);
        }
        catch (IOException e) { FMC.error(e.getMessage()); }
    }

    private void save()
    {
        try { config.save(configFile); }
        catch (IOException e) { FMC.error(e.getMessage()); }
    }

    private void reload()
    {
        if(configFile == null) setup();
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public boolean startSync(String discordid, String name, String code)
    {
        reload();
        UsersAPI usersAPI = new UsersAPI();
        if(!(usersAPI.getDiscordID(usersAPI.getUserUUIDByDiscord(discordid)) == null)) return false;
        config.set("sync." + name.toLowerCase() + ".discordid", discordid);
        config.set("sync." + name.toLowerCase() + ".code", code);
        save();
        return true;
    }

    public boolean finishSync(String name, String code)
    {
        reload();
        UsersAPI usersAPI = new UsersAPI();
        String syncCode = config.getString("sync." + name.toLowerCase() + ".code");
        if(syncCode == null) return false;
        if(syncCode.equals(code))
        {
            String discordid = config.getString("sync." + name.toLowerCase() + ".discordid");
            if (discordid == null) return false;
            String uuid = usersAPI.getUserUUIDByNick(name);
            usersAPI.setDiscordID(uuid, discordid);
            return true;
        }
        return false;
    }

    public String getCode(String name)
    {
        reload();
        String syncCode = config.getString("sync." + name.toLowerCase() + ".code");
        if(syncCode == null) return null;
        return syncCode;
    }

    public void deleteSyncData(String name)
    {
        reload();
        config.set("sync." + name.toLowerCase(), null);
        save();
    }
}

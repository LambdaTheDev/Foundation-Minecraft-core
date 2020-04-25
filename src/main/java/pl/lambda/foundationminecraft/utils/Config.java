package pl.lambda.foundationminecraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.io.File;
import java.io.IOException;

public class Config
{
    FoundationMinecraft plugin = FoundationMinecraft.getPlugin(FoundationMinecraft.class);
    File configFile;
    FileConfiguration config;

    public String botToken;
    public String botPrefix;
    public String syncChannelID;
    public String guildID;
    public String discordInvitation;

    public Location spawnLocation;
    public String messagePrefix;
    public String noPermissionMessage;
    public String motd;

    public String level0role;
    public String level1role;
    public String level2role;
    public String level3role;
    public String level4role;
    public String level5role;
    public String siteDirectorRole;

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

            configFile = new File(plugin.getDataFolder(), "config.yml");
            boolean loadDefaults = false;

            if (!configFile.exists())
            {
                if(!configFile.createNewFile())
                {
                    throw new IOException();
                }
                loadDefaults = true;
            }

            config = YamlConfiguration.loadConfiguration(configFile);
            if(loadDefaults) loadDefaults();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        reloadConfigObject();
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
        }
    }

    public void saveConfig()
    {
        config.set("discord.botToken", this.botToken);
        config.set("discord.botPrefix", this.botPrefix);
        config.set("discord.syncChannelID", this.syncChannelID);
        config.set("discord.guildID", this.guildID);
        config.set("discord.invitation", this.discordInvitation);

        config.set("server.spawnLocation.x", this.spawnLocation.getBlockX());
        config.set("server.spawnLocation.y", this.spawnLocation.getBlockY());
        config.set("server.spawnLocation.z", this.spawnLocation.getBlockZ());
        config.set("server.spawnLocation.w", this.spawnLocation.getWorld().getName());
        config.set("server.messagePrefix", this.messagePrefix);
        config.set("server.noPermissionMessage", this.noPermissionMessage);
        config.set("server.motd", this.motd);

        config.set("roles.level0", this.level0role);
        config.set("roles.level1", this.level1role);
        config.set("roles.level2", this.level2role);
        config.set("roles.level3", this.level3role);
        config.set("roles.level4", this.level4role);
        config.set("roles.level5", this.level5role);
        config.set("roles.siteDirector", this.siteDirectorRole);
        save();
    }

    public Config getConfig()
    {
        reloadConfigObject();
        return this;
    }

    void reloadConfigObject()
    {
        this.botToken = config.getString("discord.botToken");
        this.botPrefix = config.getString("discord.prefix");
        this.syncChannelID = config.getString("discord.syncChannelID");
        this.guildID = config.getString("discord.guildID");
        this.discordInvitation = config.getString("discord.invitation");

        int spawnX = config.getInt("server.spawnLocation.x");
        int spawnY = config.getInt("server.spawnLocation.y");
        int spawnZ = config.getInt("server.spawnLocation.z");
        String spawnWorld = config.getString("server.spawnLocation.w");
        World spawnW = Bukkit.getWorld(spawnWorld);
        this.spawnLocation = new Location(spawnW, spawnX, spawnY, spawnZ);
        this.messagePrefix = config.getString("server.messagePrefix") + ' ';
        this.noPermissionMessage = config.getString("server.noPermissionMessage");
        this.motd = config.getString("server.motd");

        this.level0role = config.getString("roles.level0");
        this.level1role = config.getString("roles.level1");
        this.level2role = config.getString("roles.level2");
        this.level3role = config.getString("roles.level3");
        this.level4role = config.getString("roles.level4");
        this.level5role = config.getString("roles.level5");
        this.siteDirectorRole = config.getString("roles.siteDirector");
    }

    void loadDefaults()
    {
        config.set("discord.botToken", "insert-here-bot-token");
        config.set("discord.botPrefix", "+");
        config.set("discord.syncChannelID", "insert-here-sync-channel-id");
        config.set("discord.guildID", "insert-here-your-server-id");
        config.set("discord.invitation", "https://discord.gg/your-invitation");

        config.set("server.spawnLocation.x", 0);
        config.set("server.spawnLocation.y", 100);
        config.set("server.spawnLocation.z", 0);
        config.set("server.spawnLocation.w", "world");
        config.set("server.messagePrefix", "&aExample");
        config.set("server.noPermissionMessage", "&aYou don't have permission to use that command!");
        config.set("server.motd", "&cExample &aMOTD&c!");

        config.set("roles.level0", "level-0-role-id");
        config.set("roles.level1", "level-1-role-id");
        config.set("roles.level2", "level-2-role-id");
        config.set("roles.level3", "level-3-role-id");
        config.set("roles.level4", "level-4-role-id");
        config.set("roles.level5", "level-5-role-id");
        config.set("roles.siteDirector", "siteDirector-role-id");
        save();
    }
}

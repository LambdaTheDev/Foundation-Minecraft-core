package pl.lambda.foundationminecraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Config
{
    private FoundationMinecraft plugin = FoundationMinecraft.getPlugin(FoundationMinecraft.class);
    private FileConfiguration config;
    private File configFile;

    private final int CONFIG_VERSION = 1;

    private int configVersion;
    private String discordBotToken;
    private String discordBotPrefix;
    private String discordGuild;
    private String discordSyncChannel;
    private String discordInvitation;

    private Location spawnLocation;
    private String messagesPrefix;
    private String motd;
    private String noPermissionMessage;

    private HashMap<String, Integer> levelRoles;
    private String siteDirectorRole;

    public void setup()
    {
        try
        {
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            configFile = new File(plugin.getDataFolder(), "config.yml");
            if(!configFile.exists())
            {
                Bukkit.getLogger().info("File config.yml not exist. creating one...");
                configFile.createNewFile();
                createDefaultConfig();
                Bukkit.getLogger().info("File config.yml has been created and default config was inserted!");
            }
            config = YamlConfiguration.loadConfiguration(configFile);
            Bukkit.getLogger().info("Configuration config.yml is ready to use!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("IOException handled in Config, disabling server...");
            Bukkit.shutdown();
        }

        buildObject();
    }

    public void reload()
    {
        config = YamlConfiguration.loadConfiguration(configFile);
        buildObject();
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
            System.err.println("IOException handled when saving config.yml file!");
        }
    }

    public void saveConfigObjectToFile()
    {
        config.set("minecraft.spawn.x", this.spawnLocation.getBlockX());
        config.set("minecraft.spawn.y", this.spawnLocation.getBlockY());
        config.set("minecraft.spawn.z", this.spawnLocation.getBlockZ());
        config.set("minecraft.spawn.w", this.spawnLocation.getWorld().getName());
        config.set("minecraft.messagePrefix", this.messagesPrefix);
        config.set("minecraft.motd", this.motd);
        config.set("minecraft.noPermissionMessage", this.noPermissionMessage);
        save();
    }

    private void createDefaultConfig()
    {
        reload();
        config.set("configVersion", CONFIG_VERSION);
        config.set("discord.botToken", "example-bot-token");
        config.set("discord.botPrefix", "+");
        config.set("discord.guild", "discord-guild-id");
        config.set("discord.syncChannel", "sync-channel-id");
        config.set("discord.invitation", "https://discord.gg/example");

        config.set("minecraft.spawn.x", 0);
        config.set("minecraft.spawn.y", 0);
        config.set("minecraft.spawn.z", 0);
        config.set("minecraft.spawn.w", "world");
        config.set("minecraft.messagePrefix", "&0[&aExample&0]");
        config.set("minecraft.motd", "&aExample&r - &bMessage &cof &dthe &fday &1!");
        config.set("minecraft.noPermissionMessage", "&4You don't have permission to use that command!");

        config.set("roles.level0", "discord-role-id");
        config.set("roles.level1", "discord-role-id");
        config.set("roles.level2", "discord-role-id");
        config.set("roles.level3", "discord-role-id");
        config.set("roles.level4", "discord-role-id");
        config.set("roles.level5", "discord-role-id");
        config.set("roles.siteDirector", "discord-role-id");
        save();
    }

    public Config buildObject()
    {
        reload();
        this.configVersion = config.getInt("configVersion");
        this.discordBotToken = config.getString("discord.botToken");
        this.discordBotPrefix = config.getString("discord.botPrefix");
        this.discordGuild = config.getString("discord.guild");
        this.discordSyncChannel = config.getString("discord.syncChannel");
        this.discordInvitation = config.getString("discord.invitation");

        int minecraftSpawnLocX = config.getInt("minecraft.spawn.x");
        int minecraftSpawnLocY = config.getInt("minecraft.spawn.y");
        int minecraftSpawnLocZ = config.getInt("minecraft.spawn.z");
        String minecraftSpawnLocWName = config.getString("minecraft.spawn.w");
        World minecraftSpawnLocW = Bukkit.getWorld(minecraftSpawnLocWName);

        this.spawnLocation = new Location(minecraftSpawnLocW, minecraftSpawnLocX, minecraftSpawnLocY, minecraftSpawnLocZ);
        this.messagesPrefix = config.getString("minecraft.messagePrefix");
        this.motd = config.getString("minecraft.motd");
        this.noPermissionMessage = config.getString("minecraft.noPermissionMessage");

        String level0roleID = config.getString("roles.level0");
        String level1roleID = config.getString("roles.level1");
        String level2roleID = config.getString("roles.level2");
        String level3roleID = config.getString("roles.level3");
        String level4roleID = config.getString("roles.level4");
        String level5roleID = config.getString("roles.level5");

        HashMap<String, Integer> levelRoles = new HashMap<>();
        levelRoles.put(level0roleID, 0);
        levelRoles.put(level1roleID, 1);
        levelRoles.put(level2roleID, 2);
        levelRoles.put(level3roleID, 3);
        levelRoles.put(level4roleID, 4);
        levelRoles.put(level5roleID, 5);

        this.levelRoles = levelRoles;
        this.siteDirectorRole = config.getString("roles.siteDirector");
        return this;
    }

    public int getConfigVersion() {
        return configVersion;
    }

    public String getDiscordBotToken() {
        return discordBotToken;
    }

    public String getDiscordBotPrefix() {
        return discordBotPrefix;
    }

    public String getDiscordGuild() {
        return discordGuild;
    }

    public String getDiscordSyncChannel() {
        return discordSyncChannel;
    }

    public String getDiscordInvitation() {
        return discordInvitation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getMessagesPrefix() {
        return messagesPrefix;
    }

    public String getMotd() {
        return motd;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public HashMap<String, Integer> getLevelRoles() {
        return levelRoles;
    }

    public String getSiteDirectorRole() {
        return siteDirectorRole;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}

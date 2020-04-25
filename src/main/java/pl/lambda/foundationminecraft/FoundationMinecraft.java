package pl.lambda.foundationminecraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.plugin.commands.*;
import pl.lambda.foundationminecraft.plugin.listeners.*;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LDeprecated;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.datastorage.PlayerDataStorage;
import pl.lambda.foundationminecraft.utils.datastorage.RankDataStorage;
import pl.lambda.foundationminecraft.utils.datastorage.SyncDataStorage;

import java.awt.*;
import java.util.HashMap;

public class FoundationMinecraft extends JavaPlugin
{
    public static final String VERSION = "2.0";
    public static final String INFO = "";

    public static FoundationMinecraft instance;
    public static boolean serverLoaded = false;

    public HashMap<Player, LPlayer> lambdaPlayers = new HashMap<>();
    public HashMap<String, LRank> ranks = new HashMap<>();
    public DiscordModule discordModule;

    Config config;
    PlayerDataStorage playerDataStorage;
    RankDataStorage rankDataStorage;
    SyncDataStorage syncDataStorage;

    @Override
    public void onEnable()
    {
        instance = this;
        discordModule = new DiscordModule();

        setupConfig();
        setupCommands();
        setupListeners();
        setupDiscord();
    }

    @Override
    public void onDisable()
    {
        serverLoaded = false;
        discordModule.sendMessageToSyncChannel("Server disabled!", Color.red, "We have turned off server.", "");
        if(discordModule.jda != null) discordModule.jda.shutdown();
        lambdaPlayers.clear();
    }

    void setupConfig()
    {
        config = new Config();
        playerDataStorage = new PlayerDataStorage();
        rankDataStorage = new RankDataStorage();
        syncDataStorage = new SyncDataStorage();

        config.setup();
        config.getConfig();
        playerDataStorage.setup();
        rankDataStorage.setup();
        syncDataStorage.setup();

        LRank.loadRanks();
    }

    void setupDiscord()
    {
        new Thread(() -> discordModule.startBot()).start();
    }

    void setupCommands()
    {
        getCommand("chattype").setExecutor(new MChattypeCmd());
        getCommand("depts").setExecutor(new MDeptsCmd());
        getCommand("gamemode").setExecutor(new MGamemodeCmd());
        getCommand("keycard").setExecutor(new MKeycardCmd());
        getCommand("money").setExecutor(new MMoneyCmd());
        getCommand("pay").setExecutor(new MPayCmd());
        getCommand("setspawm").setExecutor(new MSetspawnCmd());
        getCommand("spawn").setExecutor(new MSetspawnCmd());
        getCommand("sync").setExecutor(new MSyncCmd());
        getCommand("tp").setExecutor(new MTpCmd());
    }

    void setupListeners()
    {
        getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
        getServer().getPluginManager().registerEvents(new OnServerPing(), this);
        getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLogin(), this);
    }

    public Config getFMCConfig() {
        return config;
    }

    public PlayerDataStorage getPlayerDataStorage() {
        return playerDataStorage;
    }

    public RankDataStorage getRankDataStorage() {
        return rankDataStorage;
    }

    public SyncDataStorage getSyncDataStorage() {
        return syncDataStorage;
    }

    public static String getUsageMessage(String usage)
    {
        return ChatColor.translateAlternateColorCodes('&', FoundationMinecraft.getPlugin(FoundationMinecraft.class).config.messagePrefix) + ChatColor.RED + "Incorrect usage! Correct: " + usage + "!";
    }

    public static String checkIfDeprecated(Class<?> checkClass)
    {
        LDeprecated check = checkClass.getAnnotation(LDeprecated.class);
        if(check == null) return null;
        if(check.willBeDeleted())
        {
            return "This is deprecated! It will be deleted in future updates. Suggested new usage: " + check.newUsage() + ".";
        }
        else
        {
            return "This is deprecated! It will not be deleted soon, but you should stop using it. Suggested new usage: " + check.newUsage() + ".";
        }
    }
}

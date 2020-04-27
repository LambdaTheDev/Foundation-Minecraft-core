package pl.lambda.foundationminecraft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.plugin.commands.*;
import pl.lambda.foundationminecraft.plugin.listeners.*;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.enums.PluginLoader;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.playerdata.PlayerDataStorage;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;
import pl.lambda.foundationminecraft.utils.ranksdata.RankDataStorage;
import pl.lambda.foundationminecraft.utils.syncdata.SyncDataStorage;
import pl.lambda.foundationminecraft.utils.syncdata.SyncManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoundationMinecraft extends JavaPlugin
{
    public static final String PLUGIN_INFO = "";
    public static final String VERSION = "2.0";
    public static boolean SERVER_ENABLED = false;

    private static FoundationMinecraft instance;

    private Config fmcConfig;
    private PlayerDataStorage playerDataStorage;
    private RankDataStorage rankDataStorage;
    private SyncDataStorage syncDataStorage;
    private DiscordModule discordModule;

    private HashMap<Player, LambdaPlayer> lambdaPlayers = new HashMap<>();
    private List<LambdaRank> lambdaRanks = new ArrayList<>();

    @Override
    public void onEnable()
    {
        instance = this;

        setupFoundationMinecraft(PluginLoader.CONFIG);
        setupFoundationMinecraft(PluginLoader.COMMANDS);
        setupFoundationMinecraft(PluginLoader.LISTENERS);
        setupFoundationMinecraft(PluginLoader.DISCORD);
    }

    @Override
    public void onDisable()
    {
        SyncManager.clearSync();
        lambdaPlayers.clear();
        lambdaRanks.clear();
    }

    private void setupFoundationMinecraft(PluginLoader type)
    {
        switch (type)
        {
            case CONFIG:
                fmcConfig = new Config();
                playerDataStorage = new PlayerDataStorage();
                rankDataStorage = new RankDataStorage();
                syncDataStorage = new SyncDataStorage();

                fmcConfig.setup();
                playerDataStorage.setup();
                rankDataStorage.setup();
                syncDataStorage.setup();

                LambdaRank.loadRanks();
            case DISCORD:
                discordModule = new DiscordModule();
                new Thread(discordModule::start).start();
            case COMMANDS:
                getCommand("chattype").setExecutor(new MCmdChattype());
                getCommand("depts").setExecutor(new MCmdDepts());
                getCommand("gamemode").setExecutor(new MCmdGamemode());
                getCommand("keycard").setExecutor(new MCmdKeycard());
                getCommand("money").setExecutor(new MCmdMoney());
                getCommand("pay").setExecutor(new MCmdPay());
                getCommand("setspawn").setExecutor(new MCmdSetspawn());
                getCommand("spawn").setExecutor(new MCmdSpawn());
                getCommand("sync").setExecutor(new MCmdSync());
            case LISTENERS:
                getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
                getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLogin(), this);
                getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
                getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
                getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
                getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
                getServer().getPluginManager().registerEvents(new OnServerPing(), this);
        }
    }

    public static FoundationMinecraft getInstance()
    {
        return instance;
    }

    public Config getFmcConfig() {
        return fmcConfig;
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

    public DiscordModule getDiscordModule() {
        return discordModule;
    }

    public HashMap<Player, LambdaPlayer> getLambdaPlayers() {
        return lambdaPlayers;
    }

    public List<LambdaRank> getLambdaRanks() {
        return lambdaRanks;
    }

    public static String getPrefix()
    {
        return ChatColor.translateAlternateColorCodes('&', FoundationMinecraft.getInstance().getFmcConfig().getMessagesPrefix()) + ' ';
    }

    public static String getUsage(String usage)
    {
        return getPrefix() + ChatColor.RED + "Incorrect command usage! Correct: " + usage + "!";
    }
}

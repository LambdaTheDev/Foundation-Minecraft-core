package pl.lambda.foundationminecraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
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
    public static final String VERSION = "2.1";
    public static boolean SERVER_ENABLED = false;
    public static boolean EXPLOSIONS_ALLOWED = false;

    private static FoundationMinecraft instance;

    private Config fmcConfig;
    private PlayerDataStorage playerDataStorage;
    private RankDataStorage rankDataStorage;
    private SyncDataStorage syncDataStorage;
    private DiscordModule discordModule;

    private HashMap<Player, LambdaPlayer> lambdaPlayers = new HashMap<>();
    private List<LambdaRank> lambdaRanks = new ArrayList<>();

    public int taskID = 0;

    @Override
    public void onEnable()
    {
        instance = this;

        setupFoundationMinecraft(PluginLoader.CONFIG);
        setupFoundationMinecraft(PluginLoader.COMMANDS);
        setupFoundationMinecraft(PluginLoader.LISTENERS);
        setupFoundationMinecraft(PluginLoader.DISCORD);

        setupRepeatingTasks();
    }

    @Override
    public void onDisable()
    {
        SyncManager.clearSync();
        lambdaPlayers.clear();
        lambdaRanks.clear();

        Bukkit.getScheduler().cancelAllTasks();
        discordModule.getSyncChannel().sendMessage("**Server closed!** It should be opened again soon.").queue();

        try
        {
            if(discordModule.getJDA() != null) discordModule.getJDA().shutdown();
        }
        catch (NoClassDefFoundError e)
        {

        }
    }

    void setupRepeatingTasks()
    {
        FoundationMinecraft.getInstance().taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run()
            {
                LambdaRank.loadRanks();
                Config config = FoundationMinecraft.getInstance().getFmcConfig();
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    LambdaPlayer lambdaPlayer = FoundationMinecraft.getInstance().getLambdaPlayers().get(p);
                    lambdaPlayer.setMoney(lambdaPlayer.getMoney() + config.getPlayerPayment());
                    p.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You received your payment (" + config.getPlayerPayment() + " money) for playing!");
                    if(lambdaPlayer.isSiteDirector())
                    {
                        lambdaPlayer.setMoney(lambdaPlayer.getMoney() + config.getSiteDirectorPayment());
                        p.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You received your Site Director payment (" + config.getSiteDirectorPayment() + " money) for playing!");
                    }
                    lambdaPlayer.save();
                }
            }
        }, 0, 20 * 60 * 20);
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
                for(LambdaRank rank : lambdaRanks)
                {
                    System.out.println(rank.toString());
                }
                break;
            case DISCORD:
                discordModule = new DiscordModule();
                discordModule.start();
                break;
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
                getCommand("explosions").setExecutor(new MCmdExplosions());
                getCommand("setupdeptspawn").setExecutor(new MCmdSetupdeptspawn());
                break;
            case LISTENERS:
                getServer().getPluginManager().registerEvents(new OnAsyncPlayerChat(), this);
                getServer().getPluginManager().registerEvents(new OnAsyncPlayerPreLogin(), this);
                getServer().getPluginManager().registerEvents(new OnInventoryClick(), this);
                getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
                getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
                getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
                getServer().getPluginManager().registerEvents(new OnServerPing(), this);
                getServer().getPluginManager().registerEvents(new OnBlockExplode(), this);
                break;
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

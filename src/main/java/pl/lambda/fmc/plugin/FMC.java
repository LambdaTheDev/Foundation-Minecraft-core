package pl.lambda.fmc.plugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.plugin.commands.*;
import pl.lambda.fmc.plugin.listeners.*;
import pl.lambda.fmc.utils.*;

import java.awt.*;
import java.util.HashMap;

public class FMC extends JavaPlugin
{
    public static boolean serverOpened = false;

    public HashMap<Player, LambdaPlayer> lambdaPlayers = new HashMap<>();
    private ConfigAPI configAPI;
    private SyncAPI syncAPI;
    private UsersAPI usersAPI;
    private RoleAPI roleAPI;

    @Override
    public void onEnable()
    {
        setupConfig();
        setupCommands();
        setupListeners();
        setupDiscord();
    }

    @Override
    public void onDisable()
    {
        ConfigAPI cfg = new ConfigAPI();
        DiscordBot.sendNoRankMessage("Server stopped!", "", DiscordBot.jda.getTextChannelById(cfg.getSyncChannelID()), null, Color.RED);
        if(DiscordBot.jda != null)
        {
            DiscordBot.jda.shutdown();
        }
    }

    private void setupCommands()
    {
        getCommand("sync").setExecutor(new SyncCmd());
        getCommand("depts").setExecutor(new DeptsCmd());
        getCommand("chattype").setExecutor(new ChattypeCmd());
        getCommand("money").setExecutor(new MoneyCmd());
        getCommand("pay").setExecutor(new PayCmd());
        getCommand("tp").setExecutor(new TpCmd());
        getCommand("msg").setExecutor(new MsgCmd());
        getCommand("gm").setExecutor(new GmCmd());
        getCommand("spawn").setExecutor(new SpawnCmd());
        getCommand("setspawn").setExecutor(new SetspawnCmd());
    }

    private void setupListeners()
    {
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new DeptsCmd.GUI(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new OnServerPing(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerRespawn(), this);
    }

    private void setupDiscord()
    {
        new Thread(DiscordBot::startBot).start();
    }

    private void setupConfig()
    {
        configAPI = new ConfigAPI();
        syncAPI = new SyncAPI();
        usersAPI = new UsersAPI();
        roleAPI = new RoleAPI();
        configAPI.setup();
        syncAPI.setup();
        usersAPI.setup();
        roleAPI.setup();
    }

    public ConfigAPI getConfigAPI() {
        return configAPI;
    }

    public SyncAPI getSyncAPI() {
        return syncAPI;
    }

    public UsersAPI getUsersAPI() {
        return usersAPI;
    }

    public RoleAPI getRoleAPI()
    {
        return roleAPI;
    }

    public LambdaPlayer getLambdaPlayer(Player p)
    {
        return lambdaPlayers.get(p);
    }
}

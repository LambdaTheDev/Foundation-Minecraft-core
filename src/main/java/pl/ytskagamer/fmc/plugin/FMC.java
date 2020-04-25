package pl.ytskagamer.fmc.plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.plugin.commands.*;
import pl.ytskagamer.fmc.plugin.listeners.*;
import pl.ytskagamer.fmc.utils.ConfigAPI;
import pl.ytskagamer.fmc.utils.SyncAPI;
import pl.ytskagamer.fmc.utils.UsersAPI;

import java.awt.*;

public class FMC extends JavaPlugin
{
    public static boolean serverOpened = false;
    private ConfigAPI configAPI;
    private SyncAPI syncAPI;
    private UsersAPI usersAPI;

    @Override
    public void onEnable()
    {
        setupCommands();
        setupDiscord();
        setupListeners();
        setupConfig();
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
    }

    private void setupListeners()
    {
        getServer().getPluginManager().registerEvents(new OnPlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new DeptsCmd.GUI(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new OnServerPing(), this);
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
        configAPI.setup();
        syncAPI.setup();
        usersAPI.setup();
    }

    public static void error(String message)
    {
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[FMC] " + ChatColor.RED + message);
    }

    public static void warn(String message)
    {
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[FMC] " + ChatColor.GOLD + message);
    }

    public static void info(String message)
    {
        Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "[FMC] " + ChatColor.GREEN + message);
    }
}

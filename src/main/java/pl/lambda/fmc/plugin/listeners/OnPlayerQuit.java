package pl.lambda.fmc.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.lambda.fmc.discord.DiscordBot;

public class OnPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        int onlineplayers = Bukkit.getServer().getOnlinePlayers().size() - 1;
        int maxplayers = Bukkit.getMaxPlayers();

        //departmentApplicationAPI.stopApplication(e.getPlayer().getUniqueId().toString());
        DiscordBot.sendMessageToSyncChannel("**" + e.getPlayer().getName() + "** left server " + onlineplayers + "/" + maxplayers + "!");
        e.setQuitMessage(ChatColor.GOLD + e.getPlayer().getName() + ChatColor.RED + " left server " + onlineplayers + "/" + maxplayers + "!");
    }
}

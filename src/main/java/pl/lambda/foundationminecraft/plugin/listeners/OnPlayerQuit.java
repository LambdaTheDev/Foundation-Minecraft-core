package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class OnPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        FoundationMinecraft.instance.lambdaPlayers.get(e.getPlayer()).saveToFile();
        FoundationMinecraft.instance.lambdaPlayers.remove(e.getPlayer());
        Config config = FoundationMinecraft.instance.getFMCConfig();
        e.setQuitMessage(config.messagePrefix + ChatColor.GREEN + e.getPlayer().getName() + " has left server (" + (Bukkit.getOnlinePlayers().size()-1) + "/" + Bukkit.getMaxPlayers() + ")!");
    }
}

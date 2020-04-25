package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class OnAsyncPlayerPreLogin implements Listener
{
    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e)
    {
        if(!FoundationMinecraft.serverLoaded)
        {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Wait a bit more time, server is being loaded!");
        }
    }
}

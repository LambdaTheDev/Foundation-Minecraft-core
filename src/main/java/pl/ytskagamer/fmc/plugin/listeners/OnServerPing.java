package pl.ytskagamer.fmc.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class OnServerPing implements Listener
{
    @EventHandler
    public void onServerPing(ServerListPingEvent e)
    {
        e.setMotd(ChatColor.RED + "FMC - Join server!");
    }
}

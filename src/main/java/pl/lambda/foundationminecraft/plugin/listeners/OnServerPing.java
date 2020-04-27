package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class OnServerPing implements Listener
{
    @EventHandler
    public void onServerPing(ServerListPingEvent e)
    {
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        e.setMotd(ChatColor.translateAlternateColorCodes('&', config.getMotd()));
    }
}

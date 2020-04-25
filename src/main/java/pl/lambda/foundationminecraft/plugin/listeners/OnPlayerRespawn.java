package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class OnPlayerRespawn implements Listener
{
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        e.getPlayer().teleport(FoundationMinecraft.instance.getFMCConfig().spawnLocation);
    }
}

package pl.lambda.fmc.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import pl.lambda.fmc.utils.ConfigAPI;

public class OnPlayerRespawn implements Listener
{
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        e.getPlayer().teleport(configAPI.getSpawnLocation());
    }
}

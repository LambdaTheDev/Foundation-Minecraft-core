package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class OnPlayerRespawn implements Listener
{
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e)
    {
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        config.reload();
        if((config.getSpawnLocation().getBlockX() == 0 && config.getSpawnLocation().getBlockY() == 0 && config.getSpawnLocation().getBlockZ() == 0))
        {
            e.setRespawnLocation(config.getSpawnLocation());
        }
    }
}

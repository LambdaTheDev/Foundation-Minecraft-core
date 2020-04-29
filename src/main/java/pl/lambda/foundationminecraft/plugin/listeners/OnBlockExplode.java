package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class OnBlockExplode implements Listener
{
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent e)
    {
        if(!FoundationMinecraft.EXPLOSIONS_ALLOWED) e.setCancelled(true);
    }
}

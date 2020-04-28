package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class OnPlayerQuit implements Listener
{
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        FoundationMinecraft.getInstance().getLambdaPlayers().get(e.getPlayer()).save();
        FoundationMinecraft.getInstance().getLambdaPlayers().remove(e.getPlayer());
        e.setQuitMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + e.getPlayer().getName() + " left the game!");
        FoundationMinecraft.getInstance().getDiscordModule().getSyncChannel().sendMessage("**" + e.getPlayer().getName() + "** has left the game!").queue();
    }
}

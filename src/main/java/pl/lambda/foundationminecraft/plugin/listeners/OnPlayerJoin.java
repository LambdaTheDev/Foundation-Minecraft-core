package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;

public class OnPlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        LambdaPlayer lambdaPlayer = LambdaPlayer.getLambdaPlayerByUUID(e.getPlayer().getUniqueId());
        if(lambdaPlayer == null)
        {
            lambdaPlayer = LambdaPlayer.createLambdaPlayer(e.getPlayer());
        }

        lambdaPlayer.save();
        FoundationMinecraft.getInstance().getLambdaPlayers().putIfAbsent(e.getPlayer(), lambdaPlayer);
        e.setJoinMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + e.getPlayer().getName() + " joined the game!");
        FoundationMinecraft.getInstance().getDiscordModule().getSyncChannel().sendMessage("**" + e.getPlayer() + "** has joined the game!").queue();
    }
}

package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.ChatType;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.Utils;

public class OnAsyncPlayerChat implements Listener
{
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
    {
        e.setCancelled(true);

        LPlayer lPlayer = FoundationMinecraft.instance.lambdaPlayers.get(e.getPlayer());
        LRank currentRank = lPlayer.getCurrentRank();
        ChatColor color = Utils.getChatColor(currentRank.getColor().charAt(currentRank.getColor().length() - 1));
        String shortcut = currentRank.getShortcut();
        ChatType type = lPlayer.getChatType();

        String message;
        if(type == ChatType.LOCAL)
        {
            message = ChatColor.GREEN + "(Local) " + color + "[" + shortcut + "] " + ChatColor.GRAY + e.getPlayer().getName() +
                    ChatColor.WHITE + e.getMessage();
            Bukkit.broadcastMessage(message);
            //todo: on request - send message to players in range X.
        }
        else
        {
            message = ChatColor.DARK_PURPLE + "(Global) " + color + "[" + shortcut + "] " + ChatColor.GRAY + e.getPlayer().getName() +
                    ChatColor.WHITE + e.getMessage();

            Bukkit.broadcastMessage(message);
            FoundationMinecraft.instance.discordModule.sendRawMessageToSyncChannel("[Minecraft] " + "[" + shortcut + "] " + "**" + e.getPlayer().getName() + "**: " + e.getMessage());
        }
    }
}

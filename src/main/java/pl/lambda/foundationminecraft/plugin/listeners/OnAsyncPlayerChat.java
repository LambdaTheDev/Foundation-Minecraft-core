package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.enums.ChatType;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

public class OnAsyncPlayerChat implements Listener
{
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
    {
        e.setCancelled(true);
        LambdaPlayer lambdaPlayer = FoundationMinecraft.getInstance().getLambdaPlayers().get(e.getPlayer());

        if(e.getMessage().startsWith("=+debug"))
        {
            e.getPlayer().sendMessage(lambdaPlayer.toString());
            return;
        }

        LambdaRank currentLambdaRank = lambdaPlayer.getCurrentLambdaRank();

        if(currentLambdaRank == null)
        {
            Bukkit.broadcastMessage("[NONE] " + ChatColor.GRAY + e.getPlayer().getName() + ": " + e.getMessage());
            return;
        }

        ChatType chatType = lambdaPlayer.getChatType();
        String chatTypePrefix = ChatColor.GREEN + "(Local) ";

        if(chatType == ChatType.GLOBAL)
        {
            chatTypePrefix = ChatColor.DARK_PURPLE + "(Global) ";
            DiscordModule discordModule = FoundationMinecraft.getInstance().getDiscordModule();
            discordModule.getSyncChannel().sendMessage("[Minecraft] **" + e.getPlayer().getName() + "**: " + e.getMessage()).queue();
        }
        Bukkit.broadcastMessage(chatTypePrefix + currentLambdaRank.getChatColor() + currentLambdaRank.getPrefix() + " " + ChatColor.GRAY + e.getPlayer().getName() + ": " + ChatColor.WHITE + e.getMessage());
    }
}

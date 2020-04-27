package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.enums.ChatType;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

public class OnAsyncPlayerChat implements Listener
{
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e)
    {
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        LambdaPlayer lambdaPlayer = FoundationMinecraft.getInstance().getLambdaPlayers().get(e.getPlayer());
        LambdaRank currentLambdaRank = lambdaPlayer.getCurrentLambdaRank();

        if(currentLambdaRank == null)
        {
            Bukkit.broadcastMessage("[NONE] " + ChatColor.GRAY + e.getPlayer().getName() + ": " + e.getMessage());
            return;
        }

        String prefix = ChatColor.translateAlternateColorCodes('&', currentLambdaRank.getPrefix());
        ChatType chatType = lambdaPlayer.getChatType();

        Bukkit.broadcastMessage(prefix + " " + ChatColor.WHITE + e.getPlayer().getName() + ": " + e.getMessage());
        if(chatType == ChatType.GLOBAL)
        {
            DiscordModule discordModule = FoundationMinecraft.getInstance().getDiscordModule();
            discordModule.getSyncChannel().sendMessage("[Minecraft] **" + e.getPlayer().getName() + "**: " + e.getMessage()).queue();
        }
    }
}

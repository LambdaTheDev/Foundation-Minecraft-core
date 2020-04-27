package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class OnMessageReceived extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(e.getAuthor().isBot()) return;
        if(e.getTextChannel() == FoundationMinecraft.getInstance().getDiscordModule().getSyncChannel())
        {
            Bukkit.broadcastMessage(ChatColor.BLUE + "[Discord] " + ChatColor.GRAY + e.getAuthor().getName() + ": " + ChatColor.WHITE + e.getMessage().getContentRaw());
        }
    }
}

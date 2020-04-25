package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class OnMessageReceived extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        Config config = FoundationMinecraft.instance.getFMCConfig();

        if(!e.getTextChannel().getId().equalsIgnoreCase(config.syncChannelID)) return;

        if(e.getAuthor().isBot()) return;
        String message = e.getMessage().getContentRaw();
        Bukkit.broadcastMessage(ChatColor.BLUE + "[Discord] " + ChatColor.GRAY + e.getAuthor().getName() + ChatColor.RESET + ": " + message);
    }
}

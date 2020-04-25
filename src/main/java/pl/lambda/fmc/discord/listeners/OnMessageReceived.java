package pl.lambda.fmc.discord.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import pl.lambda.fmc.utils.ConfigAPI;

public class OnMessageReceived extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        String syncChannelID = configAPI.getSyncChannelID();

        if(syncChannelID == null) return;

        if(e.getAuthor().isBot()) return;

        if(e.getChannel().getId().equalsIgnoreCase(syncChannelID))
        {
            if(e.getMessage().getContentRaw().startsWith("[Minecraft]"))
            {
                return;
            }
            Bukkit.broadcastMessage("[Discord] " + e.getAuthor().getName() + ": " + e.getMessage().getContentRaw());
        }
    }
}

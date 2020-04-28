package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;

public class DCmdRefreshroles extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdRefreshroles(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase(config.getDiscordBotPrefix() + "refreshroles"))
        {
            e.getTextChannel().sendMessage("**Error!** Command is deprecated and will be removed in next update.").queue();
        }
    }
}

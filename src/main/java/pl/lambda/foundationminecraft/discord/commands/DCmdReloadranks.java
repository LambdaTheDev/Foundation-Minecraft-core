package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

public class DCmdReloadranks extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdReloadranks(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        if(args[0].equalsIgnoreCase(config.getDiscordBotPrefix() + "reloadranks"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**Error!** You don't have permission to use that command!").queue();
                return;
            }

            LambdaRank.loadRanks();
            e.getTextChannel().sendMessage("**Success!** All roles refreshed successfully.").queue();
        }
    }
}

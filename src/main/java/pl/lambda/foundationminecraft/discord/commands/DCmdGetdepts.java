package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.HashMap;
import java.util.List;

public class DCmdGetdepts extends ListenerAdapter
{
    private DiscordModule discordModule;
    public DCmdGetdepts(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase(config.getDiscordBotPrefix() + "getdepts"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**No permission!** You don't have permission to use that command!").queue();
                return;
            }

            List<LambdaRank> lambdaRanks = FoundationMinecraft.getInstance().getLambdaRanks();
            e.getTextChannel().sendMessage("**List of registered ranks:**").queue();
            for(LambdaRank rank : lambdaRanks)
            {
                String message = "__" + rank.getName() + "__\nDiscord ID: " + rank.getDiscordID() + "\nColor: " + rank.getColor() + "\nPrefix: " + rank.getPrefix();
                e.getTextChannel().sendMessage(message).queue();
            }
        }
    }
}

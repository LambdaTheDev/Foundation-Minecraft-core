package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

public class DCmdDeletedept extends ListenerAdapter
{
    private DiscordModule discordModule;
    public DCmdDeletedept(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase(config.getDiscordBotPrefix() + "deletedept"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**No permission!** You don't have permission to use that command!").queue();
                return;
            }

            if(!(args.length >= 2))
            {
                e.getTextChannel().sendMessage("**Error!** Wrong usage. Correct: " + config.getDiscordBotPrefix() + "deletedept <name...>!").queue();
                return;
            }

            String name = Utils.argsBuilder(args, 2);
            LambdaRank rank = LambdaRank.getRankByName(name);
            if(rank == null)
            {
                e.getTextChannel().sendMessage("**Error!** That ran not exist (system is case-sensitive)!").queue();
                return;
            }

            rank.deleteRank();
            e.getTextChannel().sendMessage("**Success!** Rank deleted successfully!").queue();
        }
    }
}

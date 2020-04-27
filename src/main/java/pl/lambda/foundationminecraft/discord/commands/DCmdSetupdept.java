package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.List;

public class DCmdSetupdept extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdSetupdept(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        if(args[0].equalsIgnoreCase("setupdept"))
        {
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                e.getTextChannel().sendMessage("**Error!** You don't have permission to use that command!").queue();
                return;
            }

            if(!(args.length >= 5))
            {
                e.getTextChannel().sendMessage("**Error!** Wrong usage. Correct: " + config.getDiscordBotPrefix() + "setupdept <in-game shortcut> <HEX color> <ping department role> <department name...>!").queue();
                return;
            }

            String shortcut = args[1];
            String colorCode = args[2];
            String roleID;
            String name = Utils.argsBuilder(args, 5);

            if(LambdaRank.getRankByName(name) != null)
            {
                e.getTextChannel().sendMessage("**Error!** Rank with that name exist!").queue();
                return;
            }

            if(!Utils.verifyHEX(colorCode))
            {
                e.getTextChannel().sendMessage("**Error!** HEX color code is invalid!").queue();
                return;
            }

            Role mentionedRole = e.getMessage().getMentionedRoles().get(0);
            roleID = mentionedRole.getId();

            LambdaRank.createLambdaRank(roleID, name, shortcut, colorCode);
            e.getTextChannel().sendMessage("**Success!** New rank with name " + name + " created successfully!").queue();
        }
    }
}

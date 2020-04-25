package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.Utils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DSetupdeptCmd extends ListenerAdapter implements ILDiscordCommand
{
    Config config = FoundationMinecraft.instance.getFMCConfig();
    TextChannel channel;

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = config.botPrefix;

        if (args[0].equalsIgnoreCase(prefix + "setupdept"))
        {
            if(e.getAuthor().isBot()) return;

            if (FoundationMinecraft.checkIfDeprecated(this.getClass()) != null)
            {
                FoundationMinecraft.instance.discordModule.sendMessageToChannel("Hey!", Color.yellow, "This command is deprecated!", FoundationMinecraft.checkIfDeprecated(this.getClass()), e.getTextChannel());
            }

            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                getNoPermissionMessage();
                return;
            }

            DiscordModule discordModule = FoundationMinecraft.instance.discordModule;
            channel = e.getTextChannel();

            if(args.length < 4)
            {
                getUsageMessage(prefix + "setupdept <shortcut> <hex color> <ping assigned Discord role> <rank name...>");
                return;
            }

            if(args[1].length() > 5)
            {
                discordModule.sendMessageToChannel("Error!", Color.red, "Shortcut too long!", "Shortcut can't be longer than 5 characters.", channel);
                return;
            }

            if(!Utils.validColor(args[2]))
            {
                discordModule.sendMessageToChannel("Error!", Color.red, "Wrong color argument!", "You need to use Minecraft HEX color code (without §) - list: https://minecraft.gamepedia.com/Formatting_codes.", channel);
                return;
            }
            args[3] = args[3].replace("<", "").replace(">", "").replace("&", "").replace("@", "");
//            args[3] = String.valueOf(Integer.parseInt(args[3]));

            String name = Utils.stringBuilder(args, 4);

            if(name.length() > 25)
            {
                discordModule.sendMessageToChannel("Error!", Color.red, "Name is too long!", "Name can't be longer than 25 characters!", channel);
                return;
            }

            boolean found = false;
            LRank.loadRanks();
            HashMap<String, LRank> ranks = FoundationMinecraft.instance.ranks;
            for(Map.Entry<String, LRank> rank : ranks.entrySet())
            {
                if(rank.getValue().getName().equalsIgnoreCase(name))
                {
                    found = true;
                    break;
                }

                if(rank.getValue().getDiscordID().equalsIgnoreCase(args[3]))
                {
                    found = true;
                    break;
                }
            }

            if(found)
            {
                discordModule.sendMessageToChannel("Error!", Color.red, "Role exist!", "Role with that Discord role or name exist!", channel);
                return;
            }

            LRank.createRank(name, args[1], args[3], args[2]);
            discordModule.sendMessageToChannel("Success!", Color.green, "Role created successfully!", "New role has been created and is now registered/", channel);
        }
    }

    @Override
    public String getUsageMessage(String usage)
    {
        FoundationMinecraft.instance.discordModule.sendUsageMessage(usage, channel);
        return null;
    }

    @Override
    public String getNoPermissionMessage()
    {
        FoundationMinecraft.instance.discordModule.sendMessageToChannel("No permission!", Color.red, "You can't use that command!", "", channel);
        return null;
    }

    @Override
    public boolean checkIfDeprecated() {
        return false;
    }
}

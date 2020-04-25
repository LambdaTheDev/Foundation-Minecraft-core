package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.ConfigurationSection;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.datastorage.RankDataStorage;

import java.awt.*;

public class DDeletedeptCmd extends ListenerAdapter implements ILDiscordCommand
{
    Config config = FoundationMinecraft.instance.getFMCConfig();
    TextChannel channel;

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = config.botPrefix;

        if(args[0].equalsIgnoreCase(prefix + "deletedept"))
        {
            if(e.getAuthor().isBot()) return;

            channel = e.getTextChannel();
            checkIfDeprecated();

            if(args.length >= 2)
            {
                RankDataStorage rankDataStorage = FoundationMinecraft.instance.getRankDataStorage();
                String name = Utils.stringBuilder(args, 1);

                ConfigurationSection lookForLambdaID = rankDataStorage.getData().getConfigurationSection("ranks");
                LRank lRank = null;
                for(String lambdaID : lookForLambdaID.getKeys(false))
                {
                    if(rankDataStorage.getData().getString("ranks." + lambdaID + ".name").equalsIgnoreCase(name))
                    {
                        lRank = FoundationMinecraft.instance.ranks.get(lambdaID);
                    }
                }

                if(lRank == null)
                {
                    FoundationMinecraft.instance.discordModule.sendMessageToChannel("Error!", Color.red, "Rank not exists!", "That rank not exist! Remember - system is case-sensitive!", channel);
                    return;
                }

                rankDataStorage.getData().set("ranks." + lRank.getLambdaRankID(), null);
                rankDataStorage.save();
                LRank.loadRanks();

                FoundationMinecraft.instance.discordModule.sendMessageToChannel("Success!", Color.green, "Rank deleted successfully!", "You deleted rank " + lRank.getName() + " successfully!", channel);
            }
            else
            {
                getUsageMessage(prefix + "deletedept <name>");
            }
        }
    }

    @Override
    public String getUsageMessage(String usage)
    {
        FoundationMinecraft.instance.discordModule.sendMessageToChannel("Wrong usage!", Color.red, "Correct usage:", usage, channel);
        return null;
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }

    @Override
    public boolean checkIfDeprecated()
    {
        if(FoundationMinecraft.checkIfDeprecated(this.getClass()) != null)
        {
            FoundationMinecraft.instance.discordModule.sendMessageToChannel("Hey!", Color.yellow, "Command deprecated", FoundationMinecraft.checkIfDeprecated(this.getClass()), channel);
            return true;
        }
        return false;
    }
}

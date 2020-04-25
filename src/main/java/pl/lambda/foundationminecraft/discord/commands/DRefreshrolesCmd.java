package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LDeprecated;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.LRank;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LDeprecated(willBeDeleted = true, newUsage = "Use new command: +reload to refresh all roles. That command may not work!")
public class DRefreshrolesCmd extends ListenerAdapter implements ILDiscordCommand
{
    Config config = FoundationMinecraft.instance.getFMCConfig();

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = config.botPrefix;

        if(args[0].equalsIgnoreCase(prefix + "refreshroles"))
        {
            if(e.getAuthor().isBot()) return;

            if(FoundationMinecraft.checkIfDeprecated(this.getClass()) != null)
            {
                FoundationMinecraft.instance.discordModule.sendMessageToChannel("Hey!", Color.yellow, "This command is deprecated!", FoundationMinecraft.checkIfDeprecated(this.getClass()), e.getTextChannel());
            }

            String discordID = e.getAuthor().getId();
            HashMap<Player, LPlayer> lambdaPlayers = FoundationMinecraft.instance.lambdaPlayers;
            LPlayer requestedPlayer = null;

            for(Map.Entry<Player, LPlayer> lPlayer : lambdaPlayers.entrySet())
            {
                String check = lPlayer.getValue().getDiscordID();
                if(check.equals(discordID))
                {
                    requestedPlayer = lPlayer.getValue();
                    break;
                }
            }

            if(requestedPlayer == null)
            {
                FoundationMinecraft.instance.discordModule.sendMessageToChannel("Error!", Color.red, "Failed to refresh roles!", "You aren't currently online on server! Log in to game, and type command again.", e.getTextChannel());
                return;
            }

            Member m = e.getMember();
            if(m == null)
            {
                FoundationMinecraft.instance.discordModule.sendMessageToChannel("Error!", Color.red, "Failed to refresh roles!", "Unexpected error happened. Try again.", e.getTextChannel());
                return;
            }

            List<Role> roles = m.getRoles();
            List<LRank> updatedRanks = new ArrayList<>();
            LRank.loadRanks();

            for(Role role : roles)
            {
                String roleID = role.getId();
                HashMap<String, LRank> ranks = FoundationMinecraft.instance.ranks;
                for(Map.Entry<String, LRank> rank : ranks.entrySet())
                {
                    if(rank.getValue().getDiscordID().equals(roleID))
                    {
                        updatedRanks.add(rank.getValue());
                    }
                }
            }

            requestedPlayer.setLambdaRanks(updatedRanks);
            requestedPlayer.saveToFile();
            FoundationMinecraft.instance.discordModule.sendMessageToChannel("Success!", Color.green, "Roles updated successfully.", "Your roles in-game have been updated successfully.", e.getTextChannel());
        }
    }

    @Override
    public String getUsageMessage(String usage) {
        return null;
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }

    @Override
    public boolean checkIfDeprecated() {
        return false;
    }
}

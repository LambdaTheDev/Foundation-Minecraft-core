package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.ArrayList;
import java.util.List;

public class DCmdRefreshroles extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdRefreshroles(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        if(args[0].equalsIgnoreCase("refreshroles"))
        {
            e.getTextChannel().sendMessage("**Hey you!**\nThis command is deprecated and it may not work correctly! Please avoid using that command, roles should update when you join server (and synced your account with Discord).").queue();
            String discordID = e.getAuthor().getId();
            LambdaPlayer lambdaPlayer = LambdaPlayer.getLambdaPlayerByDiscord(discordID);
            if(lambdaPlayer == null)
            {
                e.getTextChannel().sendMessage("**Error!** There is no registered player synced with your Discord!").queue();
                return;
            }

            Member member = e.getMember();
            if(member == null)
            {
                e.getTextChannel().sendMessage("**Error!** Unknown error, if that appears too often, contact staff.").queue();
                return;
            }
            List<Role> memberRoles = member.getRoles();
            List<LambdaRank> lambdaRanks = FoundationMinecraft.getInstance().getLambdaRanks();
            List<LambdaRank> updatedLambdaRanks = new ArrayList<>();
            for(Role role : memberRoles)
            {
                String roleID = role.getId();
                for(LambdaRank lambdaRank : lambdaRanks)
                {
                    if(lambdaRank.getDiscordID().equals(roleID))
                    {
                        updatedLambdaRanks.add(lambdaRank);
                    }
                }
            }

            lambdaPlayer.setLambdaRanks(updatedLambdaRanks);
            e.getTextChannel().sendMessage("**Success!** Your roles should be updated!").queue();
        }
    }
}

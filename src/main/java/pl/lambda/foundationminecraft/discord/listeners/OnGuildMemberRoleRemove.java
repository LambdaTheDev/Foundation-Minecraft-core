package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.List;

public class OnGuildMemberRoleRemove extends ListenerAdapter
{
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e)
    {
        String discordID = e.getUser().getId();
        LambdaPlayer lambdaPlayer = LambdaPlayer.getLambdaPlayerByDiscord(discordID);
        if(lambdaPlayer == null) return;

        List<LambdaRank> updatedRanks = lambdaPlayer.getLambdaRanks();
        for(Role role : e.getRoles())
        {
            String roleId = role.getId();
            LambdaRank lambdaRank = LambdaRank.getRankByDiscordID(roleId);
            if(lambdaRank == null) return;
            if(lambdaPlayer.getLambdaRanks().contains(lambdaRank))
            {
                updatedRanks.remove(lambdaRank);
            }
        }

        lambdaPlayer.setLambdaRanks(updatedRanks);
        lambdaPlayer.save();
    }
}

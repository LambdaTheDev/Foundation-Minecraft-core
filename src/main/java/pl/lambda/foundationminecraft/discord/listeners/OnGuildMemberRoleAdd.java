package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.configuration.ConfigurationSection;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.datastorage.PlayerDataStorage;
import pl.lambda.foundationminecraft.utils.datastorage.RankDataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OnGuildMemberRoleAdd extends ListenerAdapter
{
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent e)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        HashMap<String, LRank> registeredRanks = FoundationMinecraft.instance.ranks;
        List<LRank> lRanks = new ArrayList<>(registeredRanks.values());
        List<Role> roles = e.getRoles();

        String memberID = e.getMember().getId();
        String lambdaPlayerID = null;
        ConfigurationSection getPlayer = playerDataStorage.getData().getConfigurationSection("players");

        for(String lambdaID : getPlayer.getKeys(false))
        {
            if(playerDataStorage.getData().getString("players." + lambdaID + ".discordid").equals(memberID))
            {
                lambdaPlayerID = lambdaID;
            }
        }

        if(lambdaPlayerID != null)
        {
            for(Role role : roles)
            {
                String roleID = role.getId();
                for(LRank lRank : lRanks)
                {
                    if(lRank.getDiscordID().equals(roleID))
                    {
                        List<String> ranks = playerDataStorage.getData().getStringList("players." + lambdaPlayerID + ".lambdaRanks");
                        ranks.add(lRank.getLambdaRankID());
                        playerDataStorage.getData().set("players." + lambdaPlayerID + ".lambdaRanks", ranks);
                    }
                }
            }
        }
    }
}

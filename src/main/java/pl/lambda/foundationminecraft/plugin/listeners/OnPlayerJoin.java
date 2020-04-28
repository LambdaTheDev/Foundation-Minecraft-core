package pl.lambda.foundationminecraft.plugin.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.ArrayList;
import java.util.List;

public class OnPlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        LambdaPlayer lambdaPlayer = LambdaPlayer.getLambdaPlayerByUUID(e.getPlayer().getUniqueId());
        if(lambdaPlayer == null)
        {
            lambdaPlayer = LambdaPlayer.createLambdaPlayer(e.getPlayer());
        }

        if(lambdaPlayer.getDiscordID() != null)
        {
            List<LambdaRank> updatedRanks = new ArrayList<>();
            Member member = FoundationMinecraft.getInstance().getDiscordModule().getGuild().getMemberById(lambdaPlayer.getDiscordID());
            if(member != null)
            {
                List<Role> memberRoles = member.getRoles();
                for(Role role : memberRoles)
                {
                    String roleID = role.getId();
                    LambdaRank lambdaRank = LambdaRank.getRankByDiscordID(roleID);
                    if(lambdaRank == null) continue;
                    for(LambdaRank loopedLambdaRank : FoundationMinecraft.getInstance().getLambdaRanks())
                    {
                        if(loopedLambdaRank.equals(lambdaRank))
                        {
                            updatedRanks.add(lambdaRank);
                        }
                    }
                }

                lambdaPlayer.setLambdaRanks(updatedRanks);
            }
        }

        lambdaPlayer.save();
        FoundationMinecraft.getInstance().getLambdaPlayers().putIfAbsent(e.getPlayer(), lambdaPlayer);
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        e.setJoinMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + e.getPlayer().getName() + " joined the game!");
        FoundationMinecraft.getInstance().getDiscordModule().getSyncChannel().sendMessage("**" + e.getPlayer().getName() + "** has joined the game!").queue();
    }
}

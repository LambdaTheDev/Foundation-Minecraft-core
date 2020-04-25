package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.datastorage.PlayerDataStorage;

import java.util.UUID;

public class OnPlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        String nickname = e.getPlayer().getName();
        Player p = e.getPlayer();

        String loopedLambdaID = null;
        ConfigurationSection check = playerDataStorage.getData().getConfigurationSection("players");
        if(check != null)
        {
            for(String lambdaID : check.getKeys(false))
            {
                if(playerDataStorage.getData().getString("players." + lambdaID + ".nickname").equals(nickname))
                {
                    loopedLambdaID = lambdaID;
                    break;
                }
            }
        }

        LPlayer lPlayer = null;

        if(loopedLambdaID != null)
        {
            //played
            lPlayer = LPlayer.getLambdaPlayerByLambdaID(loopedLambdaID);
        }

        if(lPlayer == null)
        {
            lPlayer = LPlayer.createLambdaPlayer(nickname);
        }

        if(!p.hasPlayedBefore()) p.teleport(FoundationMinecraft.instance.getFMCConfig().spawnLocation);
        lPlayer.saveToFile();
        FoundationMinecraft.instance.lambdaPlayers.put(e.getPlayer(), lPlayer);

        Config config = FoundationMinecraft.instance.getFMCConfig();
        e.setJoinMessage(config.messagePrefix + ChatColor.GREEN + p.getName() + " has joined server (" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ")!");
    }
}

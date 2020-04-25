package pl.ytskagamer.fmc.plugin.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.plugin.FMC;
import pl.ytskagamer.fmc.utils.RoleAPI;
import pl.ytskagamer.fmc.utils.ConfigAPI;
import pl.ytskagamer.fmc.utils.UsersAPI;

import java.util.List;

public class OnPlayerJoin implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        RoleAPI roleAPI = new RoleAPI();
        UsersAPI usersAPI = new UsersAPI();
        ConfigAPI configAPI = new ConfigAPI();

        if(!FMC.serverOpened)
        {
            e.getPlayer().kickPlayer(ChatColor.RED + "Serwer almost ready! Wait second.");
            return;
        }

        int onlineplayers = Bukkit.getServer().getOnlinePlayers().size();
        int maxplayers = Bukkit.getMaxPlayers();

        //DiscordBot.sendMessageToSyncChannel("**" + e.getPlayer().getName() + "** joined the server " + onlineplayers + "/" + maxplayers + "!");
        e.setJoinMessage(ChatColor.GOLD + e.getPlayer().getName() + ChatColor.GREEN + " joined the server " + onlineplayers + "/" + maxplayers + "!");

        Player p = e.getPlayer();

        p.setPlayerListName(ChatColor.DARK_RED + " [" + ChatColor.RED + "REDACTED" + ChatColor.DARK_RED + "]");
        usersAPI.setCurrentRole(p.getUniqueId().toString(), "none");

        if(usersAPI.create(p.getUniqueId().toString(), p.getName()))
        {
            p.sendMessage(ChatColor.GREEN + "Your account created succesfully! To finish sync, go to Discord and type command: " + configAPI.getDiscordPrefix() + "sync " + p.getName() + "!");
        }
        else
        {
            if(usersAPI.getDiscordID(p.getUniqueId().toString()).equalsIgnoreCase("none"))
            {
                p.sendMessage(ChatColor.RED + "Account not synced with Discord! To do it, type Discord command: " + configAPI.getDiscordPrefix() + "sync " + p.getName() + "! Without that you can't play here!");
                return;
            }
        }

        String uuid = p.getUniqueId().toString();
        usersAPI.clearRoles(uuid);
        User u = DiscordBot.jda.getUserById(usersAPI.getDiscordID(uuid));
        if(u == null) return;

        List<Role> list = DiscordBot.jda.getGuildById(configAPI.getDiscordServerId()).getMember(u).getRoles();
        for(Role role : list)
        {
            String roleid = role.getId();
            String classid = roleAPI.getRoleUUIDByDiscord(roleid);
            if(!(classid == null))
            {
                usersAPI.addRoles(uuid, classid);
            }
        }
    }
}

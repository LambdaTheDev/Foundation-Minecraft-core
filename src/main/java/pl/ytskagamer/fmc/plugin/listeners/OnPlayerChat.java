package pl.ytskagamer.fmc.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.utils.RoleAPI;
import pl.ytskagamer.fmc.utils.UsersAPI;

public class OnPlayerChat implements Listener
{
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e)
    {
        UsersAPI usersAPI = new UsersAPI();
        RoleAPI roleAPI = new RoleAPI();
        Player p = e.getPlayer();

        String classid = usersAPI.getCurrentRole(p.getUniqueId().toString());
        char color = roleAPI.getColor(classid);
        String shortcut = roleAPI.getShortcut(classid);

        if(classid == null)
        {
            p.sendMessage(ChatColor.RED + "To use chat, you need to choose class (/depts)!");
            e.setCancelled(true);
            return;
        }

        if(shortcut == null)
        {
            p.sendMessage(ChatColor.RED + "Something gone wrong!");
            System.out.println(color + ";" + classid + ";" + shortcut);
            e.setCancelled(true);
            return;
        }
        if(usersAPI.getChat(p.getUniqueId().toString())) DiscordBot.sendMessageToSyncChannel("**" + e.getPlayer().getName() + "**: " + e.getMessage());
        Bukkit.broadcastMessage(getColor(color) + p.getName() + " [" + shortcut + "]" + ChatColor.WHITE + ": " + e.getMessage());
        e.setCancelled(true);
    }

    private ChatColor getColor(char color)
    {
        switch (color)
        {
            case '0':
                return ChatColor.BLACK;
            case '1':
                return ChatColor.DARK_BLUE;
            case '2':
                return ChatColor.DARK_GREEN;
            case '3':
                return ChatColor.DARK_AQUA;
            case '4':
                return ChatColor.DARK_RED;
            case '5':
                return ChatColor.DARK_PURPLE;
            case '6':
                return ChatColor.GOLD;
            case '7':
                return ChatColor.GRAY;
            case '8':
                return ChatColor.DARK_GRAY;
            case '9':
                return ChatColor.BLUE;
            case 'a':
                return ChatColor.GREEN;
            case 'b':
                return ChatColor.AQUA;
            case 'c':
                return ChatColor.RED;
            case 'd':
                return ChatColor.LIGHT_PURPLE;
            case 'e':
                return ChatColor.YELLOW;
            case 'f':
                return ChatColor.WHITE;
            default:
                System.out.println("Default response");
                return ChatColor.WHITE;
        }
    }
}

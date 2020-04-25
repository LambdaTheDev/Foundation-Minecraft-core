package pl.lambda.fmc.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.UsersAPI;

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

        String chatType = ChatColor.GREEN + "(Local) " + ChatColor.RESET;
        if(usersAPI.getChat(p.getUniqueId().toString()))
        {
            chatType = ChatColor.DARK_PURPLE + "(Global) " + ChatColor.RESET;
            DiscordBot.sendMessageToSyncChannel("**" + e.getPlayer().getName() + "**: " + e.getMessage());
        }

        Bukkit.broadcastMessage(chatType + getColor(color) + "[" + shortcut + "] " + ChatColor.GRAY + p.getName() + ChatColor.WHITE + ": " + e.getMessage());
        e.setCancelled(true);
    }

    //todo prefix with chat glob or loc, and Discrod sync

    public static ChatColor getColor(char color)
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

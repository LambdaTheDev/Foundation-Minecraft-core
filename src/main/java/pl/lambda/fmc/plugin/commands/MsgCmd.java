package pl.lambda.fmc.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.plugin.listeners.OnPlayerChat;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.UsersAPI;
import pl.lambda.fmc.utils.Utils;

public class MsgCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("msg")) return false;

        if(!(sender instanceof Player)) return false;
        UsersAPI usersAPI = new UsersAPI();
        RoleAPI roleAPI = new RoleAPI();
        Player p = (Player) sender;

        if(args.length > 1)
        {
            Player target = Bukkit.getPlayer(args[0]);
            if(target == null)
            {
                p.sendMessage(ChatColor.RED + "Player not online!");
                return false;
            }

            String message = Utils.stringBuilder(args, 1);

            String classid = usersAPI.getCurrentRole(p.getUniqueId().toString());
            if(classid == null)
            {
                p.sendMessage(ChatColor.RED + "You have to choose role before sending messages (/depts)!");
                return false;
            }

            char color = roleAPI.getColor(classid);
            String shortcut = roleAPI.getShortcut(classid);
            ChatColor chatColor = OnPlayerChat.getColor(color);

            String formattedMessage = ChatColor.GOLD + "(" + ChatColor.GREEN + "MSG" + ChatColor.GOLD + ") "
                    + chatColor + "[" + shortcut + "] " + ChatColor.GRAY + p.getName() + ": " + ChatColor.WHITE + message;

            target.sendMessage(formattedMessage);
            for(Player findOp : Bukkit.getOnlinePlayers())
            {
                if(findOp == target) continue;

                if(findOp.isOp())
                {
                    findOp.sendMessage(formattedMessage);
                }
            }

            p.sendMessage(ChatColor.GREEN + "You ---> " + target.getName() + ChatColor.GRAY + ": " + message);
            return true;
        }
        else
        {
            p.sendMessage(ChatColor.RED + "Usage: /msg <nick> <message...>");
            return false;
        }
    }
}

package pl.lambda.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.utils.UsersAPI;

public class ChattypeCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(command.getName().equalsIgnoreCase("chattype"))
        {
            if(!(sender instanceof Player)) return false;
            Player p = (Player) sender;
            UsersAPI api = new UsersAPI();

            boolean chat;
            api.setChat(p.getUniqueId().toString(), chat = !api.getChat(p.getUniqueId().toString()));

            if(chat)
            {
                p.sendMessage(ChatColor.GREEN + "You changed your chat to: " + ChatColor.GOLD + "global" + ChatColor.GREEN + "!");
                return true;
            }
            p.sendMessage(ChatColor.GREEN + "You changed your chat to: " + ChatColor.GOLD + "local" + ChatColor.GREEN + "!");
        }
        return false;
    }
}

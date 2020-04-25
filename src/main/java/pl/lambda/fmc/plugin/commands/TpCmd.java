package pl.lambda.fmc.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("tp")) return false;

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        if(p.isOp())
        {
            if(args.length == 1)
            {
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null)
                {
                    p.sendMessage(ChatColor.RED + "Player not online!");
                    return false;
                }
                p.teleport(target.getLocation());
                p.sendMessage(ChatColor.GREEN + "Teleported to " + target.getName() + " succesfully!");
                return true;
            }
            else
            {
                p.sendMessage(ChatColor.RED + "Usage: /tp <nick>!");
            }
        }
        else
        {
            p.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
            return false;
        }

        return false;
    }
}

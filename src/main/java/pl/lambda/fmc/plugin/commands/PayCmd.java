package pl.lambda.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.utils.UsersAPI;

public class PayCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("pay")) return false;

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        UsersAPI usersAPI = new UsersAPI();

        if(args.length == 2)
        {
            String username = args[0];
            int ammount = Integer.parseInt(args[1]);
            int currentMoney = usersAPI.getMoney(p.getUniqueId().toString());

            System.out.println("MONEY IN GAME: amount - " + ammount + " current - " + currentMoney);

            if(ammount <= currentMoney)
            {
                usersAPI.setMoney(p.getUniqueId().toString(), (usersAPI.getMoney(p.getUniqueId().toString()) - ammount));
                usersAPI.setMoney(usersAPI.getUserUUIDByNick(username), (usersAPI.getMoney(usersAPI.getUserUUIDByNick(username)) + ammount));
                p.sendMessage(ChatColor.GREEN + "" + ammount + "$ shared successfully to: " + username + "!");
                return true;
            }
            else
            {
                p.sendMessage(ChatColor.RED + "You can't afford for that!");
                return false;
            }
        }
        else
        {
            p.sendMessage(ChatColor.RED + "Usage: /pay <nickname> <amount>!");
            return false;
        }

    }
}

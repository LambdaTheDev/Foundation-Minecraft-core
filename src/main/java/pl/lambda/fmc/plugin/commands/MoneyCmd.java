package pl.lambda.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.utils.UsersAPI;

public class MoneyCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("money")) return false;

        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        UsersAPI usersAPI = new UsersAPI();

        int currentMoney = usersAPI.getMoney(p.getUniqueId().toString());
        p.sendMessage(ChatColor.GREEN + "Your current balance: " + currentMoney + "! Don't waste them.");

        return false;
    }
}

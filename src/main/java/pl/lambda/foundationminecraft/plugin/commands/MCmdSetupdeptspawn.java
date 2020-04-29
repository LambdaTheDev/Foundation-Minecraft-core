package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

public class MCmdSetupdeptspawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("setupdeptspawn")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        if(!sender.isOp())
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.DARK_RED + "You don't have permission to use that command!");
            return false;
        }

        if(args.length == 0)
        {
            sender.sendMessage(FoundationMinecraft.getUsage("/setupdeptspawn <name...>"));
            return false;
        }

        String name = Utils.argsBuilder(args, 1);
        if(LambdaRank.getRankByName(name) == null)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Rank with that name not exist!");
            return false;
        }

        LambdaRank rank = LambdaRank.getRankByName(name);
        rank.setSpawnLocation(((Player) sender).getLocation());
        rank.save();

        LambdaRank.loadRanks();
        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Spawn of " + name + " has been set to your location");

        return true;
    }
}

package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class MCmdKeycard implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("keycard")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        String commandItem;

        int clearance = FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).getClearance();
        switch (clearance)
        {
            case 1:
                commandItem = "scp:level1card";
                break;
            case 2:
                commandItem = "scp:level2card";
                break;
            case 3:
                commandItem = "scp:level3card";
                break;
            case 4:
                commandItem = "scp:level4card";
                break;
            case 5:
                commandItem = "scp:level5card";
                break;
            default:
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't receive any keycards!");
                return false;
        }

        Bukkit.dispatchCommand(sender, "give " + sender.getName() + " " + commandItem + " 1");
        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You received your keycard successfully!");
        return true;
    }
}

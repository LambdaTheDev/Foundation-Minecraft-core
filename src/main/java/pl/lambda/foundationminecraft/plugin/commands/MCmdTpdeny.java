package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class MCmdTpdeny implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("tpdeny")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Player target = FoundationMinecraft.getInstance().getTeleportRequests().getOrDefault((Player) sender, null);
        if(target == null)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You have no teleport requests pending!");
            return false;
        }

        FoundationMinecraft.getInstance().getTeleportRequests().remove((Player) sender);
        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You have denied " + target.getName() + "'s teleport request!");
        target.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Your teleport request to: " + sender.getName() + " has been denied!");

        return false;
    }
}

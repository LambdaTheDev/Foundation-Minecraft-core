package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class MCmdExplosions implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equalsIgnoreCase("explosions")) return false;

        if(!sender.isOp())
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.DARK_RED + "You don't have permission to use that command!");
            return false;
        }

        if(FoundationMinecraft.EXPLOSIONS_ALLOWED)
        {
            FoundationMinecraft.EXPLOSIONS_ALLOWED = false;
            Bukkit.broadcastMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Explosions disabled by: " + sender.getName() + "!");
        }
        else
        {
            FoundationMinecraft.EXPLOSIONS_ALLOWED = true;
            Bukkit.broadcastMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Explosions enabled by: " + sender.getName() + "!");

        }

        return false;
    }
}

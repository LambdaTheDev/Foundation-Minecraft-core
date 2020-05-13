package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Utils;

public class MCmdBroadcast implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("broadcast")) return false;

        if(!sender.isOp())
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.DARK_RED + "You don't have permission to use that command!");
            return false;
        }

        if(args.length == 0)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Incorrect usage! Correct: /broadcast <message>!");
            return false;
        }

        String message = Utils.argsBuilder(args, 1);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Announcement: " + ChatColor.GOLD + message);
        Bukkit.broadcastMessage(" ");

        return false;
    }
}

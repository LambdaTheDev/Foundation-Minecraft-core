package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class MCmdSpawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("spawn")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Config config = FoundationMinecraft.getInstance().getFmcConfig().buildObject();
        if(config.getSpawnLocation().getBlockX() == 0 && config.getSpawnLocation().getBlockY() == 0 && config.getSpawnLocation().getBlockZ() == 0)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Spawn point is not set! Contact staff to solve that problem!");
            return false;
        }

        ((Player) sender).teleport(config.getSpawnLocation());
        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Teleported to spawn successfully!");

        return false;
    }
}

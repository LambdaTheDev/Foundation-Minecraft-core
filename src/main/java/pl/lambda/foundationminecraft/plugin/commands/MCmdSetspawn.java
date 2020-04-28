package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class MCmdSetspawn implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("setspawn")) return false;

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


        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        Location location = ((Player) sender).getLocation();

        config.setSpawnLocation(location);
        config.saveConfigObjectToFile();

        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Spawn location set to place, where you are staying!");
        return false;
    }
}

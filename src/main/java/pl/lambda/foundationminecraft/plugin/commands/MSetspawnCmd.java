package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

public class MSetspawnCmd implements CommandExecutor, ILMinecraftCommand
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

        Config config = FoundationMinecraft.instance.getFMCConfig();

        if(!sender.isOp())
        {
            sender.sendMessage(config.messagePrefix + getNoPermissionMessage());
            return false;
        }

        Location l = ((Player) sender).getLocation();
        config.spawnLocation = l;
        config.saveConfig();

        sender.sendMessage(config.messagePrefix + ChatColor.GREEN + "New spawn location set to place where you are!");

        return false;
    }

    @Override
    public String getUsageMessage(String usage) {
        return null;
    }

    @Override
    public String getNoPermissionMessage()
    {
        return ChatColor.DARK_RED + "You don't have permission to use that command!";
    }
}

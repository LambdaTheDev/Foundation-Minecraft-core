package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MTpCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("tp")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        return false;
    }

    @Override
    public String getUsageMessage(String usage) {
        return null;
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }
}

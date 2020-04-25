package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class MMoneyCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("money")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        sender.sendMessage(FoundationMinecraft.instance.getFMCConfig().messagePrefix + ChatColor.GREEN + "Your current balance: " +
                FoundationMinecraft.instance.lambdaPlayers.get(sender).getMoney() + "!");

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

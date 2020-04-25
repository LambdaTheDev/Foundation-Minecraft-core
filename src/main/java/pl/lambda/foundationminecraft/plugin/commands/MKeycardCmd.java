package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LPlayer;

public class MKeycardCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(command.getName().equalsIgnoreCase("keycard"))) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        LPlayer lPlayer = FoundationMinecraft.instance.lambdaPlayers.get(sender);
        Config config = FoundationMinecraft.instance.getFMCConfig();
        String prefix = config.messagePrefix;

        switch (lPlayer.getClearance())
        {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + ((Player)sender).getName() + " scp:level" + lPlayer.getClearance() + "card");
                sender.sendMessage(prefix + ChatColor.GREEN + "You received your keycard successfully!");
            default:
                sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to receive keycard!");
        }

        return false;
    }

    @Override
    public String getUsageMessage(String usage)
    {
        return FoundationMinecraft.getUsageMessage(usage);
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }
}

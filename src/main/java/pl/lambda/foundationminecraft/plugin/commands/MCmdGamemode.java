package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;

public class MCmdGamemode implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("gamemode")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        LambdaPlayer lambdaPlayer = FoundationMinecraft.getInstance().getLambdaPlayers().get(sender);
        int clearance = lambdaPlayer.getClearance();

        if(args.length != 1)
        {
            sender.sendMessage(FoundationMinecraft.getUsage("/gamemode <s|c|a|sp>"));
            return false;
        }

        switch (args[0])
        {
            case "0":
            case "s":
                if(!(clearance >= 3))
                {
                    sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't change this gamemode to survival!");
                    return false;
                }
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Changed gamemode to survival successfully!");
            case "1":
            case "c":
                if(!(clearance >= 5))
                {
                    sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't change this gamemode to survival!");
                    return false;
                }
                ((Player) sender).setGameMode(GameMode.CREATIVE);
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Changed gamemode to creative successfully!");
            case "2":
            case "a":
                ((Player) sender).setGameMode(GameMode.ADVENTURE);
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Changed gamemode to adventure successfully!");
            case "3":
            case "sp":
                if(!sender.isOp())
                {
                    sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't change this gamemode to spectator!");
                    return false;
                }
                ((Player) sender).setGameMode(GameMode.SPECTATOR);
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Changed gamemode to spectator successfully!");
        }

        return false;
    }
}

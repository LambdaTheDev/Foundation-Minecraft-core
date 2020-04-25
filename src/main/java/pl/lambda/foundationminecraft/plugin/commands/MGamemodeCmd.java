package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LPlayer;

public class MGamemodeCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("gamemode")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("That command is only for players!");
            return false;
        }

        Config config = FoundationMinecraft.instance.getFMCConfig();
        String prefix = config.messagePrefix;
        LPlayer lPlayer = FoundationMinecraft.instance.lambdaPlayers.get(sender);

        if(args.length != 1)
        {
            sender.sendMessage(prefix + getUsageMessage("/gamemode <s|c|a|sp>"));
            return false;
        }

        switch (args[0])
        {
            case "s":
            case "0":
            case "survival":
                if(!(lPlayer.getClearance() >= 3))
                {
                    sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to set yourself that gamemode!");
                    return false;
                }
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(prefix + ChatColor.GREEN + "You have changed your gamemode to survival successfully!");
                break;
            case "c":
            case "1":
            case "creative":
                if(!(lPlayer.getClearance() >= 5))
                {
                    sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to set yourself that gamemode!");
                    return false;
                }
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(prefix + ChatColor.GREEN + "You have changed your gamemode to creative successfully!");
                break;
            case "a":
            case "2":
            case "adventure":
                ((Player) sender).setGameMode(GameMode.ADVENTURE);
                sender.sendMessage(prefix + ChatColor.GREEN + "You have changed your gamemode to adventure successfully!");
                break;
            case "sp":
            case "3":
            case "spectator":
                if(!(lPlayer.getClearance() >= 5))
                {
                    sender.sendMessage(prefix + ChatColor.RED + "You are not allowed to set yourself that gamemode!");
                    return false;
                }
                ((Player) sender).setGameMode(GameMode.SURVIVAL);
                sender.sendMessage(prefix + ChatColor.GREEN + "You have changed your gamemode to spectator successfully!");
                break;
            default:
                sender.sendMessage(prefix + getUsageMessage("/gamemode <s|c|a|sp>"));
                return false;
        }

        return true;
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

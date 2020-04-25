package pl.lambda.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.utils.UsersAPI;

public class GmCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("gm")) return false;
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        UsersAPI usersAPI = new UsersAPI();

        if(!(p.isOp() || usersAPI.getSiteDirector(p.getUniqueId().toString())))
        {
            p.sendMessage(ChatColor.RED + "You have no permission to use that command!");
            return false;
        }

        if(args.length != 1)
        {
            p.sendMessage(ChatColor.RED + "Usage: /gm <0/1/2/3>");
        }

        int gamemode;

        switch (args[0])
        {
            case "0":
                gamemode = 0;
                break;
            case "1":
                gamemode = 1;
                break;
            case "2":
                gamemode = 2;
                break;
            case "3":
                gamemode = 3;
                break;
            default:
                p.sendMessage(ChatColor.RED + "Usage: /gm <0/1/2/3>");
                return false;
        }

        if(gamemode == 2)
        {
            p.setGameMode(GameMode.ADVENTURE);
        }

        if(gamemode == 0 && usersAPI.getClearance(p.getUniqueId().toString()) >= 3)
        {
            p.setGameMode(GameMode.SURVIVAL);
        }
        else if(gamemode == 1 && usersAPI.getClearance(p.getUniqueId().toString()) == 5)
        {
            p.setGameMode(GameMode.CREATIVE);
        }
        else if(gamemode == 3 && p.isOp())
        {
            p.setGameMode(GameMode.SPECTATOR);
        }
        else
        {
            p.sendMessage(ChatColor.RED + "You are not allowed to set yourself gamemode " + gamemode + "!");
            return false;
        }

        p.sendMessage(ChatColor.GREEN + "Gamemode changed successfully!");

        return false;
    }
}

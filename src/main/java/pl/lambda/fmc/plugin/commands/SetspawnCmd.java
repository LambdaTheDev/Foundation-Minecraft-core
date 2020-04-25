package pl.lambda.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.utils.ConfigAPI;

public class SetspawnCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("setspawn")) return false;

        if(!sender.isOp())
        {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to use that command!");
            return false;
        }

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Player p = (Player) sender;
        int x = p.getLocation().getBlockX();
        int y = p.getLocation().getBlockY();
        int z = p.getLocation().getBlockZ();
        World w = p.getWorld();
        String worldName = w.getName();

        ConfigAPI configAPI = new ConfigAPI();
        configAPI.setup();
        configAPI.setSpawnLocation(x, y, z, worldName);

        sender.sendMessage(ChatColor.GREEN + "New spawn location set to place where you are standing!");

        return true;
    }
}

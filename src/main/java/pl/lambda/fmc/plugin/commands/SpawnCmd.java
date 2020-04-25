package pl.lambda.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.lambda.fmc.utils.ConfigAPI;

public class SpawnCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(!command.getName().equalsIgnoreCase("spawn")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Player p = (Player) sender;

        ConfigAPI configAPI = new ConfigAPI();
        p.teleport(configAPI.getSpawnLocation());
        p.sendMessage(ChatColor.GREEN + "Teleported to spawn successfully!");

        return true;
    }
}

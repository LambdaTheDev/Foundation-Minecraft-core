package pl.ytskagamer.fmc.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.ytskagamer.fmc.utils.SyncAPI;

public class SyncCmd implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        if(command.getName().equalsIgnoreCase("sync"))
        {
            if(!(sender instanceof Player)) return false;

            if(args.length != 1)
            {
                sender.sendMessage(ChatColor.RED + "Incorrect usage! Correct: /sync <code>!");
                return false;
            }

            String code = args[0];
            Player p = (Player) sender;
            SyncAPI syncAPI = new SyncAPI();

            boolean sync = syncAPI.finishSync(p.getName(), code);
            if(!sync)
            {
                p.sendMessage(ChatColor.RED + "Error! Bad sync code.");
                return false;
            }

            p.sendMessage(ChatColor.GREEN + "Account synced succesfuly!");
            syncAPI.deleteSyncData(p.getName());
        }
        return false;
    }
}

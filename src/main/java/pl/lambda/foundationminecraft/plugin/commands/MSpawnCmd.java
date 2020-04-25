package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;

import java.util.concurrent.ForkJoinPool;

public class MSpawnCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("spawn")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Config config = FoundationMinecraft.instance.getFMCConfig();

        ((Player) sender).teleport(config.spawnLocation);

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

package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.syncdata.SyncManager;

public class MCmdSync implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("sync")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        if(args.length != 1)
        {
            sender.sendMessage(FoundationMinecraft.getUsage("/sync <code>"));
            return false;
        }

        String code = args[0];
        String verify = SyncManager.verifySync(sender.getName(), code);
        if(verify == null)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Wrong code! Check if code is correct or start new sync procedure!");
            return false;
        }

        FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).setDiscordID(verify);
        FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).save();
        SyncManager.endSync(sender.getName());

        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Account synced successfully! Enjoy your play.");

        return false;
    }
}

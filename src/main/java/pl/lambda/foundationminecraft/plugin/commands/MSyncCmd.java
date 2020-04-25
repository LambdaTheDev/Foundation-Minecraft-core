package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.datastorage.SyncDataStorage;

public class MSyncCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(command.getName().equalsIgnoreCase("sync")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        LPlayer lPlayer = FoundationMinecraft.instance.lambdaPlayers.get(sender);
        Config config = FoundationMinecraft.instance.getFMCConfig();

        if(lPlayer.getDiscordID() == null)
        {
            if(args.length == 0)
            {
                sender.sendMessage(config.messagePrefix + getUsageMessage("/sync <code>"));
                return false;
            }

            String code = args[0];

            SyncDataStorage syncDataStorage = FoundationMinecraft.instance.getSyncDataStorage();
            String requiredCode = syncDataStorage.getData().getString("sync." + ((Player) sender).getUniqueId().toString() + ".code");
            String discordID = syncDataStorage.getData().getString("sync." + ((Player) sender).getUniqueId().toString() + ".discordid");

            if(code.equals(requiredCode))
            {
                lPlayer.setDiscordID(discordID);
                lPlayer.saveToFile();

                sender.sendMessage(config.messagePrefix + ChatColor.GREEN + "Synchronization finished successfully!");
            }
            else
            {
                sender.sendMessage(config.messagePrefix + ChatColor.RED + "Wrong sync code. Try again.");
            }
        }
        else
        {
            sender.sendMessage(config.messagePrefix + ChatColor.RED + "You have already synced your account. If wanna un-sync account, contact staff.");
            return false;
        }

        return false;
    }

    @Override
    public String getUsageMessage(String usage) {
        return FoundationMinecraft.getUsageMessage(usage);
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }
}

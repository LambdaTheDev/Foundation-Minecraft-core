package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.datastorage.PlayerDataStorage;

public class MPayCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("pay")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        LPlayer lPlayer = FoundationMinecraft.instance.lambdaPlayers.get(sender);
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        Config config = FoundationMinecraft.instance.getFMCConfig();
        String prefix = config.messagePrefix;

        if(args.length != 2)
        {
            sender.sendMessage(prefix + getUsageMessage("/pay <nick> <amount>"));
            return false;
        }

        if(!LPlayer.hasPlayedBefore(args[0]))
        {
            sender.sendMessage(prefix + ChatColor.RED + "That player has never played before!");
            return false;
        }

        String lambdaID = LPlayer.getLambdaID(args[0]);
        int amount = Integer.parseInt(args[1]);
        int senderBalance = lPlayer.getMoney();

        Player target = Bukkit.getPlayer(args[0]);

        if(amount <= senderBalance)
        {
            if(target != null)
            {
                LPlayer targetLambdaPlayer = FoundationMinecraft.instance.lambdaPlayers.get(target);
                targetLambdaPlayer.setMoney(targetLambdaPlayer.getMoney() + amount);
                lPlayer.setMoney(lPlayer.getMoney() - amount);

                sender.sendMessage(prefix + ChatColor.GREEN + "You sent " + amount + " money to: " + targetLambdaPlayer.getNickname() + " successfully!");
            }
            else
            {
                playerDataStorage.getData().set("players." + lambdaID + ".money", (playerDataStorage.getData().getInt("players." + lambdaID + ".money") + amount));
                lPlayer.setMoney(lPlayer.getMoney() - amount);
                sender.sendMessage(prefix + ChatColor.GREEN + "You sent " + amount + " money to: " + args[0] + " successfully!");
            }
        }
        else
        {
            sender.sendMessage(prefix + ChatColor.RED + "You can't afford for that!");
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

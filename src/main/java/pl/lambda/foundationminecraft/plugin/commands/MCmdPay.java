package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

public class MCmdPay implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("pay")) return false;

        if(args.length != 2)
        {
            sender.sendMessage(FoundationMinecraft.getUsage("/pay <nick> <amount>"));
            return false;
        }

        LambdaPlayer target;
        Player p = null;

        if(Bukkit.getPlayer(args[0]) != null)
        {
            target = FoundationMinecraft.getInstance().getLambdaPlayers().get(Bukkit.getPlayer(args[0]));
        }
        else
        {
            p = Bukkit.getPlayer(args[0]);
            target = LambdaPlayer.getLambdaPlayerByNickname(args[0]);
            if(target == null)
            {
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Player with that nickname hasn't played here (system is case-sensitive)!");
                return false;
            }
        }


        int amount = Integer.parseInt(args[1]);

        if(sender.isOp())
        {
            target.setMoney(target.getMoney() + amount);
            target.save();
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You shared " + amount + " money to " + args[0] + " from OP's wallet!");
            if(p != null) p.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You received from " + sender.getName() + " " + amount + " money!");
        }
        else
        {
            LambdaPlayer lambdaSender = FoundationMinecraft.getInstance().getLambdaPlayers().get((Player) sender);
            int balance = lambdaSender.getMoney();

            if(balance >= amount)
            {
                lambdaSender.setMoney(lambdaSender.getMoney() - amount);
                target.setMoney(target.getMoney() + amount);
                lambdaSender.save();
                target.save();
                if(p != null) p.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You received from " + sender.getName() + " " + amount + " money!");
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You shared " + amount + " money to " + args[0] + " from your wallet!");
            }
            else
            {
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't afford for that!");
                return false;
            }
        }

        return false;
    }
}

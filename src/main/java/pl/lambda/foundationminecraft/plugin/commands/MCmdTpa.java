package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MCmdTpa implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("tpa")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        if(args.length != 1)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Incorrect usage! Correct: /tpa <nick>!");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if(target == null)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "That player isn't on the server!");
            return false;
        }

        Date date = new Date();
        Date cooldownTime = FoundationMinecraft.getInstance().getTpaCommandCooldown().getOrDefault(((Player) sender).getUniqueId(), null);

        if(cooldownTime == null || date.after(cooldownTime))
        {
            Calendar calendar = Calendar.getInstance();
            long time = calendar.getTimeInMillis();
            Date newCooldown = new Date(time + (10 * 60000));

            FoundationMinecraft.getInstance().getTpaCommandCooldown().put(((Player) sender).getUniqueId(), newCooldown);
            FoundationMinecraft.getInstance().getTeleportRequests().put(target, (Player) sender);
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Teleport request sent to: " + target.getName() + "!");
            target.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GOLD + "Player " + sender.getName() + " wants to teleport to you!\nAccept by typing /tpaccept, or deny - by typing /tpdeny!");
        }
        else
        {
            DateFormat format = new SimpleDateFormat("yyyy MM dd hh:mm");
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't teleport to player now! You need to wait until: " + format.format(cooldownTime) + "!");
        }

        return false;
    }
}

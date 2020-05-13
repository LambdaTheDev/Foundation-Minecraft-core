package pl.lambda.foundationminecraft.plugin.commands;

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

public class MCmdTpaccept implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("tpaccept")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Player target = FoundationMinecraft.getInstance().getTeleportRequests().getOrDefault((Player) sender, null);
        if(target == null)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You don't have active teleport requests!");
            return false;
        }

        Date date = new Date();
        Date cooldownTime = FoundationMinecraft.getInstance().getTpaCommandCooldown().getOrDefault(((Player) sender).getUniqueId(), null);

        if(cooldownTime == null || date.after(cooldownTime))
        {
            Calendar calendar = Calendar.getInstance();
            long time = calendar.getTimeInMillis();
            Date newCooldown = new Date(time + (10 * 60000));

            FoundationMinecraft.getInstance().getTpacceptCommandCooldown().put(((Player) sender).getUniqueId(), newCooldown);
            FoundationMinecraft.getInstance().getTeleportRequests().remove((Player) sender);

            target.teleport(((Player) sender).getLocation());
            target.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Your teleport request has been accepted!");
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You have accepted " + target.getName() + "'s teleport request!");
        }
        else
        {
            DateFormat format = new SimpleDateFormat("yyyy MM dd hh:mm");
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You can't accept teleport requests now! You need to wait until: " + format.format(cooldownTime) + "!");
        }

        return false;
    }
}

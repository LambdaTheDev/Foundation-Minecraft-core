package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.enums.ChatType;

public class MCmdChattype implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("chattype")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("That command is only for players!");
            return false;
        }

        if(FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).getChatType() == ChatType.GLOBAL)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Changed your chat type to local!");
            FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).setChatType(ChatType.LOCAL);
        }
        else
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Changed your chat type to global!");
            FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).setChatType(ChatType.GLOBAL);
        }

        return false;
    }
}

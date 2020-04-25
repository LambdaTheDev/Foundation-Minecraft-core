package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.ChatType;
import pl.lambda.foundationminecraft.utils.LPlayer;

public class MChattypeCmd implements CommandExecutor, ILMinecraftCommand
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("chatType")) return false;

        String prefix = ChatColor.translateAlternateColorCodes('&', FoundationMinecraft.instance.getFMCConfig().messagePrefix);

        if(FoundationMinecraft.checkIfDeprecated(this.getClass()) != null)
        {
            sender.sendMessage(prefix + ChatColor.RED + FoundationMinecraft.checkIfDeprecated(this.getClass()));
        }

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Player p = (Player) sender;
        LPlayer lp = FoundationMinecraft.instance.lambdaPlayers.get(p);
        System.out.println(FoundationMinecraft.instance.lambdaPlayers.toString());
        ChatType currentType = lp.getChatType();

        if(currentType == ChatType.GLOBAL)
        {
            lp.setChatType(ChatType.LOCAL);
            sender.sendMessage(prefix + ChatColor.GREEN + "Changed your chat type to local!");
        }
        else
        {
            lp.setChatType(ChatType.GLOBAL);
            sender.sendMessage(prefix + ChatColor.GREEN + "Changed your chat type to global!");
        }

        return true;
    }

    @Override
    public String getUsageMessage(String usage)
    {
        return null;
    }

    @Override
    public String getNoPermissionMessage()
    {
        return null;
    }
}

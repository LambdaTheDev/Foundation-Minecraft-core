package pl.lambda.fmc.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.plugin.FMC;
import pl.lambda.fmc.utils.ConfigAPI;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.Utils;

import java.awt.*;

public class DiscordDeletedeptCmd extends ListenerAdapter
{
    FMC plugin = FMC.getPlugin(FMC.class);
    ConfigAPI configAPI = plugin.getConfigAPI();
    RoleAPI roleAPI = plugin.getRoleAPI();

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = configAPI.getDiscordPrefix();
        if(args[0].equalsIgnoreCase(prefix + "deletedept"))
        {
            if(e.getAuthor().isBot()) return;

            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                DiscordBot.sendNoRankMessage("No permission!",
                        "You don't have permission to use that command!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }
            if(args.length < 2)
            {
                DiscordBot.sendNoRankMessage("Wrong usage!",
                        "Correct usage: " + prefix + "deletedept <name>!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            String name = Utils.stringBuilder(args, 1);
            if(!roleAPI.roleExist(roleAPI.getRoleUUIDByName(name)))
            {
                DiscordBot.sendNoRankMessage("Error!",
                        "Role with that name not exist!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            roleAPI.delete(roleAPI.getRoleUUIDByName(name));
            DiscordBot.sendNoRankMessage("Success!", "You deleted role " + name + " succesfully!", e.getTextChannel(), e.getMember(), Color.GREEN);
        }
    }
}

package pl.ytskagamer.fmc.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.utils.ConfigAPI;
import pl.ytskagamer.fmc.utils.RoleAPI;
import pl.ytskagamer.fmc.utils.Utils;

import java.awt.*;

public class DiscordDeletedeptCmd extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        RoleAPI roleAPI = new RoleAPI();

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

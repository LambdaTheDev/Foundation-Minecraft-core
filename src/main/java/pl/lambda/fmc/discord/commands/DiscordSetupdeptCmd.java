package pl.lambda.fmc.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.utils.ConfigAPI;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.Utils;

import java.awt.*;

public class DiscordSetupdeptCmd extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        RoleAPI roleAPI = new RoleAPI();

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = configAPI.getDiscordPrefix();
        if(args[0].equalsIgnoreCase(prefix + "setupdept"))
        {
            if(e.getAuthor().isBot()) return;

            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                DiscordBot.sendNoRankMessage("No permission!",
                        "You don't have permission to use that command!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            if(args.length < 4)
            {
                DiscordBot.sendNoRankMessage("Wrong usage!",
                        "Correct usage is: " + prefix + "setupdept (shortcut) (color) (ping department role) (department name)!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            if(args[1].length() > 5)
            {
                DiscordBot.sendNoRankMessage("Wrong shortcut!",
                        "Shortcut can't be longer than 5 characters!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            if(!Utils.validColor(args[2]))
            {
                DiscordBot.sendNoRankMessage("Wrong color!",
                        "Wrong color code!\nList of valid codes:\n" +
                                "(0-black, 1-dark blue, 2-dark green, 3-aqua, 4-dark red, 5-purple, 6-gold, 7-gray, 8-dark gray, 9-blue, a-light blue, b-light blue, c-red, d-magenta, e-yellow, f-white).",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }
            args[3] = args[3].replace("<", "").replace(">", "").replace("&", "").replace("@", "");
//            args[3] = String.valueOf(Integer.parseInt(args[3]));

            String name = Utils.stringBuilder(args, 4);

            if(name.length() > 25)
            {
                DiscordBot.sendNoRankMessage("Name is invalid!",
                        "Name can't be longer than 25 characters!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            if((roleAPI.roleExist(roleAPI.getRoleUUIDByName(name)) || roleAPI.roleExist(roleAPI.getRoleUUIDByDiscord(args[4]))))
            {
                DiscordBot.sendNoRankMessage("Role exist!",
                        "Role with that Discord or name exist!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            roleAPI.create(name, args[1], args[2], args[3], false);
            DiscordBot.sendNoRankMessage("Department created successfully!",
                    "You created " + name + " with color: " + args[2] + " and shortcut: " + args[1] + ".",
                    e.getTextChannel(), e.getMember(), Color.GREEN);
        }
    }
}

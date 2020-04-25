package pl.lambda.fmc.discord.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.utils.ConfigAPI;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.UsersAPI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DiscordRefreshrolesCmd extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        UsersAPI usersAPI = new UsersAPI();
        RoleAPI roleAPI = new RoleAPI();

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = configAPI.getDiscordPrefix();
        if(args[0].equalsIgnoreCase(prefix + "refreshroles"))
        {
            String discordID = e.getAuthor().getId();
            String check = usersAPI.getUserUUIDByDiscord(discordID);
            if(check == null || check.equals("none"))
            {
                DiscordBot.sendNoRankMessage("Error!", "Something went wrong - your account not exist. If you think that's an error - contact staff.", e.getTextChannel(), e.getMember(), Color.red);
                DiscordBot.sendNoRankMessage("Hey!", "This command is deprecated and will be removed in future updates. Roles should update when you join server.", e.getTextChannel(), e.getMember(), Color.yellow);
                return;
            }

            Member m = e.getMember();
            if(m == null)
            {
                DiscordBot.sendNoRankMessage("Error!", "Unknown error. Contact staff.", e.getTextChannel(), e.getMember(), Color.red);
                DiscordBot.sendNoRankMessage("Hey!", "This command is deprecated and will be removed in future updates. Roles should update when you join server.", e.getTextChannel(), e.getMember(), Color.yellow);
                return;
            }

            List<Role> roles = m.getRoles();

            for(Role role : roles)
            {
                String roleID = role.getId();
                String roleUUID = roleAPI.getRoleUUIDByDiscord(roleID);
                if(roleUUID == null) continue;
                usersAPI.clearRoles(check);
                usersAPI.addRoles(roleUUID);
            }

            DiscordBot.sendNoRankMessage("Success!", "Your roles should be updated now.", e.getTextChannel(), e.getMember(), Color.green);
            DiscordBot.sendNoRankMessage("Hey!", "This command is deprecated and will be removed in future updates. Roles should update when you join server.", e.getTextChannel(), e.getMember(), Color.yellow);
        }
    }
}

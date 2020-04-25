package pl.lambda.fmc.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.utils.ConfigAPI;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.UsersAPI;

import java.awt.*;

public class DiscordMoneyCmd extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        UsersAPI usersAPI = new UsersAPI();

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = configAPI.getDiscordPrefix();
        if(args[0].equalsIgnoreCase(prefix + "money"))
        {
            int money = usersAPI.getMoney(usersAPI.getUserUUIDByDiscord(e.getAuthor().getId()));
            DiscordBot.sendNoRankMessage("Your balance", "You have now: " + money + "$! Don't waste them.", e.getTextChannel(), e.getMember(), Color.green);
        }
    }
}

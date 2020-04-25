package pl.lambda.fmc.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.utils.ConfigAPI;
import pl.lambda.fmc.utils.UsersAPI;

import java.awt.*;

public class DiscordPayCmd extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        ConfigAPI configAPI = new ConfigAPI();
        UsersAPI usersAPI = new UsersAPI();

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = configAPI.getDiscordPrefix();
        if(args[0].equalsIgnoreCase(prefix + "pay"))
        {
            if(args.length != 3)
            {
                DiscordBot.sendNoRankMessage("Wrong usage!", "Correct: " + prefix + "pay <ping member who you share money> <amount>", e.getTextChannel(), e.getMember(), Color.green);
                return;
            }

            String userid = args[1].replace("!", "").replace("<", "").replace(">", "").replace("&", "").replace("@", "");
            int ammount = Integer.parseInt(args[2]);
            int currentMoney = usersAPI.getMoney(usersAPI.getUserUUIDByDiscord(e.getAuthor().getId()));

            boolean accountExist = usersAPI.accountExist(usersAPI.getUserUUIDByDiscord(userid));
            System.out.println(usersAPI.getUserUUIDByDiscord(userid) + ":" + userid);
            if(accountExist)
            {
                System.out.println("MONEY: amount " + ammount + ": current " + currentMoney);

                if(ammount > currentMoney)
                {
                    DiscordBot.sendNoRankMessage("You can't afford for that!", "You don't have enough money!", e.getTextChannel(), e.getMember(), Color.red);
                }
                else
                {
                    //can afford
                    usersAPI.setMoney(usersAPI.getUserUUIDByDiscord(userid), (usersAPI.getMoney(usersAPI.getUserUUIDByDiscord(userid)) + ammount));
                    usersAPI.setMoney(usersAPI.getUserUUIDByDiscord(e.getAuthor().getId()), (usersAPI.getMoney(usersAPI.getUserUUIDByDiscord(e.getAuthor().getId())) - ammount));
                    DiscordBot.sendNoRankMessage("You gave " + ammount + "$!", "Member " + args[1] + " received your money succesfully!", e.getTextChannel(), e.getMember(), Color.green);
                }
            }
            else
            {
                DiscordBot.sendNoRankMessage("Error!", "Pinged member don't have account here!", e.getTextChannel(), e.getMember(), Color.red);
            }
        }
    }
}

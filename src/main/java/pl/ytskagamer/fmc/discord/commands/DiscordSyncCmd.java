package pl.ytskagamer.fmc.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.utils.*;

import java.awt.*;

public class DiscordSyncCmd extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        UsersAPI usersAPI = new UsersAPI();
        RoleAPI roleAPI = new RoleAPI();
        SyncAPI syncAPI = new SyncAPI();
        ConfigAPI configAPI = new ConfigAPI();

        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = configAPI.getDiscordPrefix();
        if(args[0].equalsIgnoreCase(prefix + "sync"))
        {
            if(e.getAuthor().isBot()) return;
            if(!e.getMember().hasPermission(Permission.ADMINISTRATOR))
            {
                DiscordBot.sendNoRankMessage("No permission!",
                        "You don't have permission to use that command!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            if(args.length != 2)
            {
                DiscordBot.sendNoRankMessage("Wrong usage!",
                        "Correct usage: " + prefix + "sync (minecraft nickname)!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            String uuid = usersAPI.getUserUUIDByNick(args[1]);
            if(uuid == null)
            {
                DiscordBot.sendNoRankMessage("Account error!",
                        "Account with nickname " + args[1] + " not exist (never joined server)!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            Player p = Bukkit.getServer().getPlayer(args[1]);
            if(p == null)
            {
                DiscordBot.sendNoRankMessage("Account error!",
                        "Player with nickname " + args[1] + " is not online!",
                        e.getTextChannel(), e.getMember(), Color.RED);
                return;
            }

            String code = Utils.randomString(4);
            boolean syncing = syncAPI.startSync(e.getAuthor().getId(), args[1], code);

            if(!syncing)
            {
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Error");
                builder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
                builder.setColor(Color.RED);
                builder.addField("", "Something went wrong with syncing!", false);
                e.getChannel().sendMessage(builder.build()).queue();
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Success!");
            builder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
            builder.setColor(Color.GREEN);
            builder.addField("", "We have sent you sync code on DM! If you disabled them, you need to enable that (just for that moment)!", false);
            e.getChannel().sendMessage(builder.build()).queue();

            e.getAuthor().openPrivateChannel().queue((channel) ->
            {
                builder.setTitle("Sync code");
                builder.setAuthor(e.getAuthor().getName(), e.getAuthor().getAvatarUrl(), e.getAuthor().getAvatarUrl());
                builder.setColor(Color.YELLOW);
                builder.addField("", "Now you have to type command: /sync " + code + " to sync " + p.getName() + " with that Discord!", false);
                channel.sendMessage(builder.build()).queue();
            });
        }
    }
}

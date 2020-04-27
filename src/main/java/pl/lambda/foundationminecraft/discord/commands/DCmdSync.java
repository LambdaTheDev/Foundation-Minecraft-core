package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.syncdata.SyncManager;

public class DCmdSync extends ListenerAdapter
{
    DiscordModule discordModule;
    public DCmdSync(DiscordModule discordModule)
    {
        this.discordModule = discordModule;
    }

    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        if(args[0].equalsIgnoreCase("sync"))
        {
            if(args.length != 2)
            {
                e.getTextChannel().sendMessage("**Error!** Wrong usage. Correct: " + config.getDiscordBotPrefix() + "sync <Minecraft nickname>!").queue();
                return;
            }

            String nickname = args[1];
            Player target = Bukkit.getPlayer(nickname);
            if(target == null)
            {
                e.getTextChannel().sendMessage("**Error!** You are not online! Join server and start sync procedure again!").queue();
                return;
            }

            String code = Utils.randomString(4);
            SyncManager.startSync(target.getName(), e.getAuthor().getId(), code);

            e.getTextChannel().sendMessage("**Success!** Check your DMs for sync code (if you have disabled DMs, unlock them for second and restart sync procedure!").queue();
            e.getAuthor().openPrivateChannel().queue((channel -> {
                channel.sendMessage("**Sync code:** Your sync code is: " + code + ". Now type in-game command /sync " + code + " to finish procedure.").queue();
            }));
        }
    }
}

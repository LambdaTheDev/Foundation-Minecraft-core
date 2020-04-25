package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.datastorage.RankDataStorage;
import pl.lambda.foundationminecraft.utils.datastorage.SyncDataStorage;

import java.awt.*;

public class DSyncCmd extends ListenerAdapter implements ILDiscordCommand
{
    Config config = FoundationMinecraft.instance.getFMCConfig();
    TextChannel channel;

    @Override
    public void onMessageReceived(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");
        String prefix = config.botPrefix;

        if(args[0].equalsIgnoreCase(prefix + "sync"))
        {
            if(e.getAuthor().isBot()) return;

            channel = e.getTextChannel();
            checkIfDeprecated();

            if(args.length == 2)
            {
                String nickname = args[1];
                Player p = Bukkit.getPlayer(nickname);
                if(p == null)
                {
                    FoundationMinecraft.instance.discordModule.sendMessageToChannel("Error!", Color.red,
                            "Player not online", "Player with provided nickname is not online!", channel);
                    return;
                }

                String code = Utils.randomString(4);
                SyncDataStorage syncDataStorage = FoundationMinecraft.instance.getSyncDataStorage();
                syncDataStorage.getData().set("sync." + p.getUniqueId().toString() + ".code", code);
                syncDataStorage.getData().set("sync." + p.getUniqueId().toString() + ".discordid", e.getAuthor().getId());
                syncDataStorage.save();

                e.getAuthor().openPrivateChannel().queue((privateChannel ->
                        privateChannel.sendMessage("**Hey!** Your sync code is: " + code + "! Type in-game command /sync " + code + ", so sync procedure will be finished.").queue()));

                FoundationMinecraft.instance.discordModule.sendMessageToChannel("Sync started!", Color.green, "You started synchronization!", "You started sync procedure for " + p.getName() + ". Check your DMs to get sync code (if you have disabled DMs, enable them and restart procedure)!", channel);
            }
            else
            {
                getUsageMessage(prefix + "sync <Minecraft nickname>");
            }
        }
    }

    @Override
    public String getUsageMessage(String usage)
    {
        FoundationMinecraft.instance.discordModule.sendMessageToChannel("Wrong usage!", Color.red, "Correct usage:", usage, channel);
        return null;
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }

    @Override
    public boolean checkIfDeprecated()
    {
        if(FoundationMinecraft.checkIfDeprecated(this.getClass()) != null)
        {
            FoundationMinecraft.instance.discordModule.sendMessageToChannel("Hey!", Color.yellow, "Command deprecated", FoundationMinecraft.checkIfDeprecated(this.getClass()), channel);
            return true;
        }
        return false;
    }
}

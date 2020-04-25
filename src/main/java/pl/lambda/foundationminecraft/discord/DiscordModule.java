package pl.lambda.foundationminecraft.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.commands.*;
import pl.lambda.foundationminecraft.discord.listeners.OnGuildMemberRoleAdd;
import pl.lambda.foundationminecraft.discord.listeners.OnGuildMemberRoleRemove;
import pl.lambda.foundationminecraft.discord.listeners.OnMessageReceived;
import pl.lambda.foundationminecraft.discord.listeners.OnReady;
import pl.lambda.foundationminecraft.utils.Config;

import javax.security.auth.login.LoginException;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DiscordModule
{
    public JDA jda;

    public void startBot()
    {
        Config config = FoundationMinecraft.instance.getFMCConfig();
        if(jda != null) return;

        List<GatewayIntent> intentList = loadIntents();

        try
        {
            jda = JDABuilder.createDefault(config.botToken, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                    .setActivity(Activity.watching("you"))
                    .addEventListeners(new OnReady(), new OnMessageReceived(), new OnGuildMemberRoleAdd(), new OnGuildMemberRoleRemove())
                    .addEventListeners(new DDeletedeptCmd(), new DRefreshrolesCmd(), new DSyncCmd(), new DSetupdeptCmd())
                    .build();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
    }

    void registerCommand(Class<? extends ILDiscordCommand> commandClass)
    {
        jda.addEventListener(commandClass);
    }

    List<GatewayIntent> loadIntents()
    {
        List<GatewayIntent> result = new ArrayList<>();
        result.add(GatewayIntent.GUILD_MESSAGES);
        result.add(GatewayIntent.GUILD_VOICE_STATES);
        result.add(GatewayIntent.DIRECT_MESSAGES);
        return result;
    }

    EmbedBuilder buildMessage(String title, Color color, String fieldTitle, String message)
    {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setColor(color);
        embed.addField(fieldTitle, message, false);
        return embed;
    }

    public void sendMessageToSyncChannel(String title, Color color, String fieldTitle, String message)
    {
        Config config = FoundationMinecraft.instance.getFMCConfig();
        TextChannel channel = jda.getTextChannelById(config.syncChannelID);
        if(channel == null)
        {
            channel = jda.getTextChannels().get(0);
            channel.sendMessage(buildMessage("Error handled!", Color.red, "Sync channel not found!", "Check if synchronization channel ID is correct in config. If you think that everything is correct and this still appears, contact staff.").build()).queue();
            return;
        }

        channel.sendMessage(buildMessage(title, color, fieldTitle, message).build()).queue();
    }

    public void sendMessageToChannel(String title, Color color, String fieldTitle, String message, TextChannel channel)
    {
        channel.sendMessage(buildMessage(title, color, fieldTitle, message).build()).queue();
    }

    public static DiscordModule getInstance()
    {
        return FoundationMinecraft.instance.discordModule;
    }

    public void sendUsageMessage(String usage, TextChannel channel)
    {
        sendMessageToChannel("Wrong usage!", Color.red, "You haven't used that command successfully!", "Correct usage: " + usage + ".", channel);
    }
}

package pl.ytskagamer.fmc.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import pl.ytskagamer.fmc.discord.commands.*;
import pl.ytskagamer.fmc.discord.listeners.OnGuildMemberRoleAdd;
import pl.ytskagamer.fmc.discord.listeners.OnGuildMemberRoleRemove;
import pl.ytskagamer.fmc.discord.listeners.OnReady;
import pl.ytskagamer.fmc.utils.ConfigAPI;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.awt.*;

public class DiscordBot
{
    public static JDA jda;

    public static void startBot()
    {
        ConfigAPI configAPI = new ConfigAPI();

        if(DiscordBot.jda != null) return;

        try
        {
            DiscordBot.jda = new JDABuilder(configAPI.getDiscordToken())
                    .setActivity(Activity.watching("you :D"))
                    .addEventListeners(
                            //comands
                            new DiscordSetupdeptCmd(),
                            new DiscordSyncCmd(),
                            new DiscordDeletedeptCmd(),
                            //listenres
                            new OnReady(),
                            new OnGuildMemberRoleAdd(),
                            new OnGuildMemberRoleRemove()
                    )
                    .build();
        }
        catch (LoginException e)
        {
            e.printStackTrace();
        }
    }

    public static void sendMessageToSyncChannel(String message)
    {
        ConfigAPI configAPI = new ConfigAPI();

//        TextChannel c = DiscordBot.jda.getTextChannelById(configAPI.getDiscordChatSyncChannelID());
//        c.sendMessage(message).queue();
    }

    public static void sendNoRankMessage(String title, String message, TextChannel channel, @Nullable Member member, Color color)
    {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(title);
        builder.setColor(color);
        builder.addField("", message, false);
        channel.sendMessage(builder.build()).queue();
    }
}
package pl.ytskagamer.fmc.discord.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.ytskagamer.fmc.discord.DiscordBot;
import pl.ytskagamer.fmc.plugin.FMC;
import pl.ytskagamer.fmc.utils.ConfigAPI;

import java.awt.*;

public class OnReady extends ListenerAdapter
{
    public void onReady(ReadyEvent e)
    {
        FMC.serverOpened = true;
        ConfigAPI cfg = new ConfigAPI();
        DiscordBot.sendNoRankMessage("Server started!",
                "What are you waiting for? Join now!", DiscordBot.jda.getTextChannelById(cfg.getSyncChannelID()), null , Color.GREEN);
    }
}

package pl.lambda.fmc.discord.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.discord.DiscordBot;
import pl.lambda.fmc.plugin.FMC;
import pl.lambda.fmc.utils.ConfigAPI;

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

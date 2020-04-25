package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;

import java.awt.*;

public class OnReady extends ListenerAdapter
{
    public void onReady(@NotNull ReadyEvent ignored)
    {
        DiscordModule.getInstance().sendMessageToSyncChannel("Server stated!", Color.green, "What are you waiting for?", "Start Minecraft and join FMC now :)");
        FoundationMinecraft.serverLoaded = true;
    }
}

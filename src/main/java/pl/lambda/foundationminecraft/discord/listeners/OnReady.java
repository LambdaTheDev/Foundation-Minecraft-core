package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.foundationminecraft.FoundationMinecraft;

public class OnReady extends ListenerAdapter
{
    public void onReady(ReadyEvent e)
    {
        FoundationMinecraft.SERVER_ENABLED = true;
        FoundationMinecraft.getInstance().getDiscordModule().getSyncChannel().sendMessage("**Server opened!** What are you waiting for? Start Minecraft and join server.").queue();
    }
}

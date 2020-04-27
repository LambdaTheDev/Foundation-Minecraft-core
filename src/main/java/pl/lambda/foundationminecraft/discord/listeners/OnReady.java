package pl.lambda.foundationminecraft.discord.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.exceptions.GuildIsNullException;
import pl.lambda.foundationminecraft.utils.exceptions.SyncChannelIsNullException;

public class OnReady extends ListenerAdapter
{
    private DiscordModule module;
    public OnReady(DiscordModule module)
    {
        this.module = module;
    }

    public void onReady(ReadyEvent e)
    {
        try
        {

            Config config = FoundationMinecraft.getInstance().getFmcConfig();

            if(this.module.getJDA() == null)
            {
                System.err.println("UNKNOWN BOT ERROR. CONTACT Lambda#2594 FOR SUPPORT. DISABLING SERVER...");
                Bukkit.shutdown();
                return;
            }

            this.module.setGuild(this.module.getJDA().getGuildById(config.getDiscordGuild()));
            if(this.module.getGuild() == null)
            {
                throw new GuildIsNullException(config.getDiscordGuild());
            }

            this.module.setSyncChannel(this.module.getGuild().getTextChannelById(config.getDiscordSyncChannel()));
            if(this.module.getSyncChannel() == null)
            {
                throw new SyncChannelIsNullException();
            }

            FoundationMinecraft.SERVER_ENABLED = true;
            this.module.getSyncChannel().sendMessage("**Server opened!** What are you waiting for? Start Minecraft and join server.").queue();
        }
        catch (GuildIsNullException | SyncChannelIsNullException | NullPointerException ex)
        {
            ex.printStackTrace();
            System.err.println("COULDN'T LOAD GUILD AND/OR SYNC CHANNEL. DISABLING SERVER...");
            Bukkit.shutdown();
        }
    }
}

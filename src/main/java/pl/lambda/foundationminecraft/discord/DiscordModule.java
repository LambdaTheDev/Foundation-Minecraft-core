package pl.lambda.foundationminecraft.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.commands.DCmdDeletedept;
import pl.lambda.foundationminecraft.discord.commands.DCmdRefreshroles;
import pl.lambda.foundationminecraft.discord.commands.DCmdSetupdept;
import pl.lambda.foundationminecraft.discord.commands.DCmdSync;
import pl.lambda.foundationminecraft.discord.listeners.OnGuildMemberRoleAdd;
import pl.lambda.foundationminecraft.discord.listeners.OnGuildMemberRoleRemove;
import pl.lambda.foundationminecraft.discord.listeners.OnMessageReceived;
import pl.lambda.foundationminecraft.discord.listeners.OnReady;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.exceptions.GuildIsNullException;
import pl.lambda.foundationminecraft.utils.exceptions.SyncChannelIsNullException;

import javax.security.auth.login.LoginException;

public class DiscordModule
{
    //private static DiscordModule instance;

    private Config config = FoundationMinecraft.getInstance().getFmcConfig();
    private JDA jda;
    private Guild guild;
    private TextChannel syncChannel;

    public void start()
    {
        if(jda != null) return;
        try
        {
            this.jda = JDABuilder.create(config.getDiscordBotToken(), GatewayIntent.getIntents(1))
                    .setActivity(Activity.watching("you"))
                    .addEventListeners(new OnGuildMemberRoleAdd(), new OnGuildMemberRoleRemove(), new OnMessageReceived(), new OnReady())
                    .addEventListeners(new DCmdDeletedept(this), new DCmdRefreshroles(this),
                            new DCmdSetupdept(this), new DCmdSync(this))
                    .build();

            this.guild = jda.getGuildById(config.getDiscordGuild());
            if(this.guild == null)
            {
                throw new GuildIsNullException();
            }

            this.syncChannel = this.guild.getTextChannelById(config.getDiscordSyncChannel());
            if(syncChannel == null)
            {
                throw new SyncChannelIsNullException();
            }
        }
        catch (LoginException | GuildIsNullException | SyncChannelIsNullException e)
        {
            e.printStackTrace();
            System.err.println("COULDN'T LOGIN TO DISCORD BOT. DISABLING SERVER...");
            Bukkit.shutdown();
        }
    }

    public JDA getJDA()
    {
        return jda;
    }

    public Guild getGuild()
    {
        return guild;
    }

    public TextChannel getSyncChannel() {
        return syncChannel;
    }
}

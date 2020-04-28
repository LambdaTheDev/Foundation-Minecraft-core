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
import pl.lambda.foundationminecraft.discord.listeners.OnMessageReceived;
import pl.lambda.foundationminecraft.discord.listeners.OnReady;
import pl.lambda.foundationminecraft.utils.Config;

import javax.security.auth.login.LoginException;

public class DiscordModule
{
    //private static DiscordModule instance;

    private Config config = FoundationMinecraft.getInstance().getFmcConfig();
    private JDA jda;
    private Guild guild;
    private TextChannel syncChannel;
    //private static boolean loaded = false;

    public void start()
    {
        if(this.jda != null) return;

        try
        {
            this.jda = JDABuilder.create(config.getDiscordBotToken(), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                    .setActivity(Activity.watching("you"))
                    .addEventListeners(new OnMessageReceived(), new OnReady(this))
                    .addEventListeners(new DCmdDeletedept(this), new DCmdRefreshroles(this),
                            new DCmdSetupdept(this), new DCmdSync(this))
                    .build();
        }
        catch (LoginException | NullPointerException e)
        {
            e.printStackTrace();
            System.err.println("COULDN'T LOGIN TO DISCORD BOT. DISABLING SERVER...");
            Bukkit.shutdown();
        }
    }

    public JDA getJDA()
    {
        return this.jda;
    }

    public Guild getGuild()
    {
        return this.guild;
    }

    public TextChannel getSyncChannel() {
        return this.syncChannel;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public void setSyncChannel(TextChannel syncChannel) {
        this.syncChannel = syncChannel;
    }

    @Override
    public String toString() {
        return "DiscordModule{" +
                "config=" + config +
                ", jda=" + jda +
                ", guild=" + guild +
                ", syncChannel=" + syncChannel +
                '}';
    }
}

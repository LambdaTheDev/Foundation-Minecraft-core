package pl.lambda.foundationminecraft.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ILDiscordCommand
{
    void onMessageReceived(MessageReceivedEvent e);
    String getUsageMessage(String usage);
    String getNoPermissionMessage();
    boolean checkIfDeprecated();
}

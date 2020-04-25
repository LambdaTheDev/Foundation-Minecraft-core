package pl.lambda.fmc.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface ILambdaDiscordCommand
{
    void onMessageReceived(MessageReceivedEvent e);
    boolean checkDeprecation();
    String getUsageMessage();
    String getDeprecationMessage();
    String getNoPermissionMessage();
}

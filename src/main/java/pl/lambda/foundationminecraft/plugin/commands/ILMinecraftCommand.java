package pl.lambda.foundationminecraft.plugin.commands;

public interface ILMinecraftCommand
{
    String getUsageMessage(String usage);
    String getNoPermissionMessage();
}

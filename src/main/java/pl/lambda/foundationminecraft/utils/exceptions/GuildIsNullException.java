package pl.lambda.foundationminecraft.utils.exceptions;

public class GuildIsNullException extends Exception
{
    public GuildIsNullException(){}
    public GuildIsNullException(String guildID)
    {
        System.err.println("GuildID={" + guildID + "}");
    }
}

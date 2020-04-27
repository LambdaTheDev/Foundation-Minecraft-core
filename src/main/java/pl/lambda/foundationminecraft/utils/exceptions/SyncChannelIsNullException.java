package pl.lambda.foundationminecraft.utils.exceptions;

public class SyncChannelIsNullException extends Exception
{
    public SyncChannelIsNullException(){}
    public SyncChannelIsNullException(String channelID)
    {
        System.err.println("SyncChannelID={" + channelID + "}");
    }
}

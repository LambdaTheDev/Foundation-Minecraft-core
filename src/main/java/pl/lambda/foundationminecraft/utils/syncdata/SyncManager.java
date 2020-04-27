package pl.lambda.foundationminecraft.utils.syncdata;

import pl.lambda.foundationminecraft.FoundationMinecraft;

public class SyncManager
{
    public static void startSync(String nickname, String discordID, String code)
    {
        SyncDataStorage syncDataStorage = FoundationMinecraft.getInstance().getSyncDataStorage();
        syncDataStorage.getData().set("sync." + nickname + ".discordID", discordID);
        syncDataStorage.getData().set("sync." + nickname + ".code", code);
        syncDataStorage.save();
    }

    public static void endSync(String nickname)
    {
        SyncDataStorage syncDataStorage = FoundationMinecraft.getInstance().getSyncDataStorage();
        syncDataStorage.getData().set("sync." + nickname, null);
        syncDataStorage.save();
    }

    public static String verifySync(String nickname, String code)
    {
        SyncDataStorage syncDataStorage = FoundationMinecraft.getInstance().getSyncDataStorage();
        String discordID = syncDataStorage.getData().getString("sync." + nickname + ".discordID");
        if(discordID == null)
        {
            return null;
        }
        return discordID;
    }

    public static void clearSync()
    {
        SyncDataStorage syncDataStorage = FoundationMinecraft.getInstance().getSyncDataStorage();
        syncDataStorage.getData().set("sync", null);
        syncDataStorage.save();
    }
}

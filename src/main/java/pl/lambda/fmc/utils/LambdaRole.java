package pl.lambda.fmc.utils;

import org.bukkit.Location;

import java.util.List;

public class LambdaRole
{
    String lambdaRoleID;
    String discordID;
    String prefix;
    List<String> permissions;
    Location location;

    public LambdaRole(String lambdaRoleID, String discordID, String prefix, List<String> permissions, Location location)
    {
        this.lambdaRoleID = lambdaRoleID;
        this.discordID = discordID;
        this.prefix = prefix;
        this.permissions = permissions;
        this.location = location;
    }

    public String getLambdaRoleID() {
        return lambdaRoleID;
    }

    public String getDiscordID() {
        return discordID;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Location getLocation() {
        return location;
    }
}

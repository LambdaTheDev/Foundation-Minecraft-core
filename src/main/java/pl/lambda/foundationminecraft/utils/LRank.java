package pl.lambda.foundationminecraft.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.datastorage.RankDataStorage;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LRank
{
    String lambdaRankID;
    String discordID;
    String prefix;
    String color;
    String name;
    String shortcut;
    List<String> permissions;
    Location location;

    public LRank(String lambdaRankID, String discordID, String prefix, String color, String name, String shortcut, List<String> permissions, Location location)
    {
        this.lambdaRankID = lambdaRankID;
        this.discordID = discordID;
        this.prefix = prefix;
        this.color = color;
        this.name = name;
        this.shortcut = shortcut;
        this.permissions = permissions;
        this.location = location;
    }

    public String getLambdaRankID() {
        return lambdaRankID;
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

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getShortcut() {
        return shortcut;
    }

    public static void loadRanks()
    {
        FoundationMinecraft.instance.ranks.clear();
        RankDataStorage rankDataStorage = FoundationMinecraft.instance.getRankDataStorage();
        rankDataStorage.reload();
        FileConfiguration ranksData = rankDataStorage.getData();

        ConfigurationSection ranks = ranksData.getConfigurationSection("ranks");
        if(ranks == null) return;
        for(String rank : ranks.getKeys(false))
        {
            String color = ranksData.getString("ranks." + rank + ".color");
            String shortcut = ranksData.getString("ranks." + rank + ".shortcut");
            String name = ranksData.getString("ranks." + rank + ".name");
            String discordID = ranksData.getString("ranks." + rank + ".discordid");
            List<String> permissions = ranksData.getStringList("ranks." + rank + ".permissions");

            int locationX = ranksData.getInt("ranks." + rank + ".spawnLoc.x");
            int locationY = ranksData.getInt("ranks." + rank + ".spawnLoc.y");
            int locationZ = ranksData.getInt("ranks." + rank + ".spawnLoc.z");
            String locationWorld = ranksData.getString("ranks." + rank + ".spawnLoc.w");
            World locationW = Bukkit.getWorld(locationWorld);

            Location location = new Location(locationW, locationX, locationY, locationZ);
            FoundationMinecraft.instance.ranks.put(rank, new LRank(rank, discordID, shortcut, color, name, shortcut, permissions, location));
        }
    }

    public static void createRank(String name, String shortcut, String discordID, String color)
    {
        RankDataStorage rankDataStorage = FoundationMinecraft.instance.getRankDataStorage();
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".color", color);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".shortcut", shortcut);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".name", name);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".discordid", discordID);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".permissions", Arrays.asList("default"));
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".spawnLoc.x", 0);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".spawnLoc.y", 0);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".spawnLoc.z", 0);
        rankDataStorage.getData().set("ranks." + UUID.randomUUID().toString() + ".spawnLoc.w", "world");
        rankDataStorage.save();
    }
}

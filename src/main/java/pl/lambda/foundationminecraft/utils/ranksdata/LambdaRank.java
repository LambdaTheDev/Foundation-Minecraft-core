package pl.lambda.foundationminecraft.utils.ranksdata;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LambdaRank
{
    String lambdaID;
    String discordID;
    String name;
    String prefix;
    String color;
    ChatColor chatColor;
    Location spawnLocation;
    List<String> permissions;

    private LambdaRank(String lambdaID, String discordID, String name, String prefix, String color, ChatColor chatColor, Location spawnLocation, List<String> permissions)
    {
        this.lambdaID = lambdaID;
        this.discordID = discordID;
        this.name = name;
        this.prefix = prefix;
        this.color = color;
        this.chatColor = chatColor;
        this.spawnLocation = spawnLocation;
        this.permissions = permissions;
    }

    public static LambdaRank getRankByLambdaID(String lambdaID)
    {
        RankDataStorage rankDataStorage = FoundationMinecraft.getInstance().getRankDataStorage();
        if(rankDataStorage.getData().get("ranks." + lambdaID + ".name") != null)
        {
            String colorString = rankDataStorage.getData().getString("ranks." + lambdaID + ".color");
            String prefix = rankDataStorage.getData().getString("ranks." + lambdaID + ".prefix");
            String name = rankDataStorage.getData().getString("ranks." + lambdaID + ".name");
            String discordID = rankDataStorage.getData().getString("ranks." + lambdaID + ".discordID");
            List<String> permissions = rankDataStorage.getData().getStringList("ranks." + lambdaID + ".permissions");

            int spawnLocX = rankDataStorage.getData().getInt("ranks." + lambdaID + ".spawnLoc.x");
            int spawnLocY = rankDataStorage.getData().getInt("ranks." + lambdaID + ".spawnLoc.y");
            int spawnLocZ = rankDataStorage.getData().getInt("ranks." + lambdaID + ".spawnLoc.z");
            String spawnLocWString = rankDataStorage.getData().getString("ranks." + lambdaID + ".spawnLoc.w");
            World spawnLocW;

            if(Bukkit.getWorld(spawnLocWString) == null)
            {
                spawnLocW = Bukkit.getWorld(spawnLocWString);
            }
            else
            {
                spawnLocW = Bukkit.getWorlds().get(0);
            }

            Location spawnLoc = new Location(spawnLocW, spawnLocX, spawnLocY, spawnLocZ);

            return new LambdaRank(lambdaID, discordID, name.toLowerCase(), prefix, colorString, ChatColor.getByChar(colorString), spawnLoc, permissions);
        }
        System.out.println("xxx");
        return null;
    }

    public static LambdaRank getRankByName(String name)
    {
        name = name.toLowerCase();
        RankDataStorage rankDataStorage = FoundationMinecraft.getInstance().getRankDataStorage();
        ConfigurationSection section = rankDataStorage.getData().getConfigurationSection("ranks");
        if(section == null) return null;
        for(String lambdaID : section.getKeys(false))
        {
            String rankName = rankDataStorage.getData().getString("ranks." + lambdaID + ".name");
            if(rankName.toLowerCase().equalsIgnoreCase(name))
            {
                return getRankByLambdaID(lambdaID);
            }
        }
        return null;
    }

    public static LambdaRank getRankByDiscordID(String discordID)
    {
        RankDataStorage rankDataStorage = FoundationMinecraft.getInstance().getRankDataStorage();
        ConfigurationSection section = rankDataStorage.getData().getConfigurationSection("ranks");
        if(section == null) return null;
        for(String lambdaID : section.getKeys(false))
        {
            if(rankDataStorage.getData().getString("ranks." + lambdaID + ".discordID").equals(discordID))
            {
                return getRankByLambdaID(lambdaID);
            }
        }
        return null;
    }

    public static void loadRanks()
    {
        RankDataStorage rankDataStorage = FoundationMinecraft.getInstance().getRankDataStorage();
        rankDataStorage.reload();
        ConfigurationSection section = rankDataStorage.getData().getConfigurationSection("ranks");
        if(section == null) return;
        FoundationMinecraft.getInstance().getLambdaRanks().clear();

        for(String rankID : section.getKeys(false))
        {
            LambdaRank rank = LambdaRank.getRankByLambdaID(rankID);
            if(rank == null)
            {
                System.out.println("Rank with LambdaID=" + rankID + " is unreachable.");
                continue;
            }
            FoundationMinecraft.getInstance().getLambdaRanks().add(rank);
        }
    }

    public static LambdaRank createLambdaRank(String discordID, String name, String prefix, String color)
    {
        LambdaRank lambdaRank = new LambdaRank(UUID.randomUUID().toString(), discordID, name, prefix, color, ChatColor.getByChar(color), new Location(Bukkit.getWorld("world"), 0, 0, 0), new ArrayList<>());
        lambdaRank.save();
        FoundationMinecraft.getInstance().getLambdaRanks().add(lambdaRank);
        return lambdaRank;
    }

    public void deleteRank()
    {
        RankDataStorage rankDataStorage = FoundationMinecraft.getInstance().getRankDataStorage();
        if(rankDataStorage.getData().getString("ranks." + this.lambdaID + ".name") != null)
        {
            rankDataStorage.getData().set("ranks." + this.lambdaID, null);
            rankDataStorage.save();
        }
        FoundationMinecraft.getInstance().getLambdaRanks().remove(this);
    }

    public void save()
    {
        RankDataStorage rankDataStorage = FoundationMinecraft.getInstance().getRankDataStorage();
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".color", this.color);
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".prefix", this.prefix);
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".name", this.name);
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".discordID", this.discordID);
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".permissions", this.permissions);
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".spawnLoc.x", this.spawnLocation.getBlockX());
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".spawnLoc.y", this.spawnLocation.getBlockY());
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".spawnLoc.z", this.spawnLocation.getBlockZ());
        rankDataStorage.getData().set("ranks." + this.lambdaID + ".spawnLoc.w", this.spawnLocation.getWorld().getName());
        rankDataStorage.save();
    }

    public String getLambdaID() {
        return lambdaID;
    }

    public String getDiscordID() {
        return discordID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "LambdaRank{" +
                "lambdaID='" + lambdaID + '\'' +
                ", discordID='" + discordID + '\'' +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", color='" + color + '\'' +
                ", chatColor=" + chatColor.name() +
                ", spawnLocation=" + spawnLocation +
                ", permissions=" + permissions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LambdaRank that = (LambdaRank) o;
        return lambdaID.equals(that.lambdaID) &&
                discordID.equals(that.discordID) &&
                name.equals(that.name) &&
                prefix.equals(that.prefix) &&
                color.equals(that.color) &&
                chatColor == that.chatColor &&
                Objects.equals(spawnLocation, that.spawnLocation) &&
                Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lambdaID, discordID, name, prefix, color, chatColor, spawnLocation, permissions);
    }
}

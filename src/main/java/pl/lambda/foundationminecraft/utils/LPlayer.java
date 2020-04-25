package pl.lambda.foundationminecraft.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.datastorage.PlayerDataStorage;
import pl.lambda.foundationminecraft.utils.exceptions.GuildIDIsNullException;
import pl.lambda.foundationminecraft.utils.exceptions.MemberIsNullException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LPlayer
{
    String nickname;
    String lambdaPlayerID;
    String password;
    String discordID;
    ChatType chatType;
    int clearance;
    int money;
    boolean isSiteDirector;
    LRank currentRank;
    PermissionAttachment permissionAttachment;
    List<LRank> lambdaRanks;
    List<String> playerPermissions;

    private LPlayer(String nickname, String lambdaPlayerID, String password, String discordID, ChatType chatType, int clearance, int money, boolean isSiteDirector, PermissionAttachment permissionAttachment, List<LRank> lambdaRanks)
    {
        this.nickname = nickname;
        this.lambdaPlayerID = lambdaPlayerID;
        this.password = password;
        this.discordID = discordID;
        this.chatType = chatType;
        this.clearance = clearance;
        this.money = money;
        this.isSiteDirector = isSiteDirector;
        this.permissionAttachment = permissionAttachment;
        this.lambdaRanks = lambdaRanks;
        this.currentRank = null;

        List<String> playerPermissions = new ArrayList<>();
        for(LRank role : lambdaRanks)
        {
            List<String> permissions = role.getPermissions();
            for(String permission : permissions)
            {
                permissionAttachment.setPermission(permission, true);
            }
        }

        this.playerPermissions = playerPermissions;
    }

    //<section-fold desc="getters and setters">

    @Override
    public String toString() {
        return "LPlayer{" +
                "nickname='" + nickname + '\'' +
                ", lambdaPlayerID='" + lambdaPlayerID + '\'' +
                ", password='" + password + '\'' +
                ", discordID='" + discordID + '\'' +
                ", chatType=" + chatType +
                ", clearance=" + clearance +
                ", money=" + money +
                ", isSiteDirector=" + isSiteDirector +
                ", currentRank=" + currentRank +
                ", permissionAttachment=" + permissionAttachment +
                ", lambdaRanks=" + lambdaRanks +
                ", playerPermissions=" + playerPermissions +
                '}';
    }

    public String getNickname() {
        return nickname;
    }

    public String getLambdaPlayerID() {
        return lambdaPlayerID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getClearance() {
        return clearance;
    }

    public void setClearance(int clearance) {
        this.clearance = clearance;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ChatType getChatType()
    {
        return chatType;
    }

    public void setChatType(ChatType chatType)
    {
        this.chatType = chatType;
    }

    public LRank getCurrentRank() {
        return currentRank;
    }

    public String getDiscordID() {
        return discordID;
    }

    public void setDiscordID(String discordID) {
        this.discordID = discordID;
    }

    public boolean isSiteDirector() {
        return isSiteDirector;
    }

    public void setCurrentRank(LRank currentRank)
    {
        this.currentRank = currentRank;
    }

    public PermissionAttachment getPermissionAttachment() {
        return permissionAttachment;
    }

    public List<LRank> getLambdaRanks() {
        return lambdaRanks;
    }

    public void setLambdaRanks(List<LRank> lambdaRanks) {
        this.lambdaRanks = lambdaRanks;
    }

    public void saveToFile()
    {
        List<String> lambdaRankIDs = new ArrayList<>();

        for(LRank rank : getLambdaRanks())
        {
            lambdaRankIDs.add(rank.lambdaRankID);
        }

        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        playerDataStorage.getData().set("players." + this.lambdaPlayerID + ".nickname", this.nickname);
        playerDataStorage.getData().set("players." + this.lambdaPlayerID + ".password", this.password);
        playerDataStorage.getData().set("players." + this.lambdaPlayerID + ".discordid", this.discordID);
        playerDataStorage.getData().set("players." + this.lambdaPlayerID + ".money", this.money);
        playerDataStorage.getData().set("players." + this.lambdaPlayerID + ".lambdaRanks", lambdaRankIDs);
        playerDataStorage.save();
    }

    public static LPlayer getLambdaPlayerByLambdaID(String lambdaID)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();

        String nickname = playerDataStorage.getData().getString("players." + lambdaID + ".nickname");
        if(nickname == null)
        {
            return null;
        }

        Player p = Bukkit.getPlayer(nickname);
        if(p != null)
        {
            return FoundationMinecraft.instance.lambdaPlayers.get(p);
        }

        String password = playerDataStorage.getData().getString("players." + lambdaID + ".password");
        String discordID = playerDataStorage.getData().getString("players." + lambdaID + ".discordid");
        int money = playerDataStorage.getData().getInt("players." + lambdaID + ".money");
        List<LRank> lambdaRanks = new ArrayList<>();

        List<String> lambdaRanksString = playerDataStorage.getData().getStringList("players." + lambdaID + ".lambdaRanks");
        for(String lambdaRank : lambdaRanksString)
        {
            LRank lRank = FoundationMinecraft.instance.ranks.get(lambdaRank);
            lambdaRanks.add(lRank);
        }

        LPlayer lPlayer = new LPlayer(nickname, lambdaID, password, discordID, ChatType.GLOBAL, getClearance(lambdaID), money, isSiteDirector(lambdaID), null, lambdaRanks);
        return lPlayer;
    }

    public static LPlayer getLambdaPlayerByNickname(String nickname)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        ConfigurationSection section = playerDataStorage.getData().getConfigurationSection("players");
        if(section == null) return null;

        String lambdaID = null;
        for(String loopedLambdaID : section.getKeys(false))
        {
            if(playerDataStorage.getData().getString("players." + loopedLambdaID + ".nickname").equalsIgnoreCase(nickname))
            {
                lambdaID = loopedLambdaID;
                break;
            }
        }

        if(lambdaID == null)
        {
            return null;
        }

        Player p = Bukkit.getPlayer(nickname);
        if(p != null)
        {
            return FoundationMinecraft.instance.lambdaPlayers.get(p);
        }

        String password = playerDataStorage.getData().getString("players." + lambdaID + ".password");
        String discordID = playerDataStorage.getData().getString("players." + lambdaID + ".discordid");
        int money = playerDataStorage.getData().getInt("players." + lambdaID + ".money");
        List<LRank> lambdaRanks = new ArrayList<>();

        List<String> lambdaRanksString = playerDataStorage.getData().getStringList("players." + lambdaID + ".lambdaRanks");
        for(String lambdaRank : lambdaRanksString)
        {
            LRank lRank = FoundationMinecraft.instance.ranks.get(lambdaRank);
            lambdaRanks.add(lRank);
        }

        LPlayer lPlayer = new LPlayer(nickname, lambdaID, password, discordID, ChatType.GLOBAL, getClearance(lambdaID), money, isSiteDirector(lambdaID), null, lambdaRanks);
        return lPlayer;
    }

    public static LPlayer createLambdaPlayer(String nickname)
    {
        //String nickname, String lambdaPlayerID, String password, String discordID, ChatType chatType, int clearance,
        //int money, boolean isSiteDirector, PermissionAttachment permissionAttachment, List<LRank> lambdaRanks

        String lambdaID = UUID.randomUUID().toString();
        String password = "not-implemented";
        String discordID = null;
        ChatType type = ChatType.GLOBAL;
        int clearance = -1;
        int money = 0;
        boolean isSiteDirector = false;
        PermissionAttachment permissionAttachment = null;
        List<LRank> lambdaRank = new ArrayList<>();

        Player p = Bukkit.getPlayer(nickname);
        if(p != null)
        {
            permissionAttachment = p.addAttachment(FoundationMinecraft.getPlugin(FoundationMinecraft.class));
        }

        LPlayer lPlayer = new LPlayer(nickname, lambdaID, password, discordID, type, clearance, money, isSiteDirector, permissionAttachment, lambdaRank);
        return lPlayer;
    }

    public static int getClearance(String lambdaID)
    {
        Config config = FoundationMinecraft.instance.getFMCConfig();
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        DiscordModule discordModule = FoundationMinecraft.instance.discordModule;
        String discordID = playerDataStorage.getData().getString("players." + lambdaID + ".discordid");
        if(discordID == null) return -1;

        Guild guild = discordModule.jda.getGuildById(config.guildID);
        if(guild == null)
        {
            try {
                throw new GuildIDIsNullException();
            } catch (GuildIDIsNullException e) {
                e.printStackTrace();
                return -1;
            }
        }

        if(guild.getMemberById(discordID) == null)
        {
            try
            {
                throw new MemberIsNullException();
            }
            catch (MemberIsNullException e)
            {
                e.printStackTrace();
                return -1;
            }
        }

        Member m = guild.getMemberById(discordID);
        HashMap<String, Integer> clearanceRoleIDs = new HashMap<>();
        clearanceRoleIDs.put(config.level0role, 0);
        clearanceRoleIDs.put(config.level1role, 1);
        clearanceRoleIDs.put(config.level2role, 2);
        clearanceRoleIDs.put(config.level3role, 3);
        clearanceRoleIDs.put(config.level4role, 4);
        clearanceRoleIDs.put(config.level5role, 5);

        List<Role> memberRoles;
        if (m != null)
        {
            memberRoles = m.getRoles();
        }
        else
        {
            return -1;
        }

        int clearance = -1;

        for(Role r : memberRoles)
        {
            String roleID = r.getId();
            if(clearanceRoleIDs.containsKey(roleID))
            {
                if(clearanceRoleIDs.get(roleID) > clearance)
                {
                    clearance = clearanceRoleIDs.get(roleID);
                }
            }
        }
        return clearance;
    }

    public static boolean isSiteDirector(String lambdaID)
    {
        Config config = FoundationMinecraft.instance.getFMCConfig();
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        DiscordModule discordModule = FoundationMinecraft.instance.discordModule;
        String discordID = playerDataStorage.getData().getString("players." + lambdaID + ".discordid");
        if(discordID == null) return false;

        Guild guild = discordModule.jda.getGuildById(config.guildID);
        if(guild == null)
        {
            try {
                throw new GuildIDIsNullException();
            } catch (GuildIDIsNullException e) {
                e.printStackTrace();
                return false;
            }
        }

        if(guild.getMemberById(discordID) == null)
        {
            try
            {
                throw new MemberIsNullException();
            }
            catch (MemberIsNullException e)
            {
                e.printStackTrace();
                return false;
            }
        }

        Member m = guild.getMemberById(discordID);
        String siteDirectorRoleID = config.siteDirectorRole;

        List<Role> memberRoles;
        if (m != null)
        {
            memberRoles = m.getRoles();
        }
        else
        {
            return false;
        }

        for(Role r : memberRoles)
        {
            String roleID = r.getId();
            if(roleID.equalsIgnoreCase(siteDirectorRoleID)) return true;
        }
        return false;
    }

    public static boolean hasPlayedBefore(String nickname)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        ConfigurationSection section = playerDataStorage.getData().getConfigurationSection("players");
        if(section == null) return false;
        for(String lambdaID : section.getKeys(false))
        {
            if(playerDataStorage.getData().getString("players." + lambdaID + ".nickname").equalsIgnoreCase(nickname))
            {
                return true;
            }
        }
        return false;
    }

    public static String getLambdaID(String nickname)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        ConfigurationSection section = playerDataStorage.getData().getConfigurationSection("players");
        if(section == null) return null;
        for(String lambdaID : section.getKeys(false))
        {
            if(playerDataStorage.getData().getString("players." + lambdaID + ".nickname").equalsIgnoreCase(nickname))
            {
                return lambdaID;
            }
        }
        return null;
    }
}


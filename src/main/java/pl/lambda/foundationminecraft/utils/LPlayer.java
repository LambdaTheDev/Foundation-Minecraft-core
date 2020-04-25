package pl.lambda.foundationminecraft.utils;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.datastorage.PlayerDataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public LPlayer(String nickname, String lambdaPlayerID, String password, String discordID, ChatType chatType, int clearance, int money, boolean isSiteDirector, PermissionAttachment permissionAttachment, List<LRank> lambdaRanks)
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

    public static LPlayer loadByLambdaID(String lambdaID, String nickname, Player p)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        Config config = FoundationMinecraft.instance.getFMCConfig();

        if(playerDataStorage.getData().get("players." + lambdaID + ".nickname") == null)
        {
            //create new Lambda Player
            PermissionAttachment attachment = p.addAttachment(FoundationMinecraft.getPlugin(FoundationMinecraft.class));
            LPlayer lPlayer = new LPlayer(nickname, lambdaID, "not-implemented", null, ChatType.GLOBAL, -1, 0, false, attachment, new ArrayList<LRank>());
            lPlayer.saveToFile();
            FoundationMinecraft.instance.lambdaPlayers.put(p, lPlayer);
            return lPlayer;
        }

        String discordID = playerDataStorage.getData().getString("players." + lambdaID + ".discordid");
        int money = playerDataStorage.getData().getInt("players." + lambdaID + ".money");
        List<String> lambdaRankIDs = playerDataStorage.getData().getStringList("players." + lambdaID + ".lambdaRanks");

        int clearance = -1;
        boolean isSiteDirector = false;
        JDA jda = FoundationMinecraft.instance.discordModule.jda;
        User u = jda.getUserById(discordID);
        if(u == null)
        {
            discordID = null;
        }
        else
        {
            Guild guild = jda.getGuildById(config.guildID);
            if(guild == null)
            {
                discordID = null;
            }
            else
            {
                List<Role> userRoles = Objects.requireNonNull(guild.getMember(u)).getRoles();
                if(!userRoles.isEmpty())
                {
                    HashMap<String, Integer> roleIDs = new HashMap<>();
                    roleIDs.put(config.level0role, 0);
                    roleIDs.put(config.level1role, 1);
                    roleIDs.put(config.level2role, 2);
                    roleIDs.put(config.level3role, 3);
                    roleIDs.put(config.level4role, 4);
                    roleIDs.put(config.level5role, 5);

                    String siteDirectorRoleID = config.siteDirectorRole;

                    for(Role role : userRoles)
                    {
                        if(roleIDs.getOrDefault(role.getId(), -1) > clearance)
                        {
                            clearance = roleIDs.get(role.getId());
                        }

                        if(role.getId().equalsIgnoreCase(siteDirectorRoleID))
                        {
                            isSiteDirector = true;
                        }
                    }
                }
            }
        }

        PermissionAttachment attachment = p.addAttachment(FoundationMinecraft.getPlugin(FoundationMinecraft.class));
        List<LRank> ranks = new ArrayList<>();

        for(String lambdaRankID : lambdaRankIDs)
        {
            if(FoundationMinecraft.instance.ranks.get(lambdaRankID) != null)
            {
                ranks.add(FoundationMinecraft.instance.ranks.get(lambdaRankID));
            }
        }

        LPlayer lPlayer = new LPlayer(nickname, lambdaID, "not-implemented", discordID, ChatType.GLOBAL, clearance, money, isSiteDirector, attachment, ranks);
        FoundationMinecraft.instance.lambdaPlayers.put(p, lPlayer);
        return lPlayer;
    }

    public static boolean hasPlayedBefore(String nickname)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.instance.getPlayerDataStorage();
        ConfigurationSection section = playerDataStorage.getData().getConfigurationSection("players");
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


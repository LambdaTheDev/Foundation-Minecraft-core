package pl.lambda.foundationminecraft.utils.playerdata;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Config;
import pl.lambda.foundationminecraft.utils.enums.ChatType;
import pl.lambda.foundationminecraft.utils.exceptions.MemberIsNullException;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LambdaPlayer
{
    Player player;
    String nickname;
    String discordID;
    ChatType chatType;
    boolean isSiteDirector;
    boolean isGoiHighRank;
    int money;
    int clearance;
    LambdaRank currentLambdaRank;
    List<LambdaRank> lambdaRanks;

    private LambdaPlayer(Player player, String discordID, boolean isSiteDirector, boolean isGoIHighRank, int money, int clearance, List<LambdaRank> lambdaRanks)
    {
        this.player = player;
        this.discordID = discordID;
        this.nickname = player.getName();
        this.chatType = ChatType.LOCAL;
        this.isSiteDirector = isSiteDirector;
        this.isGoiHighRank = isGoIHighRank;
        this.money = money;
        this.clearance = clearance;
        this.currentLambdaRank = null;
        this.lambdaRanks = lambdaRanks;
    }

    public void save()
    {
        List<String> rankLambdaIDs = new ArrayList<>();
        for(LambdaRank lambdaRank : this.lambdaRanks)
        {
            rankLambdaIDs.add(lambdaRank.getLambdaID());
        }

        PlayerDataStorage playerDataStorage = FoundationMinecraft.getInstance().getPlayerDataStorage();
        playerDataStorage.getData().set("players." + this.player.getUniqueId().toString(), null);
        playerDataStorage.getData().set("players." + this.player.getUniqueId().toString() + ".money", this.money);
        playerDataStorage.getData().set("players." + this.player.getUniqueId().toString() + ".nickname", this.nickname);
        playerDataStorage.getData().set("players." + this.player.getUniqueId().toString() + ".discordID", this.discordID);
        playerDataStorage.getData().set("players." + this.player.getUniqueId().toString() + ".lambdaRanks", rankLambdaIDs);
        playerDataStorage.save();
    }

    public static LambdaPlayer getLambdaPlayerByUUID(UUID uuid)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.getInstance().getPlayerDataStorage();
        Config config = FoundationMinecraft.getInstance().getFmcConfig();
        if(playerDataStorage.getData().getString("players." + uuid.toString() + ".nickname") != null)
        {
            int money = playerDataStorage.getData().getInt("players." + uuid.toString() + ".money");
            String discordID = playerDataStorage.getData().getString("players." + uuid.toString() + ".discordID");
            List<String> lambdaRanksString = playerDataStorage.getData().getStringList("players." + uuid.toString() + ".lambdaRanks");

            Player player;
            boolean isSiteDirector = false;
            boolean isGoIHighRank = false;
            int clearance = -1;

            if(discordID != null)
            {
                Guild guild = FoundationMinecraft.getInstance().getDiscordModule().getGuild();
                Member member = guild.getMemberById(discordID);
                try { if (member == null) throw new MemberIsNullException(); }
                catch (MemberIsNullException e)
                { e.printStackTrace(); return null; }

                List<Role> memberRoles = member.getRoles();
                for(Role role : memberRoles)
                {
                    String roleID = role.getId();
                    HashMap<String, Integer> clearanceLevelRoles = config.getLevelRoles();
                    String sidRoleID = config.getSiteDirectorRole();
                    String goiHRRoleID = config.getGoiHighRankRole();
                    int roleClearance = clearanceLevelRoles.getOrDefault(roleID, -1);
                    if(roleClearance > clearance) clearance = roleClearance;
                    if(roleID.equals(sidRoleID)) isSiteDirector = true;
                    if(roleID.equals(goiHRRoleID)) isGoIHighRank = true;
                }
            }

            player = (Player) Bukkit.getOfflinePlayer(uuid);
            if(player == null) return null;

            List<LambdaRank> lambdaRanks = new ArrayList<>();
            for(String lambdaRankID : lambdaRanksString)
            {
                LambdaRank rank = LambdaRank.getRankByLambdaID(lambdaRankID);
                if(rank != null) lambdaRanks.add(rank);
            }

            return new LambdaPlayer(player, discordID, isSiteDirector, isGoIHighRank, money, clearance, lambdaRanks);
        }
        return null;
    }

    public static LambdaPlayer getLambdaPlayerByNickname(String nickname)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.getInstance().getPlayerDataStorage();
        ConfigurationSection section = playerDataStorage.getData().getConfigurationSection("players");
        if(section == null) return null;

        for(String uuid : section.getKeys(false))
        {
            if(playerDataStorage.getData().getString("players." + uuid + ".nickname").equals(nickname))
            {
                return LambdaPlayer.getLambdaPlayerByUUID(UUID.fromString(uuid));
            }
        }
        return null;
    }

    public static LambdaPlayer getLambdaPlayerByDiscord(String discordID)
    {
        PlayerDataStorage playerDataStorage = FoundationMinecraft.getInstance().getPlayerDataStorage();
        ConfigurationSection section = playerDataStorage.getData().getConfigurationSection("players");
        if(section == null) return null;

        for(String uuid : section.getKeys(false))
        {
            if(playerDataStorage.getData().getString("players." + uuid + ".discordID").equals(discordID))
            {
                return LambdaPlayer.getLambdaPlayerByUUID(UUID.fromString(uuid));
            }
        }
        return null;
    }

    public static LambdaPlayer createLambdaPlayer(Player player)
    {
        return new LambdaPlayer(player, null, false, false, 0, -1, new ArrayList<LambdaRank>());
    }

    public Player getPlayer()
    {
        return player;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDiscordID() {
        return discordID;
    }

    public void setDiscordID(String discordID) {
        this.discordID = discordID;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public boolean isSiteDirector() {
        return isSiteDirector;
    }

    public void setSiteDirector(boolean siteDirector) {
        isSiteDirector = siteDirector;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getClearance() {
        return clearance;
    }

    public void setClearance(int clearance) {
        this.clearance = clearance;
    }

    public LambdaRank getCurrentLambdaRank() {
        return currentLambdaRank;
    }

    public void setCurrentLambdaRank(LambdaRank currentLambdaRank) {
        this.currentLambdaRank = currentLambdaRank;
    }

    public List<LambdaRank> getLambdaRanks() {
        return lambdaRanks;
    }

    public void setLambdaRanks(List<LambdaRank> lambdaRanks)
    {
        this.lambdaRanks = lambdaRanks;
    }

    public boolean isGoiHighRank() {
        return isGoiHighRank;
    }

    public void setGoiHighRank(boolean goiHighRank) {
        isGoiHighRank = goiHighRank;
    }

    @Override
    public String toString() {
        return "LambdaPlayer{" +
                "player=" + player +
                ", nickname='" + nickname + '\'' +
                ", discordID='" + discordID + '\'' +
                ", chatType=" + chatType +
                ", isSiteDirector=" + isSiteDirector +
                ", money=" + money +
                ", clearance=" + clearance +
                ", currentLambdaRank=" + currentLambdaRank +
                ", lambdaRanks=" + lambdaRanks +
                '}';
    }
}

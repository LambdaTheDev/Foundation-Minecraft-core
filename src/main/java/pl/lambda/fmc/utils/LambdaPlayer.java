package pl.lambda.fmc.utils;

import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.List;

public class LambdaPlayer
{
    String nickname;
    String lambdaPlayerID;
    String password;
    boolean chatType;
    int clearance;
    int money;
    PermissionAttachment permissionAttachment;
    List<LambdaRole> lambdaRoles;
    List<String> playerPermissions;

    public LambdaPlayer(String nickname, String lambdaPlayerID, String password, boolean chatType, int clearance, int money, PermissionAttachment permissionAttachment, List<LambdaRole> lambdaRoles)
    {
        this.nickname = nickname;
        this.lambdaPlayerID = lambdaPlayerID;
        this.password = password;
        this.chatType = chatType;
        this.clearance = clearance;
        this.money = money;
        this.permissionAttachment = permissionAttachment;
        this.lambdaRoles = lambdaRoles;

        List<String> playerPermissions = new ArrayList<>();
        for(LambdaRole role : lambdaRoles)
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

    public PermissionAttachment getPermissionAttachment() {
        return permissionAttachment;
    }

    public List<LambdaRole> getLambdaRoles() {
        return lambdaRoles;
    }

    public void setLambdaRoles(List<LambdaRole> lambdaRoles) {
        this.lambdaRoles = lambdaRoles;
    }
}

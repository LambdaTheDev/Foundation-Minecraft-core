package pl.lambda.fmc.discord.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.lambda.fmc.utils.ConfigAPI;
import pl.lambda.fmc.utils.RoleAPI;
import pl.lambda.fmc.utils.UsersAPI;

import java.util.List;
import java.util.Map;

public class OnGuildMemberRoleRemove extends ListenerAdapter
{
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent e)
    {
        RoleAPI roleAPI = new RoleAPI();
        UsersAPI usersAPI = new UsersAPI();
        ConfigAPI configAPI = new ConfigAPI();

        String uuid = usersAPI.getUserUUIDByDiscord(e.getMember().getId());
        List<Role> roles = e.getRoles();

        Map<Integer, String> clearanceIDs = configAPI.getAllClearanceRoleID();
        int currentClearance = 0;

        for (Role r : roles)
        {
            String roleid = r.getId();
            String classid = roleAPI.getRoleUUIDByDiscord(roleid);
            if (!(classid == null))
            {
                usersAPI.deleteRoles(uuid, classid);
            }
        }

        List<Role> memberRoles = e.getMember().getRoles();
        for(Role r : memberRoles)
        {
            String roleid = r.getId();
            if(clearanceIDs.containsValue(roleid))
            {
                int roleclearance = ConfigAPI.getMapKey(clearanceIDs, roleid);
                if(roleclearance > currentClearance) currentClearance = roleclearance;
            }

            if(roleid.equals(configAPI.getSiteDirectorRoleID()))
            {
                usersAPI.setSiteDirector(uuid, false);
            }
        }

        usersAPI.setClearance(uuid, currentClearance);
    }
}

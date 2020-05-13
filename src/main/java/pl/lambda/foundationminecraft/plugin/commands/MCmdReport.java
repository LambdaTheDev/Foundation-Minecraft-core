package pl.lambda.foundationminecraft.plugin.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.discord.DiscordModule;
import pl.lambda.foundationminecraft.utils.Utils;

public class MCmdReport implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("report")) return false;

        if(args.length == 0 || args.length == 1)
        {
            sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Incorrect usage! Correct: /report <IGN> <reason>!");
            return false;
        }

        String nickname = args[0];
        String reportMessage = Utils.argsBuilder(args, 2);

        DiscordModule module = FoundationMinecraft.getInstance().getDiscordModule();

        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.isOp())
            {
                p.sendMessage(" ");
                p.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Report from: " + sender.getName());
                p.sendMessage(ChatColor.GOLD + "Reported player: " + nickname);
                p.sendMessage(ChatColor.GOLD + "Reason: " + reportMessage);
                p.sendMessage(" ");
            }
        }

        for(Member m : module.getGuild().getMembers())
        {
            if(m.hasPermission(Permission.ADMINISTRATOR))
            {
                User u = m.getUser();
                u.openPrivateChannel().queue((channel -> {
                    channel.sendMessage("Foundation Minecraft: Report").queue();
                    channel.sendMessage("Report from: " + sender.getName()).queue();
                    channel.sendMessage("Reported player: " + nickname).queue();
                    channel.sendMessage("Reason: " + reportMessage).queue();
                }));
            }
        }

        sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Your report has been submitted successfully!");

        return false;
    }
}

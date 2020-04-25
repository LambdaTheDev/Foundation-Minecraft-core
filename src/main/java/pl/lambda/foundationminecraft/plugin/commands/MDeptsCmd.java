package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.LPlayer;
import pl.lambda.foundationminecraft.utils.LRank;
import pl.lambda.foundationminecraft.utils.Utils;

import java.util.HashMap;
import java.util.List;

public class MDeptsCmd implements CommandExecutor, ILMinecraftCommand
{
    Inventory inv;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("depts")) return false;

        String prefix = ChatColor.translateAlternateColorCodes('&', FoundationMinecraft.instance.getFMCConfig().messagePrefix);

        if(FoundationMinecraft.checkIfDeprecated(this.getClass()) != null)
        {
            sender.sendMessage(prefix + ChatColor.RED + FoundationMinecraft.checkIfDeprecated(this.getClass()));
        }

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Player p = (Player) sender;
        LPlayer lp = FoundationMinecraft.instance.lambdaPlayers.get(p);

        if(args.length == 0)
        {
            openMainMenu(p);
        }
        else if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("choose"))
            {
                inv = Bukkit.createInventory(null, 14, "Choose your department");

                List<LRank> playerRanks = lp.getLambdaRanks();
                for(LRank rank : playerRanks)
                {
                    String name = rank.getName();
                    String color = rank.getColor();
                    DyeColor dyeColor = Utils.getDyeFromColor(color.charAt(0));

                    ItemStack item = Utils.createWool(dyeColor, name);
                    inv.addItem(item);
                }

                openInventory(p, inv);
            }
            else if(args[0].equalsIgnoreCase("balance"))
            {
                Bukkit.dispatchCommand(p, "money");
            }
            else if(args[0].equalsIgnoreCase("info"))
            {
                sender.sendMessage(prefix + ChatColor.GOLD + "Foundation Minecraft plugin v." + FoundationMinecraft.VERSION);
                sender.sendMessage(ChatColor.GREEN + FoundationMinecraft.INFO);
            }
            else
            {
                openMainMenu(p);
            }
        }
        else if(args.length == 2)
        {
            if(args[0].equalsIgnoreCase("choose"))
            {
                String lambdaRankID = args[1];
                HashMap<String, LRank> ranks = FoundationMinecraft.instance.ranks;

                LRank rank = ranks.get(lambdaRankID);
                if(rank == null)
                {
                    sender.sendMessage(prefix + ChatColor.RED + "Wrong role ID. If you think that's an error, contact staff.");
                    return false;
                }

                if(!lp.getLambdaRanks().contains(rank))
                {
                    sender.sendMessage(prefix + ChatColor.DARK_RED + "You are not allowed to set that role!");
                    return false;
                }

                lp.setCurrentRank(rank);
                sender.sendMessage(prefix + ChatColor.GREEN + "You set your current rank to: " + rank.getName() + "!");
            }
            else
            {
                openMainMenu(p);
            }
        }
        else
        {
            openMainMenu(p);
        }

        return false;
    }

    void openMainMenu(Player p)
    {
        ItemStack item1 = Utils.createWool(DyeColor.GREEN, "Choose department");
        ItemStack item2 = Utils.createWool(DyeColor.YELLOW, "Your balance");
        ItemStack item3 = Utils.createWool(DyeColor.LIGHT_BLUE, "Plugin info");

        inv = Bukkit.createInventory(null, 7, "FMC: Main menu");
        inv.setItem(1, item1);
        inv.setItem(3, item2);
        inv.setItem(5, item3);

        openInventory(p, inv);
    }

    void openInventory(Player p, Inventory inv)
    {
        p.closeInventory();
        p.openInventory(inv);
    }

    @Override
    public String getUsageMessage(String usage)
    {
        return FoundationMinecraft.getUsageMessage("/depts");
    }

    @Override
    public String getNoPermissionMessage() {
        return null;
    }
}

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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.Utils;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.ArrayList;
import java.util.List;

public class MCmdDepts implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("depts")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        LambdaPlayer lambdaPlayer = FoundationMinecraft.getInstance().getLambdaPlayers().get(sender);
        Inventory inventory;

        if(args.length == 0)
        {
            inventory = createMainMenu();
            ((Player) sender).closeInventory();
            ((Player) sender).openInventory(inventory);
        }
        else if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("choose"))
            {
                List<LambdaRank> lambdaRanks = lambdaPlayer.getLambdaRanks();

                if(lambdaRanks.isEmpty())
                {
                    sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You don't have any roles now! If you think that's an error, rejoin.");
                    return false;
                }

                int size = 27;
                Inventory gui = Bukkit.createInventory(null, size, "FMC: Choose department");
                int i = 0;

                System.out.println(lambdaRanks.toString());
                for(LambdaRank rank : lambdaRanks)
                {
                    DyeColor color = Utils.getDyeColor(rank.getColor());
                    ItemStack item = new Wool(color).toItemStack(1);
                    ItemMeta meta = item.getItemMeta();

                    meta.setDisplayName(rank.getName());
                    item.setItemMeta(meta);

                    gui.setItem(i, item);
                    i++;
                }

                ((Player) sender).closeInventory();
                ((Player) sender).openInventory(gui);
            }
            else if(args[0].equalsIgnoreCase("balance"))
            {
                Bukkit.dispatchCommand(sender, "money");
            }
            else if(args[0].equalsIgnoreCase("info"))
            {
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GOLD + "FMC plugin version " + FoundationMinecraft.VERSION);
                sender.sendMessage(ChatColor.BLUE + FoundationMinecraft.PLUGIN_INFO);
            }
            else
            {
                inventory = createMainMenu();
                ((Player) sender).closeInventory();
                ((Player) sender).openInventory(inventory);
            }
        }
        else if(args.length == 2)
        {
            if(!args[0].equalsIgnoreCase("choose"))
            {
                inventory = createMainMenu();
                ((Player) sender).closeInventory();
                ((Player) sender).openInventory(inventory);
                return false;
            }

            String rankID = args[1];
            LambdaRank checkRank = LambdaRank.getRankByLambdaID(rankID);
            if(checkRank == null)
            {
                inventory = createMainMenu();
                ((Player) sender).closeInventory();
                ((Player) sender).openInventory(inventory);
                return false;
            }

            List<String> playerRankLambdaIDs = new ArrayList<>();
            for(LambdaRank lambdaRank : FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).getLambdaRanks())
            {
                playerRankLambdaIDs.add(lambdaRank.getLambdaID());
            }

            if(playerRankLambdaIDs.contains(checkRank.getLambdaID()))
            {
                FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).setCurrentLambdaRank(checkRank);
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "Your current rank updated successfully!");
            }
            else
            {
                System.out.println(FoundationMinecraft.getInstance().getLambdaPlayers().get(sender).getLambdaRanks().toString());
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You don't have access to set this role!");
            }
        }
        else
        {
            inventory = createMainMenu();
            ((Player) sender).closeInventory();
            ((Player) sender).openInventory(inventory);
        }

        return false;
    }

    Inventory createMainMenu()
    {
        Inventory inventory = Bukkit.createInventory(null, 9, "FMC: Main menu");

        ItemStack choose = new Wool(DyeColor.GREEN).toItemStack(1);
        ItemStack balance = new Wool(DyeColor.YELLOW).toItemStack(1);
        ItemStack info = new Wool(DyeColor.LIGHT_BLUE).toItemStack(1);


        ItemMeta chooseMeta = choose.getItemMeta();
        ItemMeta balanceMeta = balance.getItemMeta();
        ItemMeta infoMeta = info.getItemMeta();

        chooseMeta.setDisplayName("Choose department");
        balanceMeta.setDisplayName("Check your balance");
        infoMeta.setDisplayName("Info about plugin");

        choose.setItemMeta(chooseMeta);
        balance.setItemMeta(balanceMeta);
        info.setItemMeta(infoMeta);

        inventory.setItem(1, choose);
        inventory.setItem(4, balance);
        inventory.setItem(7, info);

        return inventory;
    }
}

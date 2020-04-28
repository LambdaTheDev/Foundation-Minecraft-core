package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;

import java.util.List;

public class OnInventoryClick implements Listener
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(e.getClickedInventory() == null) return;
        ItemStack item = e.getCurrentItem();
        if(item.getItemMeta() == null) return;
        String inventoryName = e.getClickedInventory().getTitle();
        String itemName = item.getItemMeta().getDisplayName();


        if(inventoryName.equals("FMC: Main menu"))
        {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();

            if(itemName.equalsIgnoreCase("Choose department"))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "depts choose");
            }
            else if(itemName.equalsIgnoreCase("Check your balance"))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "depts balance");
            }
            else if(itemName.equalsIgnoreCase("Info about plugin"))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "depts info");
            }
            else
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "depts");
            }
        }
        else if(inventoryName.equals("FMC: Choose department"))
        {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();

            List<LambdaRank> lambdaRanks = FoundationMinecraft.getInstance().getLambdaRanks();

            boolean found = false;
            for(LambdaRank lambdaRank : lambdaRanks)
            {
                if(lambdaRank.getName().equalsIgnoreCase(itemName))
                {
                    found = true;
                    Bukkit.dispatchCommand(e.getWhoClicked(), "depts choose " + lambdaRank.getLambdaID());
                    break;
                }
            }

            if(!found)
                e.getWhoClicked().sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Provided role has not been found!");
        }
    }
}

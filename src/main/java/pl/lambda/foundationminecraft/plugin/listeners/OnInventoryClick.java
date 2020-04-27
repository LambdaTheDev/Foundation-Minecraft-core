package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
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
        ItemStack item = e.getCurrentItem();
        String inventoryName = e.getClickedInventory().getTitle();
        String itemName = item.getItemMeta().getDisplayName();

        if(inventoryName.equals("FMC: Main menu"))
        {
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

            e.getWhoClicked().closeInventory();
        }
        else if(inventoryName.equals("FMC: Choose department"))
        {
            List<LambdaRank> lambdaRanks = FoundationMinecraft.getInstance().getLambdaRanks();

            boolean found = false;
            for(LambdaRank lambdaRank : lambdaRanks)
            {
                if(lambdaRank.getName().equalsIgnoreCase(itemName))
                {
                    found = true;
                    Bukkit.dispatchCommand(e.getWhoClicked(), "depts " + lambdaRank.getLambdaID());
                    break;
                }
            }

            if(!found) Bukkit.dispatchCommand(e.getWhoClicked(), "depts");

            e.getWhoClicked().closeInventory();
        }
    }
}

package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.LRank;

import java.util.HashMap;
import java.util.Map;

public class OnInventoryClick implements Listener
{
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if(e.getInventory().getTitle().equalsIgnoreCase("FMC: Main menu"))
        {
            e.getWhoClicked().closeInventory();
            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            if(itemName == null) return;

            if(itemName.equalsIgnoreCase("Choose department"))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "depts choose");
            }
            else if(itemName.equalsIgnoreCase("Your balance"))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "money");
            }
            else if(itemName.equalsIgnoreCase("Plugin info"))
            {
                Bukkit.dispatchCommand(e.getWhoClicked(), "depts info");
            }
        }
        else if(e.getInventory().getTitle().equalsIgnoreCase("Choose your department"))
        {
            e.getWhoClicked().closeInventory();
            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            HashMap<String, LRank> ranks = FoundationMinecraft.instance.ranks;

            for(Map.Entry<String, LRank> rank : ranks.entrySet())
            {
                if(rank.getValue().getName().equalsIgnoreCase(itemName))
                {
                    Bukkit.dispatchCommand(e.getWhoClicked(), "depts choose " + rank.getValue().getLambdaRankID());
                    return;
                }
            }
        }
    }
}

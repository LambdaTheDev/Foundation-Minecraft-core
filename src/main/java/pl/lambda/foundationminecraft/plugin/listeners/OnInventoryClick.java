package pl.lambda.foundationminecraft.plugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.playerdata.LambdaPlayer;
import pl.lambda.foundationminecraft.utils.ranksdata.LambdaRank;
import pl.lambda.foundationminecraft.utils.shopdata.Shop;
import pl.lambda.foundationminecraft.utils.shopdata.ShopCategory;
import pl.lambda.foundationminecraft.utils.shopdata.ShopItem;

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

        //todo - Shop module setup
        else if(inventoryName.equals("FMC: In-game shop"))
        {
            Shop shop = FoundationMinecraft.getInstance().getShop();
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();

            for(ShopCategory category : shop.getLoadedCategories())
            {
                if(category.getCategoryName().equalsIgnoreCase(itemName))
                {
                    String command = category.getCategoryName().replace(' ', '-');
                    Bukkit.dispatchCommand(e.getWhoClicked(), "shop " + command);
                    return;
                }
            }

            e.getWhoClicked().sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Selected category not exists!");
        }

        else if(inventoryName.startsWith("FMC: Buy items from:"))
        {
            Shop shop = FoundationMinecraft.getInstance().getShop();
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();

            for(ShopCategory category : shop.getLoadedCategories())
            {
                String expectedCategoryName = inventoryName.replace("FMC: Buy items from: ", "");
                if(category.getCategoryName().equalsIgnoreCase(expectedCategoryName))
                {
                    for(ShopItem shopItem : category.getShopItems())
                    {
                        if(shopItem.getDisplayName().equalsIgnoreCase(itemName))
                        {
                            LambdaPlayer lambdaPlayer = FoundationMinecraft.getInstance().getLambdaPlayers().get((Player)e.getWhoClicked());
                            int itemCost = shopItem.getCost();

                            if(lambdaPlayer.getMoney() >= itemCost)
                            {
                                lambdaPlayer.setMoney(lambdaPlayer.getMoney() - itemCost);
                                lambdaPlayer.save();
                                FoundationMinecraft.getInstance().getLogger().info("Player: " + e.getWhoClicked().getName() + " is buying: " + shopItem.getItem() + ".");
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "give " + e.getWhoClicked().getName() + " " + shopItem + " " + shopItem.getAmount());
                                e.getWhoClicked().sendMessage(FoundationMinecraft.getPrefix() + ChatColor.GREEN + "You have bought " + shopItem.getDisplayName() + " succesfully!");
                            }
                            else
                            {
                                e.getWhoClicked().sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "You don't have enough money to buy " + shopItem.getDisplayName() + "!");
                            }
                            return;
                        }
                    }
                }
            }

            e.getWhoClicked().sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Error while buying item. Contact staff for help.");
        }
    }
}

package pl.lambda.foundationminecraft.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.lambda.foundationminecraft.FoundationMinecraft;
import pl.lambda.foundationminecraft.utils.shopdata.Shop;
import pl.lambda.foundationminecraft.utils.shopdata.ShopCategory;
import pl.lambda.foundationminecraft.utils.shopdata.ShopItem;

import java.util.ArrayList;
import java.util.List;

public class MCmdShop implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!command.getName().equalsIgnoreCase("shop")) return false;

        if(!(sender instanceof Player))
        {
            sender.sendMessage("This command is only for players!");
            return false;
        }

        Shop registeredShop = FoundationMinecraft.getInstance().getShop();
        Inventory inventory;

        if(args.length == 0)
        {
            inventory = Bukkit.createInventory(null, registeredShop.getChestSlots(), "FMC: In-game shop");

            for(ShopCategory category : registeredShop.getLoadedCategories())
            {
                ItemStack itemStack = new ItemStack(Material.BOOK);
                ItemMeta itemMeta = itemStack.getItemMeta();

                itemMeta.setDisplayName(category.getCategoryName());
                List<String> lore = new ArrayList<>();
                String requiredClearance = "none";
                if (category.getRequiredClearance() != -1) { requiredClearance = "level-" + category.getRequiredClearance(); }
                lore.add("Required clearance: " + requiredClearance);

                itemStack.setItemMeta(itemMeta);
                inventory.addItem(itemStack);
            }

            ((Player) sender).openInventory(inventory);
        }
        else
        {
            String categoryName = args[0].replace('-', ' ');
            ShopCategory selectedCategory = null;
            for(ShopCategory category : registeredShop.getLoadedCategories())
            {
                if(category.getCategoryName().equalsIgnoreCase(categoryName))
                {
                    selectedCategory = category;
                }
            }

            if(selectedCategory == null)
            {
                sender.sendMessage(FoundationMinecraft.getPrefix() + ChatColor.RED + "Selected shop category not exist!");
                return false;
            }

            inventory = Bukkit.createInventory(null, selectedCategory.getChestSlots(), "FMC: Buy items from: " + selectedCategory.getCategoryName());
            for(ShopItem shopItem : selectedCategory.getShopItems())
            {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> lore = new ArrayList<>();

                itemMeta.setDisplayName(shopItem.getDisplayName());
                lore.add("Category: " + selectedCategory.getCategoryName());
                lore.add("Cost: " + shopItem.getCost());
                lore.add("Amount: " + shopItem.getAmount());

                itemStack.setItemMeta(itemMeta);
                inventory.addItem(itemStack);
            }

            ((Player) sender).openInventory(inventory);
        }

        return false;
    }
}

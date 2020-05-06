package pl.lambda.foundationminecraft.utils.shopdata;

import org.bukkit.configuration.ConfigurationSection;
import pl.lambda.foundationminecraft.FoundationMinecraft;

import java.util.ArrayList;
import java.util.List;

public class Shop
{
    private List<ShopCategory> loadedCategories;
    private int chestSlots;

    public Shop()
    {
        ShopDataStorage shopDataStorage = FoundationMinecraft.getInstance().getShopDataStorage();

        ConfigurationSection section = shopDataStorage.getData().getConfigurationSection("shopCategories");
        if(section == null)
        {
            FoundationMinecraft.getInstance().getLogger().warning("There is no registered category.");
            this.loadedCategories = new ArrayList<>();
            this.chestSlots = 9;
            return;
        }

        for(String categoryName : section.getKeys(false))
        {
            int requiredClearance = shopDataStorage.getData().getInt("shopCategories." + categoryName + ".requiredClearance");
            int inventorySlot = shopDataStorage.getData().getInt("shopCategories." + categoryName + ".inventorySlot");
            List<ShopItem> categoryItems = new ArrayList<>();

            ConfigurationSection items = shopDataStorage.getData().getConfigurationSection("shopCategories." + categoryName + ".items");
            if(items == null)
            {
                FoundationMinecraft.getInstance().getLogger().warning("Category " + categoryName + " not contains any items!");
                continue;
            }

            for(String itemDisplayName : items.getKeys(false))
            {
                String itemName = shopDataStorage.getData().getString("shopCategories." + categoryName + ".items." + itemDisplayName + ".item");
                int cost = shopDataStorage.getData().getInt("shopCategories." + categoryName + ".items." + itemDisplayName + ".cost");
                int amount = shopDataStorage.getData().getInt("shopCategories." + categoryName + ".items." + itemDisplayName + ".amount");
                ShopItem item = new ShopItem(itemDisplayName, itemName, cost, amount);
                categoryItems.add(item);
            }

            ShopCategory category = new ShopCategory(categoryName, categoryItems, requiredClearance, inventorySlot);
            loadedCategories.add(category);
        }

        if(loadedCategories.size() <= 9) this.chestSlots = 9;
        else if(this.loadedCategories.size() <= 18) this.chestSlots = 18;
        else if(this.loadedCategories.size() <= 27) this.chestSlots = 27;
        else throw new IllegalArgumentException("Shop can contain only 27 categories!");
    }

    public List<ShopCategory> getLoadedCategories() {
        return loadedCategories;
    }

    public int getChestSlots() {
        return chestSlots;
    }
}

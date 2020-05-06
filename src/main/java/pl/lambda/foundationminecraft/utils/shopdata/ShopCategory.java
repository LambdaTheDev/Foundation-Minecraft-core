package pl.lambda.foundationminecraft.utils.shopdata;

import java.util.List;

public class ShopCategory
{
    private String categoryName;
    private List<ShopItem> shopItems;
    private int requiredClearance;
    private int inventorySlot;
    private int chestSlots;

    public ShopCategory(String categoryName, List<ShopItem> shopItems, int requiredClearance, int inventorySlot) {
        this.categoryName = categoryName;
        this.shopItems = shopItems;
        this.requiredClearance = requiredClearance;
        this.inventorySlot = inventorySlot;

        if(shopItems.size() <= 9) chestSlots = 9;
        else if(shopItems.size() <= 18) chestSlots = 18;
        else if(shopItems.size() <= 27) chestSlots = 27;
        else throw new IllegalArgumentException("One category cannot contain more than 27 items!");
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public int getRequiredClearance() {
        return requiredClearance;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public int getChestSlots() {
        return chestSlots;
    }
}

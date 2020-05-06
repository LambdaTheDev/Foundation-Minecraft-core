package pl.lambda.foundationminecraft.utils.shopdata;

public class ShopItem
{
    private String displayName;
    private String item;
    private int cost;
    private int amount;

    public ShopItem(String displayName, String item, int cost, int amount) {
        this.displayName = displayName;
        this.item = item;
        this.cost = cost;
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAmount() {
        return amount;
    }
}

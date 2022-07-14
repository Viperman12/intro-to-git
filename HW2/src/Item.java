public class Item {
    private double unitPrice;
    private String itemName;
    private String itemCode;

    /***
     * Creates an item object
     * @param name String for itemName
     * @param price Double for unitPrice
     * @param code String for itemCode
     */
    Item(String name, double price, String code)
    {
        unitPrice = price;
        itemName = name;
        itemCode = code;
    }

    /***
     * Gets the price of the object
     * @return price
     */
    double getPrice()
    {
        return unitPrice;
    }

    /***
     * Gets the name of the object
     * @return Name
     */
    String getName()
    {
        return itemName;
    }

    /***
     * Gets the item code of the object
     * @return Item Code
     */
    String getItemCode()
    {
        return itemCode;
    }
}

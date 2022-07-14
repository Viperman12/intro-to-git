import java.util.Arrays;
public class Sale {
    // Member data
    private Item[] it;
    private int size;

    /***
     * Creates a Sale object using initialSize
     * @param initialSize Integer used to create the length of the array
     */
    Sale(int initialSize)
    {
        it = new Item[initialSize];
        size = 0;
    }

    /***
     * Adds item quant amount of times to the array
     * @param i
     * @param quant
     */
    void addItem(Item i, int quant)
    {
        for (int x = 0; x < quant; x++)
        {
            if (size == it.length)
                increaseCapacity();
            it[size] = i;
            size++;
        }
    }

    /***
     * Gets how many elements are in the Array
     * @return integer of the amount of elements in the array
     */
    int getSize()
    {
        return size;
    }

    /***
     * Gets the sum of all the items' price that are in the Array
     * @return integer of the amount of elements in the array
     */
    double getTotal()
    {
        double sum = 0;

        for(int i = 0; i < size; i++)
            sum += it[i].getPrice();

        return sum;
    }

    /***
     * Prints the items that are to be bought in alphabetical order
     * @param a ArrayList used to help
     */
    void Summary(ItemArrayList a)
    {
        Item[] items = new Item[a.getSize()];

        for(int i = 0; i < a.getSize(); i++)
            items[i] = a.get(i);

        int[] itemNum = new int[items.length];

        for(int k = 0; k < size; k++)
        {
            String code = it[k].getItemCode();
            for(int j = 0; j < items.length; j++)
                if(code.equals(items[j].getItemCode()))
                {
                    itemNum[j]++;
                    break;
                }
        }

        Sort(items, itemNum);

        for(int i = 0; i < itemNum.length; i++)
        {
            if (!(itemNum[i] == 0))
                System.out.printf("%5d %-13s $ %6.2f%n", itemNum[i], items[i].getName(), (itemNum[i] * items[i].getPrice()));
        }
    }

    /***
     * Doubles the length of the array
     */
    void increaseCapacity()
    {
        Item[] temp = Arrays.copyOf(it, it.length * 2);
        this.it = temp;
    }

    /***
     * Sorts a and b in alphabetical order
     * @param a Array of items
     * @param b Array of integers
     */
    void Sort(Item[] a, int[] b)
    {
        for(int i = 0; i < a.length; i++)
        {
            char lowestChar = a[i].getName().toLowerCase().charAt(0);
            int index = i;
            for(int j = i; j < a.length; j++)
            {
                char jChar = a[j].getName().toLowerCase().charAt(0);
                if (jChar < lowestChar)
                {
                    lowestChar = jChar;
                    index = j;
                }
            }
            if (index != i)
            {
                Item tempItem = a[i];
                a[i] = a[index];
                a[index] = tempItem;

                int tempInt = b[i];
                b[i] = b[index];
                b[index] = tempInt;
            }
        }
    }

    /***
     * Gets Item at index
     * @param index integer used to find the Item in the array
     * @return Item
     */
    Item getItem(int index)
    {
        return it[index];
    }
}

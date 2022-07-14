import java.util.Arrays;
import java.util.NoSuchElementException;
public class ItemArrayList {

    private Item[] elements;
    private int size;
    private static final int DEFAULT_CAPACITY = 16;

    /***
     * Creates an ArrayList of Items the size of initial Capacity
     * @param initialCapacity Integer for the length of the array
     */
    ItemArrayList(int initialCapacity)
    {
        elements = new Item[initialCapacity];
        size = 0;
    }

    /***
     * No Args Constructor
     */
    ItemArrayList()
    {
        this(DEFAULT_CAPACITY);
    }

    /***
     * Throws error if index is less than 0 or greater than upperbound
     * @param index the integer that is being determined
     * @param upperBound the integer that should be greater than index
     */
    private void checkIndex(int index, int upperBound) {
        if(index < 0 || index > upperBound) {
            throw new IndexOutOfBoundsException("Invalid item index: " + index);
        }
    }

    /***
     * Doubles the Array length
     */
    private void increaseCapacity() {
        Item[] newElements = Arrays.copyOf(elements, elements.length * 2);
        this.elements = newElements;
    }

    /***
     * Adds element to the end of the ArrayList
     * @param element Item object that is added to the end
     */
    void addLast(Item element) {
        add(size, element);
    }

    /***
     * Adds element to index in the Array
     * @param index
     * @param element
     */
    void add(int index, Item element) {
        checkIndex(index, size);

        if(size == elements.length) {
            increaseCapacity();
        }

        for(int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }

        elements[index] = element;

        size++;
    }

    /***
     * Removes an element at index
     * @param index removes an element using index
     * @return Item object that was removed
     */
    Item remove(int index) {
        if(isEmpty()) {
            throw new NoSuchElementException("The list is empty.");
        }

        checkIndex(index, size - 1);

        Item value = elements[index];

        for(int i = index + 1; i < size; ++i) {
            elements[i - 1] = elements[i];
        }

        size--;

        return value;
    }

    /***
     * Gets an Item object at index
     * @param index Integer that is used to get an item object
     * @return Item object
     */
    Item get(int index) {
        checkIndex(index, size - 1);
        return elements[index];
    }

    /***
     * Gets an Item object by code
     * @param code String that is used to find the Item with the matching code
     * @return Item object
     */
    Item get(String code) {
        for(int i = 0; i < getSize(); i++)  {
            if (code.equals(elements[i].getItemCode()))  {
                return elements[i];
            }
        }

        return null;
    }

    /***
     * Gets the index of Item Object or returns -1
     * @param element Object used to find the index
     * @return index of the Item
     */
    int indexOf(Item element) {
        for(int i = 0; i < size; ++i) {
            if(elements[i] == element) {
                return i;
            }
        }

        return -1;
    }

    /***
     * Determines if the String code exists in the ArrayList
     * @param code String that could be in the ArrayList
     * @return boolean is the String is a code in the Array
     */
    boolean codeExist(String code) {
        try
        {
            if (get(code) == null)
                throw new ArrayIndexOutOfBoundsException("!!! Invalid Product code\n");
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;

//        for(int i = 0; i < getSize(); i++)  {
//            if (code.equals(elements[i].getItemCode()))  {
//                return true;
//            }
//        }
//
//        return false;
    }

    /***
     * Gets how many elements are in the Array
     * @return integer of the amount of elements in the array
     */
    int getSize() {
        return size;
    }

    /***
     * Determines if the ArrayList has no elements
     * @return boolean if there are no elements in the array
     */
    boolean isEmpty() {
        return getSize() == 0;
    }
}

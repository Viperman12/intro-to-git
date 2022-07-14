// Homework 2: Sales Register Program (Improved)
// Course: CIS357
// Due Date: 7/13/2022
// Name Andy Do
// GitHub: https://github.com/Viperman12/cis357-hw2-Do
// Instructor: Il-Hyung Cho
// Program description: An improved version of HW1 that simulated a cash register
/*
Program features:
Change the item code type to String: Y
Provide the input in CSV format. Ask the user to enter the input file name: Y
Implement exception handling for
    File input: Y
    Checking wrong input data type: Y
    Checking invalid data value: Y
    Tendered amount less than the total amount: Y
Use ArrayList for the items data: Y
Adding item data: Y
Deleting item data: Y
Modifying item data: Y
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HW2_Do {
    public static void main(String[] arg) throws Exception
    {
        // Member data
        double totalSale = 0;
        Scanner s = new Scanner(System.in);
        String filePath;

        // File Input
        System.out.print("Welcome to POST system \n");
        do {
            System.out.print("\nInput File: ");
            filePath = s.nextLine();
        } while(!fileExist(filePath));

        File file = new File(filePath);
        ItemArrayList ial = updateArrayList(file);


        System.out.print("\nBeginning a new sale (Y/N)\t");
        String ans = s.nextLine();
        ans = ans.toLowerCase();

        // Begins the sale if Y, gives total sale if N
        while (ans.equals("y")) {
            Sale sale = new Sale(5);
            sale.Summary(ial);
            System.out.println("----------------------------");
            String input = "";
            // Adds items to the sale object
            while (!input.equals("-1")) {
                String code = "";
                System.out.print("Enter product code: ");
                input = s.nextLine();
                if (input.equals("0000")) {
                    printAllItem(ial);
                    continue;
                } else if (input.equals("-1")) {
                    break;
                } else if (isCode(input)) {
                    code = input;
                }
                else {
                    continue;
                }

                if (ial.codeExist(code)) {
                    Item temp = ial.get(0);
                    for(int i = 0; i < ial.getSize(); i++)
                        if (code.equals(ial.get(i).getItemCode()))
                        {
                            temp = ial.get(i);
                            break;
                        }

                    System.out.print("\t\t item name: " + temp.getName() + "\n");
                    boolean validQuantity = false;
                    do
                    {
                        System.out.print("Enter quantity:\t\t");
                        input = s.nextLine();
                        if(isNum(input))
                            validQuantity = true;
                    } while (!validQuantity);

                    sale.addItem(temp, Integer.parseInt(input));
                    System.out.printf("\t\titem total: $ %6.2f%n", (temp.getPrice() * Integer.parseInt(input)));
                }
            }

            // Gives total before and after tax and prompts user for the tender amount and gives change
            double total = sale.getTotal();
            double price = 0;
            double tender;

            for(int i = 0; i < sale.getSize(); i++)
            {
                if (sale.getItem(i).getItemCode().charAt(0) == 'A')
                    price += sale.getItem(i).getPrice() * 1.06;
                else
                    price += sale.getItem(i).getPrice();
            }

            System.out.println("----------------------------\nItem Listed:");
            sale.Summary(ial);
            System.out.printf("Subtotal\t\t\t$ %6.2f%n", total);
            System.out.printf("%s %6.2f%n", "Total with Tax (6%) $", price);
            do
            {
                if (price < 10)
                    System.out.printf("Tendered amount \t$   ");
                else
                    System.out.printf("Tendered amount \t$  ");
                tender = Double.parseDouble(s.nextLine());
            } while(!amountIsEnough(tender, price));
            double change = tender - price;
            totalSale += total;
            System.out.printf("Change\t\t\t\t$ %6.2f%n", change);
            System.out.println("----------------------------");
            System.out.print("\nBeginning a new sale (Y/N)\t");
            ans = s.nextLine();
            ans = ans.toLowerCase();
        }
        System.out.printf("The total sale for the day is $\t%.2f\n\n", totalSale);

        // Begins the modification of items
        char c = 'A';
        while(!(c == 'Q'))
        {
            System.out.print("Do you want to update the items data? (A/D/M/Q): ");
            c = s.nextLine().charAt(0);
            ial = updateArrayList(file);
            // Switch Case for the four options. Breaks if input is Q
            switch ((int) c) {
                case 65:
                    addItem(ial, file.getName());
                    break;
                case 68:
                    deleteItem(ial, file.getName());
                    break;
                case 77:
                    updateItem(ial, file.getName());
                    break;
                default:
                    printAllItem(ial);
            }
        }
        System.out.println("Thanks for using POST system. Goodbye");
    }

    /***
     * Passes a String and throws an error if a file with specified string does not exist
     * @param path the absolute path
     * @return boolean if file exists using path
     */
    static boolean fileExist(String path)
    {
        try
        {
            File file = new File(path);
            if (!file.exists())
                throw new FileNotFoundException("File is not Found.");
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /***
     * Scans all lines of a file and adds them to an Arraylist of Items
     * @param file Object that is used to create a scanner object
     * @return An ArrayList of Item objects
     * @throws FileNotFoundException
     */
    static ItemArrayList updateArrayList(File file) throws FileNotFoundException
    {
        ItemArrayList ial = new ItemArrayList();
        Scanner output = new Scanner(file);
        // Starts scanning file to ItemArrayList
        while (output.hasNext()) {
            String[] line = output.nextLine().split(", ", 3);
            String code = line[0];
            String name = line[1];
            double price = Double.parseDouble(line[2]);
            Item i = new Item(name, price, code);
            ial.addLast(i);
        }
        output.close();

        return ial;
    }

    /***
     * Adds an item object to an ArrayList and uses a Print Writer to print them onto the file
     * @param ial Adds new Item to this ItemArrayList
     * @param file Path name of the file to create a Print Writer
     * @throws Exception
     */
    static void addItem(ItemArrayList ial, String file) throws Exception
    {
        Scanner s = new Scanner(System.in);

        // Gets code
        String code;
        do {
            System.out.print("item code: ");
            code = s.nextLine();
        } while(!isCode(code));

        // Gets name
        System.out.print("item name: ");
        String name = s.nextLine();

        // Gets price
        String temp;
        do {
            System.out.print("item price: ");
            temp = s.nextLine();
        }  while(!isNum(temp));
        double price = Double.parseDouble(temp);

        ial.addLast(new Item(name, price, code));
        PrintWriter output = new PrintWriter(file);

        for(int i = 0; i < ial.getSize(); i++)
            output.println(ial.get(i).getItemCode() + ", " + ial.get(i).getName() + ", " + ial.get(i).getPrice());
        output.close();
        System.out.println("Item add successful");
    }

    /***
     * Deletes an item object to an ArrayList and uses a Print Writer to print them onto the file
     * @param ial Adds new Item to this ItemArrayList
     * @param file Path name of the file to create a Print Writer
     * @throws Exception
     */
    static void deleteItem(ItemArrayList ial, String file) throws Exception
    {
        Scanner s = new Scanner(System.in);

        // Gets code
        String code;
        do {
            System.out.print("item code: ");
            code = s.nextLine();
        } while(!isCode(code));

        ial.remove(ial.indexOf(ial.get(code)));

        PrintWriter output = new PrintWriter(file);
        for(int i = 0; i < ial.getSize(); i++)
            output.println(ial.get(i).getItemCode() + ", " + ial.get(i).getName() + ", " + ial.get(i).getPrice());
        output.close();
        System.out.println("Item delete successful");
    }

    /***
     * Updates an item object to an ArrayList and uses a Print Writer to print them onto the file
     * @param ial Adds new Item to this ItemArrayList
     * @param file Path name of the file to create a Print Writer
     * @throws Exception
     */
    static void updateItem(ItemArrayList ial, String file) throws Exception
    {
        Scanner s = new Scanner(System.in);

        // Gets Code
        String code;
        do {
            System.out.print("item code: ");
            code = s.nextLine();
        } while(!(isCode(code) && ial.codeExist(code)));

        // Gets Name
        System.out.print("item name: ");
        String name = s.nextLine();

        String temp;
        do {
            System.out.print("item price: ");
            temp = s.nextLine();
        } while(!isNum(temp));
        Double price = Double.parseDouble(temp);

        Item item = new Item(name, price, code);

        int index = ial.indexOf(ial.get(code));
        ial.remove(index);
        ial.add(index, item);

        PrintWriter output = new PrintWriter(file);

        for(int i = 0; i < ial.getSize(); i++)
            output.println(ial.get(i).getItemCode() + ", " + ial.get(i).getName() + ", " + ial.get(i).getPrice());

        output.close();
        System.out.println("Item modify successful");
    }

    /***
     * Prints all elements of an ArrayList of items
     * @param ial prints all elements in the ArrayList
     */
    static void printAllItem(ItemArrayList ial)  {
        System.out.printf("%n%-15s %-15s %-15s%n", "item code", "item name", "unit price");
        for(int i = 0; i < ial.getSize(); i++)
        {
            Item item = ial.get(i);
            System.out.printf("%-15s %-15s %-15.2f%n", item.getItemCode(), item.getName(), item.getPrice());
        }
        System.out.println();
    }

    /***
     * Determines whether a is a number
     * @param a String that could be a number
     * @return boolean whether String a is a number or not
     */
    static boolean isNum(String a)
    {
        try
        {
            if (a == null)
                throw new IllegalArgumentException("!!! Invalid data type\n");



            double d = Double.parseDouble(a);
//            for (int i = 0; i < a.length(); i++)
//            {
//                int charInt = a.charAt(i);
//                if (!(charInt >= 48 && charInt <= 57))
//                    throw new NumberFormatException("!!! Invalid data type\n");
//            }
        }
        catch(NumberFormatException e)
        {
            System.out.println("!!! Invalid data type\n");
            return false;
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    /***
     * Determines whether a is a code
     * @param a String that could be a code
     * @return boolean whether String a is a code or not
     */
    static boolean isCode(String a)
    {
        try
        {
            int ascii = a.charAt(0);
            if (a == null || !(ascii >= 65 && ascii <= 90))
                throw new NumberFormatException("!!! Invalid data type\n");

            if (a.length() != 4)
                throw new NumberFormatException("!!! Invalid Product code\n");

            return true;
        }
        catch(NumberFormatException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /***
     * Determines whether tender is enough to pay price
     * @param tender Amount that should be greater than price
     * @param price Amount that should be less than tender
     * @return boolean whether tender is greater than price
     */
    static boolean amountIsEnough(double tender, double price)
    {

        try
        {
            if ((tender - price) < 0)
                throw new IllegalArgumentException("!!! Not enough. Enter again.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}

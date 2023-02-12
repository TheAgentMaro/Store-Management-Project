package com.supstore.core.management;

import com.supstore.core.database.DatabaseManager;

import java.sql.*;
import java.util.Scanner;

public class AdminInventoryManagement {
    public static void InventoryManagement() throws SQLException {

        // Create a scanner for reading input from the console
        Scanner scanner = new Scanner(System.in);

        // Display the main menu
        displayMainMenu();

        // Loop until the user chooses to exit
        while (true) {
            // Read the user's choice
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Add a new item to the inventory
                    addItem();
                    break;
                case 2:
                    // Update the quantity of an existing item
                    updateQuantity();
                    break;
                case 3:
                    // Display all items in the inventory
                    displayInventory();
                    break;
                case 4:
                    // Display all items in the inventory
                    deleteItem();
                    break;
                case 5:
                    // Exit the program
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            // Display the main menu again
            displayMainMenu();
        }

    }

    private static void displayMainMenu() {
        System.out.println("|--------------------------------|");
        System.out.println("|--Inventory Management System|--|");
        System.out.println("|--------------------------------|");
        System.out.println("|1. Add a new item               |");
        System.out.println("|2. Update quantity of an item   |");
        System.out.println("|3. Display inventory            |");
        System.out.println("|4. Delete an item               |");
        System.out.println("|5. Exit to store management     |");
        System.out.println("|--------------------------------|");
        System.out.print("Enter your choice: ");
    }
    private static void addItem() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------------------------------");
        System.out.println("To add new item fill the following form : ");
        System.out.println("");
        System.out.print("Enter the item ID: ");
        String id = scanner.next();

        System.out.print("Enter the item name: ");
        String name = scanner.next();

        System.out.print("Enter the item price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter the initial quantity: ");
        int quantity = scanner.nextInt();

        System.out.print("Enter the ID of the inventory : ");
        int inv_id = scanner.nextInt();

        // Insert the new item into the database
        DatabaseManager db = new DatabaseManager();
        String sql = "INSERT INTO items (id, name, price, quantity, inv_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(id));
        statement.setString(2, name);
        statement.setDouble(3, price);
        statement.setInt(4, quantity);
        statement.setInt(5, inv_id);
        statement.executeUpdate();
        System.out.println("|------------------------- |");
        System.out.println("|Item added successfully : |");
        System.out.println("|------------------------- |");
        System.out.println("|ID : " + id );
        System.out.println("|Name : " + name);
        System.out.println("|Price : " + price +" $");
        System.out.println("|Quantity : "+ quantity);
        System.out.println("|--------------------------|");
        System.out.println("");
    }


    private static void updateQuantity() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("|-----    Item Management System     -----|");
        System.out.println("|1. Increase item quantity                |");
        System.out.println("|2. Decrease item quantity                |");
        System.out.println("|3. Exit                                  |");
        System.out.println("|-----------------------------------------|");
        System.out.println("");
        System.out.print("Enter your choice: ");
        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    AddQuantity();
                    break;
                case 2:
                    SubtractQuantity();
                    break;
                case 3:
                    // Exit the program
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.print("Enter your choice: ");
                    break;

            }
        }
    }

    private static int getItemQuantityFromDB(int item_id) throws SQLException {
        int itemQuantity = 0;
        DatabaseManager db = new DatabaseManager();
        db.st = db.conn.createStatement();
        // Execute the query to retrieve the item quantity
        db.rs  = db.st.executeQuery("SELECT quantity FROM items WHERE id = " + item_id);
        if (db.rs.next()) {
            itemQuantity = db.rs.getInt("quantity");
        }
        return itemQuantity;
    }
    private static void AddQuantity() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the item to update: ");
        int id = scanner.nextInt();

        System.out.print("Enter the quantity to add : ");
        int quantity = scanner.nextInt();

        // Update the quantity of the item in the database
        DatabaseManager db = new DatabaseManager();
        String sql = "UPDATE items SET quantity = ? WHERE id = ?";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setInt(1, quantity);
        statement.setInt(2, id);
        statement.executeUpdate();
        while (quantity < 0) {
            System.out.println("Cannot decrease item quantity below 0.");
            System.out.print("Enter the new quantity again : ");
            quantity = scanner.nextInt();
        }
        System.out.println("--------------------------------");
        System.out.println("Quantity updated successfully !.");
        System.out.println("--------------------------------");
        System.out.println("");
    }

    private static void SubtractQuantity() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the item to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter the quantity to subtract :  ");
        int quantity = scanner.nextInt();
        // Update the quantity of the item in the database
        DatabaseManager db = new DatabaseManager();
        int currentQuantity = getItemQuantityFromDB(id);
        if (currentQuantity - quantity < 0) {
            System.out.println("Cannot decrease item quantity below 0.");
            updateQuantity();
        }else{String sql = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
            PreparedStatement statement = db.conn.prepareStatement(sql);
            statement.setInt(1, quantity);
            statement.setInt(2, id);
            statement.executeUpdate();
            System.out.println("--------------------------------");
            System.out.println("Quantity updated successfully !.");
            System.out.println("--------------------------------");
            System.out.println("");
            updateQuantity();}
    }


    private static void deleteItem() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the item to delete: ");
        int id = scanner.nextInt();


        // Delete an item in the database
        DatabaseManager db = new DatabaseManager();
        String sql = "DELETE FROM items  WHERE id = ?";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        System.out.println("--------------------------------");
        System.out.println("  Item deleted successfully !.  ");
        System.out.println("--------------------------------");
        System.out.println("");
        displayMainMenu();
    }
    private static void displayInventory() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the inventory to display: ");
        int inventoryId = scanner.nextInt();

        // Select all items from the database
        DatabaseManager db = new DatabaseManager();
        Statement statement = db.conn.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM items WHERE inv_id = " + inventoryId);

        
        // Print the items to the console
        System.out.println("|------ Items in Inventory ID : " + inventoryId + " -------|");
        System.out.println("");
        System.out.println("------------------------------------------------");
        System.out.println("ID\tName\tPrice\tQuantity");
        System.out.println("------------------------------------------------");
        System.out.println("");
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            double price = result.getDouble("price");
            int quantity = result.getInt("quantity");
            System.out.println("------------------------------------------------");
            System.out.println(id + "\t" + name + "\t" + price + "\t" + quantity);
            System.out.println("------------------------------------------------");
            System.out.println("");
            displayMainMenu();
        }
    }
}
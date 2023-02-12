package com.supstore.core.management;

import com.supstore.core.database.DatabaseManager;
import com.supstore.core.facade.RegisterLoginFacade;
import com.supstore.core.menu.UserHomeMenu;

import java.sql.*;
import java.util.Scanner;

public class UserInventoryManagement {
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
                    // Update the quantity of an existing item
                    updateQuantity();
                    break;
                case 2:
                    // Display all items in the inventory
                    displayInventory();
                    break;
                case 3:
                    // Return to main menu
                    UserHomeMenu UH = new UserHomeMenu();
                    UH.userInterface();
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
        System.out.println("|1. Update quantity of an item   |");
        System.out.println("|2. Display inventory            |");
        System.out.println("|3. Exit to store management     |");
        System.out.println("|--------------------------------|");
        System.out.print("Enter your choice: ");
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

    private static void AddQuantity() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager db = new DatabaseManager();
        System.out.println("Updating item in inventory acceding...");
        System.out.println("For verification enter again your Store ID :");
        int store_id = scanner.nextInt();
        int employeeid = Integer.parseInt(RegisterLoginFacade.getUser_id());
        String checkEmployerExistsQuery = "SELECT * FROM store_employees WHERE store_id = ? AND employee_id = ?";
        try (PreparedStatement statementcheck = db.conn.prepareStatement(checkEmployerExistsQuery) ) {
            statementcheck.setInt(1, store_id);
            statementcheck.setInt(2, employeeid);
            try (ResultSet resultSet = statementcheck.executeQuery()) {
                if (resultSet.next()) {
                    int idstore = store_id;
                    System.out.println("Enter the ID of the inventory : ");
                    int inventoryId = scanner.nextInt();
                    System.out.println("Enter the ID of the item to update: ");
                    int item_id = scanner.nextInt();
                    String checkInventoryExistsQuery = "SELECT * FROM inventory WHERE id = ? AND store_id = ? AND item_id = ? ";
                    // verify if exist
                    try (PreparedStatement statementcheck2 = db.conn.prepareStatement(checkInventoryExistsQuery)) {
                        statementcheck2.setInt(1, inventoryId);
                        statementcheck2.setInt(2, idstore);
                        statementcheck2.setInt(3, item_id);
                        try (ResultSet resultset = statementcheck2.executeQuery()) {
                            if (resultset.next()) {
                                System.out.print("Enter the quantity to add : ");
                                int quantity = scanner.nextInt();
                                // Update the quantity of the item in the database
                                String sql = "UPDATE items SET quantity = quantity + ? WHERE id = ?";
                                PreparedStatement statement = db.conn.prepareStatement(sql);
                                statement.setInt(1, quantity);
                                statement.setInt(2, item_id);
                                statement.executeUpdate();
                                System.out.println("--------------------------------");
                                System.out.println("Quantity updated successfully !.");
                                System.out.println("--------------------------------");
                                System.out.println("");
                            }else {
                                System.out.print("Sorry the item doesn't exist in the inventory !");
                                System.out.println("");}
                        }
                    }
                }else {
                    System.out.print("Sorry you don't have access to this store inventory!");
                    System.out.println("");}
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

    private static void SubtractQuantity() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager db = new DatabaseManager();
        System.out.println("Updating item in inventory acceding...");
        System.out.println("For verification enter again your Store ID :");
        int store_id = scanner.nextInt();
        int employeeid = Integer.parseInt(RegisterLoginFacade.getUser_id());
        String checkEmployerExistsQuery = "SELECT * FROM store_employees WHERE store_id = ? AND employee_id = ?";
        try (PreparedStatement statementcheck = db.conn.prepareStatement(checkEmployerExistsQuery) ) {
            statementcheck.setInt(1, store_id);
            statementcheck.setInt(2, employeeid);
            try (ResultSet resultSet = statementcheck.executeQuery()) {
                if (resultSet.next()) {
                    int idstore = store_id;
                    System.out.println("Enter the ID of the inventory : ");
                    int inventoryId = scanner.nextInt();
                    System.out.println("Enter the ID of the item to update: ");
                    int item_id = scanner.nextInt();
                    String checkInventoryExistsQuery = "SELECT * FROM inventory WHERE id = ? AND store_id = ? AND item_id = ? ";
                    // verify if exist
                    try (PreparedStatement statementcheck2 = db.conn.prepareStatement(checkInventoryExistsQuery)) {
                        statementcheck2.setInt(1, inventoryId);
                        statementcheck2.setInt(2, idstore);
                        statementcheck2.setInt(3, item_id);
                        try (ResultSet resultset = statementcheck2.executeQuery()) {
                            if (resultset.next()) {
                                    System.out.print("Enter the quantity to subtract: ");
                                    int quantity = scanner.nextInt();
                                    // Update the quantity of the item in the database
                                    int currentQuantity = getItemQuantityFromDB(item_id);
                                    if (currentQuantity - quantity < 0) {
                                        System.out.println("Error: Cannot decrease quantity below 0");
                                        updateQuantity();
                                    }else{String sql = "UPDATE items SET quantity = quantity - ? WHERE id = ?";
                                        PreparedStatement statement = db.conn.prepareStatement(sql);
                                        statement.setInt(1, quantity);
                                        statement.setInt(2, item_id);
                                        statement.executeUpdate();
                                    System.out.println("--------------------------------");
                                    System.out.println("Quantity updated successfully !.");
                                    System.out.println("--------------------------------");
                                    System.out.println("");}
                            }else {
                                System.out.print("Sorry the item doesn't exist in the inventory !");
                                System.out.println("");}
                        }
                    }
                }else {
                    System.out.print("Sorry you don't have access to this store inventory!");
                    System.out.println("");}
            }
        }
    }


    private static void displayInventory() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        DatabaseManager db = new DatabaseManager();
        System.out.println("Acceding to your store inventory...");
        System.out.println("For verification enter again your Store ID :");
        int store_id = scanner.nextInt();
        int employeeid = Integer.parseInt(RegisterLoginFacade.getUser_id());
        String checkEmployerExistsQuery = "SELECT * FROM store_employees WHERE store_id = ? AND employee_id = ?";
        try (PreparedStatement statementcheck = db.conn.prepareStatement(checkEmployerExistsQuery) ) {
            statementcheck.setInt(1, store_id);
            statementcheck.setInt(2, employeeid);
            try (ResultSet resultSet = statementcheck.executeQuery()) {
                if (resultSet.next()) {
                    int idstore = store_id;
                    System.out.println("Enter the ID of the inventory : ");
                    int inventoryId = scanner.nextInt();
                    String checkInventoryExistsQuery = "SELECT * FROM stores WHERE id = ? AND inv_id = ? ";
                    // verify if exist
                    try (PreparedStatement statementcheck2 = db.conn.prepareStatement(checkInventoryExistsQuery)) {
                        statementcheck2.setInt(1, idstore);
                        statementcheck2.setInt(2, inventoryId);
                        try (ResultSet resultset = statementcheck2.executeQuery()) {
                            if (resultset.next()) {
                                // Select all items from the database
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
                            } else {
                                System.out.print("Sorry you do not have access to this inventory. Please try again !");
                                System.out.println("");}
                                displayMainMenu();
                        }
                    }
                } else {
                    System.out.print("Sorry you do not have access to this store. Please try again !");
                    System.out.println("");
                    displayMainMenu();
                }
            }
        }
    }
}
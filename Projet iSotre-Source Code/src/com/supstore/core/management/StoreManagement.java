package com.supstore.core.management;

import com.supstore.core.database.DatabaseManager;
import com.supstore.core.facade.RegisterLoginFacade;

import java.sql.*;
import java.util.Scanner;
public class StoreManagement {

    public static void StoreManagement() {
        try {
            // Connect to the database
            DatabaseManager db = new DatabaseManager();

            // Create a scanner for reading input from the console
            Scanner scanner = new Scanner(System.in);

            // Check if the user is logged in as an admin
            boolean isAdmin = loginStore();

            // Display the main menu
            displayMainMenu(isAdmin);

            // Loop until the user chooses to exit
            while (true) {
                // Read the user's choice
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Create a new store
                        if (isAdmin) {
                            createStore();
                        } else {
                            System.out.println("You must be logged in as an admin to create a new store.");
                        }
                        break;
                    case 2:
                        // Delete a store
                        if (isAdmin) {
                            deleteStore();
                        } else {
                            System.out.println("You must be logged in as an admin to delete a store.");
                        }
                        break;
                    case 3:
                        // Add an employee to a store
                        if (isAdmin) {
                            addEmployeeToStore();
                        } else {
                            System.out.println("You must be logged in as an admin to add an employee to a store.");
                        }
                        break;
                    case 4:
                        // Remove an employee from a store
                        if (isAdmin) {
                            removeEmployeeFromStore();
                        } else {
                            System.out.println("You must be logged in as an admin to remove an employee from a store.");
                        }
                        break;
                    case 5:
                        if (isAdmin) {
                            // Display all stores and their employees
                            AdminInventoryManagement IMA = new AdminInventoryManagement();
                            IMA.InventoryManagement();
                        } else { System.out.println("You must be logged in as an admin to access this !"); }
                        break;
                    case 6:
                        // Display all stores and their employees
                        displayStoresAndEmployees();
                        break;
                    case 7:
                        // Exit the program
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

                // Display the main menu again
                displayMainMenu(isAdmin);
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }
    private static boolean loginStore() throws SQLException {
        while (true) {
            System.out.print("For Security Majors you need to login again to the desired store !  ");
            System.out.println("");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the store ID: ");
            String id = scanner.next();

            System.out.print("Enter your email: ");
            String email = scanner.next();

            System.out.print("Enter your password: ");
            String password = scanner.next();

            DatabaseManager db = new DatabaseManager();
            // Check if the email and password match an admin user
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            String sql2= "SELECT * FROM stores WHERE id = ?";
            PreparedStatement statement = db.conn.prepareStatement(sql);
            PreparedStatement statement2 = db.conn.prepareStatement(sql2);
            statement2.setString(1, id);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();
            ResultSet result2 = statement2.executeQuery();
            if (!result2.next()) {
                System.out.println("");
                System.out.println("Store Not found !");
                System.out.println("");
                System.out.println("ReEnter Store ID:");
                id = String.valueOf(scanner.nextInt());
                scanner.nextLine();
                statement2.setString(1, id);
                }
            if (result.next()) {
                String role = result.getString("role");
                if ("admin".equals(role)) {
                    return true;
                }
            }System.out.println("Invalid email or password. Please try again.");
        }
    }

    private static void displayMainMenu(boolean isAdmin) {
        System.out.println("|-----    Store Management System    ----|");
        if (isAdmin) {
            System.out.println("|-----------------(Admin)----------------|");
            System.out.println("|1. Create a new store                   |");
            System.out.println("|2. Delete a store                       |");
            System.out.println("|3. Add an employee to a store           |");
            System.out.println("|4. Remove an employee from a store      |");
            System.out.println("|5. Store Inventory Management Admin     |");
            System.out.println("|----------------------------------------|");
        }
        System.out.println("|--------------( Admin+User )-------------|");
        System.out.println("|6. Display all stores and their employees|");
        System.out.println("|7. Exit to main menu                     |");
        System.out.println("|-----------------------------------------|");
        System.out.println("");
        System.out.print("Enter your choice: ");
    }
    public static void displayMainMenuUser() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager db = new DatabaseManager();
        System.out.print("Enter the ID of your store: ");
        int storeid = scanner.nextInt();
        int employeeid = Integer.parseInt(RegisterLoginFacade.getUser_id());
        String checkEmployerExistsQuery = "SELECT * FROM store_employees WHERE store_id = ? AND employee_id = ?";
        // verify if exist
        try (PreparedStatement statementcheck = db.conn.prepareStatement(checkEmployerExistsQuery)) {
            statementcheck.setInt(1, storeid);
            statementcheck.setInt(2, employeeid);
            try (ResultSet resultSet = statementcheck.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("");
                    System.out.println("|-----    Store Management System     ----|");
                    System.out.println("|--------------( Admin+User )-------------|");
                    System.out.println("|6. Store Inventory Management            |");
                    System.out.println("|7. Display all store employees           |");
                    System.out.println("|8. Exit  to main menu                    |");
                    System.out.println("|-----------------------------------------|");
                    System.out.println("");
                    System.out.print("Enter your choice: ");
                        while (true) {
                            int choice = scanner.nextInt();
                            switch (choice) {
                                case 6:
                                    // User store management
                                    UserInventoryManagement IM = new UserInventoryManagement();
                                    IM.InventoryManagement();
                                    break;
                                case 7:
                                    // Display all stores and their employees
                                    displayStoresAndEmployeesUser();
                                    break;
                                case 8:
                                    // Exit the program
                                    return;
                                default:
                                    System.out.println("Invalid choice. Please try again.");
                                    System.out.print("Enter your choice: ");
                                    break;
                            }
                        }
                }else {
                    System.out.print("Sorry you do not have access to this store. Please try again !");
                    System.out.println("");
                    displayMainMenuUser();
                }
            }
        } catch (SQLException e) {
                        // handle the exception
        }
    }

    private static void createStore() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the store ID : ");
        String id = scanner.next();
        System.out.print("Enter the store name : ");
        String name = scanner.next();


        DatabaseManager db = new DatabaseManager();
        // Insert the new store into the database
        String sql = "INSERT INTO stores (id, name) VALUES (?, ?)";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setString(1, id);
        statement.setString(2, name);
        statement.executeUpdate();
        System.out.println("-------------------------------------------------");
        System.out.println( name +" Store created successfully ID : "+ id);
        System.out.println("-------------------------------------------------");
    }

    private static void deleteStore() throws SQLException {
        System.out.print("Enter the ID of the store to delete: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();

        DatabaseManager db = new DatabaseManager();
        // Delete the store from the database
        String sql = "DELETE FROM stores WHERE id = ?";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
        System.out.println("----------------------------------");
        System.out.println("  Store deleted successfully.");
        System.out.println("----------------------------------");
    }
    private static void addEmployeeToStore() throws SQLException {
        System.out.print("Enter the ID of the employee: ");
        Scanner scanner = new Scanner(System.in);
        int employeeId = scanner.nextInt();

        System.out.print("Enter the ID of the store: ");
        int storeId = scanner.nextInt();

        DatabaseManager db = new DatabaseManager();
        // Insert a new record into the store_employees table
        String sql = "INSERT INTO store_employees (store_id, employee_id) VALUES (?, ?)";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setInt(1, storeId);
        statement.setInt(2, employeeId);
        statement.executeUpdate();
        System.out.println("Employee added to store successfully.");
    }

    private static void removeEmployeeFromStore() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the ID of the employee: ");
        int employeeId = scanner.nextInt();

        System.out.print("Enter the ID of the store: ");
        int storeId = scanner.nextInt();

        DatabaseManager db = new DatabaseManager();
        // Delete the record from the store_employees table
        String sql = "DELETE FROM store_employees WHERE store_id = ? AND employee_id = ?";
        PreparedStatement statement = db.conn.prepareStatement(sql);
        statement.setInt(1, storeId);
        statement.setInt(2, employeeId);
        statement.executeUpdate();
        System.out.println("-----------------------------------------");
        System.out.println("Employee removed from store successfully.");
        System.out.println("-----------------------------------------");
    }

    private static void displayStoresAndEmployees() throws SQLException {
        // Select all stores and their employees from the database
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT stores.id, stores.name, employees.id, employees.name FROM stores LEFT JOIN store_employees ON stores.id = store_employees.store_id LEFT JOIN employees ON store_employees.employee_id = employees.id";
        Statement statement = db.conn.createStatement();
        ResultSet result = statement.executeQuery(sql);
        // Print the stores and their employees to the console
        System.out.println("Store ID\tStore Name\tEmployee ID\tEmployee Name");
        while (result.next()) {
            int storeId = result.getInt("stores.id");
            String storeName = result.getString("stores.name");
            int employeeId = result.getInt("employees.id");
            String employeeName = result.getString("employees.name");
            System.out.println(storeId + "\t\t\t\t" + storeName + "\t\t\t\t" + employeeId + "\t\t\t\t" + employeeName);
        }
        System.out.println("");
        displayMainMenuUser();
    }

    private static void displayStoresAndEmployeesUser() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        DatabaseManager db = new DatabaseManager();
        System.out.print("To view the employees list reenter your Store ID :");
        int store_id = scanner.nextInt();
        int employeeid = Integer.parseInt(RegisterLoginFacade.getUser_id());
        String checkEmployerExistsQuery = "SELECT * FROM store_employees WHERE store_id = ? AND employee_id = ?";
        // verify if exist
        try (PreparedStatement statementcheck = db.conn.prepareStatement(checkEmployerExistsQuery)) {
            statementcheck.setInt(1, store_id);
            statementcheck.setInt(2, employeeid);
            try (ResultSet resultSet = statementcheck.executeQuery()) {
                if (resultSet.next()) {
                String sql = "SELECT stores.id, stores.name, employees.id, employees.name FROM stores LEFT JOIN store_employees ON stores.id = store_employees.store_id LEFT JOIN employees ON store_employees.employee_id = employees.id WHERE store_id = ?";
                PreparedStatement statement = db.conn.prepareStatement(sql);
                statement.setInt(1, store_id);
                ResultSet result = statement.executeQuery();
                // Print the stores and their employees to the console
                System.out.println("Store ID\tStore Name\tEmployee ID\tEmployee Name");
                while (result.next()) {
                    int storeId = result.getInt("stores.id");
                    String storeName = result.getString("stores.name");
                    int employeeId = result.getInt("employees.id");
                    String employeeName = result.getString("employees.name");
                    System.out.println(storeId + "\t\t\t\t" + storeName + "\t\t\t\t" + employeeId + "\t\t\t\t" + employeeName);

                }
            } else {
                    System.out.print("Sorry you do not have access to view the list of this store. Please try again !");
                    System.out.println("");
                    displayMainMenuUser();}
            }
        }
    }
}



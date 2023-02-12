package com.supstore.core.management;


import com.supstore.core.database.DatabaseManager;

import java.sql.*;
import java.util.Scanner;

public class AdminManagement {
    public static void whitelistEmail() throws SQLException {
        System.out.print("Enter email to whitelist: ");
        Scanner scanner = new Scanner(System.in);
        String email = scanner.nextLine();

        DatabaseManager db = new DatabaseManager();

        String sql = "INSERT INTO whitelist (email) VALUES (?)";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, email);
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Email whitelisted successfully!");
        } else {
            System.out.println("Error whitelisting email");
        }
    }


    public static void createStore() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the store ID : ");
        String storeId = scanner.next();
        System.out.print("Enter the store name : ");
        String name = scanner.next();
        System.out.print("Enter linked Inventory ID : ");
        String inv_id = scanner.next();

        DatabaseManager db = new DatabaseManager();
        String sql = "INSERT INTO stores (id, name, inv_id) VALUES (?, ?, ?)";
        PreparedStatement stmt = db.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, storeId);
        stmt.setString(2, name);
        stmt.setString(3, inv_id);
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                System.out.println(name +" Store created successfully! ID : " + storeId);
            }
        } else {
            System.out.println("Error creating store");
        }
    }

    public static void deleteStore() throws SQLException {
        System.out.print("Enter store ID to delete: ");
        Scanner scanner = new Scanner(System.in);
        int storeId = scanner.nextInt();
        scanner.nextLine(); // consume newline character

        // check if store exists
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM stores WHERE id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, storeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: store not found");
            return;
        }

        // delete store
        sql = "DELETE FROM stores WHERE id = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, storeId);
        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Store deleted successfully!");
        } else {
            System.out.println("Error deleting store");
        }
    }

    public static void listStoreEmployees() throws SQLException {
        System.out.print("Enter store ID: ");
        Scanner scanner = new Scanner(System.in);
        int storeId = scanner.nextInt();
        scanner.nextLine(); // consume newline character

        // check if store exists
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM stores WHERE id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, storeId);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: store not found");
            return;
        }

        // list store employees
        sql = "SELECT first_name,last_name,email FROM users u INNER JOIN store_employees se ON u.user_id = se.employee_id WHERE se.store_id = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, storeId);
        rs = stmt.executeQuery();
        System.out.println("List of employees for store " + storeId + ": ");
        while (rs.next()) {
            System.out.println("-------------------------------");
            System.out.println("First Name : " + rs.getString("first_name"));
            System.out.println("Last Name : " + rs.getString("last_name"));
            System.out.println("Email : " + rs.getString("email"));
            System.out.println("-------------------------------");
            System.out.println(""); }
    }

    /////////////////////////////////////////////// UPDATE ////////////////////////////////
    public static void updateUser() throws SQLException {
        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user id: ");
        int user_id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new first name: ");
        String first_name = scanner.nextLine();
        System.out.print("Enter new last name: ");
        String last_name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Enter new role: ");
        String role = scanner.nextLine();


        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: user not found");
            return;
        }
        int currentUserId = rs.getInt("user_id");
        String currentUserRole = rs.getString("role");
        if (currentUserId != user_id && !currentUserRole.equals("admin")) {
            System.out.println("Error: not authorized to update user");
            return;
        }
        // check if email is whitelisted
        sql = "SELECT * FROM whitelist WHERE email = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, email);
        rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: email not whitelisted");
            return;
        }

        // update user in database
        sql = "UPDATE users SET first_name = ?, last_name = ? ,email = ?, password = ?, role = ? WHERE user_id = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, first_name);
        stmt.setString(2, last_name);
        stmt.setString(3, email);
        stmt.setString(4, password);
        stmt.setString(5, role);
        stmt.setInt(6, user_id);
        stmt.executeUpdate();
        System.out.println("---------------------------");
        System.out.println("User updated successfully");
        System.out.println("---------------------------");
    }
    /////////////////////////////////////////////// DELETE ////////////////////////////////
    public static void deleteUser() throws SQLException {
        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user id: ");
        int user_id = scanner.nextInt();
        scanner.nextLine();

        // check if user is allowed to delete the user
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: user not found");
            return;
        }
        int currentUserId = rs.getInt("user_id");
        String currentUserRole = rs.getString("role");
        if (currentUserId != user_id && !currentUserRole.equals("admin")) {
            System.out.println("Error: not authorized to delete user");
            return;
        }

        // delete user from database
        sql = "DELETE FROM users WHERE user_id = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        stmt.executeUpdate();
        System.out.println("-----------------------------");
        System.out.println("User deleted successfully");
        System.out.println("-----------------------------");
    }

}
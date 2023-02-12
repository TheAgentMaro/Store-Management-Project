package com.supstore.core.management;

import com.supstore.core.database.DatabaseManager;
import com.supstore.core.facade.RegisterLoginFacade;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserManagement {

    /////////////////////////////////////////////// CREATE ////////////////////////////////
    public static void createUser() throws SQLException {
        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new ID: ");
        String user_id = scanner.nextLine();
        System.out.print("Enter your first name: ");
        String first_name = scanner.nextLine();
        System.out.print("Enter email: ");
        String last_name = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role: ");
        String role = scanner.nextLine();

        // check if email is whitelisted
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM whitelist WHERE email = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: email not whitelisted");
            return;
        }

        // hash the password
        password = BCrypt.hashpw(password, BCrypt.gensalt());

        // insert user into users table
        sql = "INSERT INTO users (user_id, first_name, last_name, email, password, role) VALUES (?, ?, ?, ?)";
        stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, user_id);
        stmt.setString(2, first_name);
        stmt.setString(3, last_name);
        stmt.setString(4, email);
        stmt.setString(5, password);
        stmt.setString(6, role);
        stmt.executeUpdate();
        System.out.println("User with email " + email + " has been created");
    }

    /////////////////////////////////////////////// READ ////////////////////////////////
    public static void readUser() throws SQLException {
        System.out.print("Enter user id: ");
        Scanner scanner = new Scanner(System.in);
        int user_id = scanner.nextInt();
        scanner.nextLine();

        // retrieve user from users table
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT email, first_name, last_name, role FROM users WHERE user_id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: user not found");
            return;
        }

        // display user information
        System.out.println("Email: " + rs.getString("email"));
        System.out.println("FirstName: " + rs.getString("first_name"));
        System.out.println("LastName: " + rs.getString("last_name"));
        System.out.println("Role: " + rs.getString("role"));
    }

    /////////////////////////////////////////////// UPDATE ////////////////////////////////
    public static void updateUser() throws SQLException {
        // get user input
        DatabaseManager db = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your user ID: ");

        int user_id = scanner.nextInt();
        scanner.nextLine();

        int currentUserID = Integer.parseInt(RegisterLoginFacade.getUser_id());

        while( user_id != currentUserID ) {
            System.out.println("--- You can't use this user ID , you need to choose your user ID to continue ! ---");
            System.out.println("");
            System.out.println("ReEnter your user ID:");
            user_id = scanner.nextInt();
            scanner.nextLine();
        }
        System.out.print("Enter new first name: ");
        String first_name = scanner.nextLine();
        System.out.print("Enter new last name: ");
        String last_name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        // check if user is allowed to update the user


        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            System.out.println("Error: user not found");
            return;
        }

        int currentUserId = rs.getInt("user_id");
        if (currentUserId != user_id) {
            System.out.println("Error: not authorized to update user");
            return;
        }

        // update user in database
        sql = "UPDATE users SET first_name = ?, last_name = ? ,email = ?, password = ?  WHERE user_id = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, first_name);
        stmt.setString(2, last_name);
        stmt.setString(3, email);
        stmt.setString(4, BCrypt.hashpw(password, BCrypt.gensalt()));
        stmt.setInt(5, user_id);
        stmt.executeUpdate();
        System.out.println("-----------------------------------------------------------");
        System.out.println("Your account updated successfully you're new informations :");
        System.out.println("User ID : " + user_id);
        System.out.println("Email: " + email);
        System.out.println("FirstName: " + first_name);
        System.out.println("LastName: " + last_name);
        System.out.println("-----------------------------------------------------------");
    }

    /////////////////////////////////////////////// DELETE ////////////////////////////////
    public static void deleteUser() throws SQLException {
        // get user input
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user id: ");
        int user_id = scanner.nextInt();
        scanner.nextLine();

        int currentUserID = Integer.parseInt(RegisterLoginFacade.getUser_id());

        while( user_id != currentUserID ) {
            System.out.println("--- You can't use this user ID , you need to choose your user ID to continue ! ---");
            System.out.println("");
            System.out.println("ReEnter your user ID:");
            user_id = scanner.nextInt();
            scanner.nextLine();
        }
        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM users WHERE user_id = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: user not found");
            return;
        }

        // delete user from database
        sql = "DELETE FROM users WHERE user_id = ?";
        stmt = db.conn.prepareStatement(sql);
        stmt.setInt(1, user_id);
        stmt.executeUpdate();
        System.out.println("Your account deleted successfully");
    }

}

package com.supstore.core.facade;

import com.supstore.core.database.DatabaseManager;
import com.supstore.core.menu.AdminHomeMenu;
import com.supstore.core.menu.UserHomeMenu;

import java.sql.*;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterLoginFacade {

    private static String user_id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String role;


    public RegisterLoginFacade()//empty constructor
    {
    }
    //create static variable of user
    public static RegisterLoginFacade loggedRegisterLoginFacade = new RegisterLoginFacade();

    // public constructor of User

    public RegisterLoginFacade(String uid, String fnam, String lname, String eml)
    {
        this.user_id = uid;
        this.first_name= fnam;
        this.last_name = lname;
        this.email = eml;

    }

    /////constructor for current connected user

    public RegisterLoginFacade(String curruid, String curremail, String currpswd){
        this.email = curremail;
        this.password = currpswd;
        this.user_id = curruid;
    }



    ///////////////////////////////////////////////getter setter methods/////////////////////////////////
    public static String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }






    //////////////////////////////////////////Register Here !////////////////////////////////////////////////////////
    public void createNewUser() throws SQLException {

        Scanner input = new Scanner(System.in);

        System.out.println("Please enter following details for signUp:");

        System.out.println("Your new id:");
        //user_id = input.next();
        setUser_id(input.nextLine());
        CheckUserID(user_id);

        System.out.println("first name: ");
        setFirst_name(input.nextLine());

        System.out.println("last name:");
        setLast_name(input.nextLine());

        System.out.println("Email :");
        setEmail(input.next());
        CheckUserEmail(email);

        System.out.println("Enter a valid password and cannot be same as userID :");
        setPassword(input.next());
        password = BCrypt.hashpw(password, BCrypt.gensalt());

        setRole("user");


        DatabaseManager db = new DatabaseManager();
        String sql = "SELECT * FROM whitelist WHERE email = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            System.out.println("Error: email not whitelisted, You need to ask admin to whitelist your email !");
            System.out.println("Thanks, Try again later !");
            System.out.println("--------------------------------------------------");
            System.out.println("");
            return;
        }

            String success  = " |------------------ |Registration Successfully !! |-------------------|";
            System.out.println("");
            String msql = "INSERT INTO users VALUES('"+getUser_id()+"', '"+getFirst_name()+"', '"+ getLast_name() +"', '"+getEmail()+"', '"+getPassword()+"', '"+ getRole()+"')";
            db.st.executeUpdate(msql);
            System.out.println(success);
            System.out.println("         Welcome to our iStore " + first_name + "!");
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("");
        }







    ///////////////////////////////////////////////////////////Show main page (ConnectInterface)////////////////////////////////
    public void showMainPage() throws SQLException {
        //declare varaibles
        Scanner input = new Scanner(System.in);
        String selection = "";

        while (!selection.equals("x")) {
            //display the menu
            System.out.println("|------------ Welcome back to iStore ------------\n");
            System.out.println("|Please make your selection");
            System.out.println("|1: Login to your iStore account");
            System.out.println("|x: Exit !");
            System.out.println("");
            System.out.print("Enter choice: ");

            //get the selection from the user
            selection = input.nextLine();
            System.out.println();

            if (selection.equals("1")) {
                //login
                login();
            } else if (selection.equals("x")) {
                break;
            }
        }
    }


    ////////////////////////////////////////////Login Here !//////////////////////////////////////////

    public static void login() throws SQLException {
        //we need id ,email and password to login
        Scanner input = new Scanner(System.in);
        // String  user_id = "";
        String email = "";
        String password = "";
        //get the login info.
        System.out.println("Please enter your Email :");
        email = input.next();
        System.out.println("Please enter your Password :");
        password = input.next();

        //Connect to my database istore
        DatabaseManager db = new DatabaseManager();

        db.rs = db.st.executeQuery("SELECT * FROM users WHERE email = '" + email  + "'  and role='admin'");
            //Verify if exist in database
            if (db.rs.next()) {
                System.out.println("----| Yay! Login successful welcome Admin |----, " + email + "! ****");
                System.out.println("");
                //// set current user details
                loggedRegisterLoginFacade = new RegisterLoginFacade(db.rs.getString("user_id"), db.rs.getString("email"),db.rs.getString("password"));
                AdminHomeMenu up = new AdminHomeMenu();
                up.adminInterface();         ////Test on current logged user
            }

            db.usr = db.st.executeQuery("SELECT * FROM users WHERE email = '" + email+ "'  AND role='user'");

            if (db.usr.next()) {
                System.out.println("*** Yay! Login successful welcome back , (" + email + ") ****");
                System.out.println("");

                //// set current user details
                loggedRegisterLoginFacade = new RegisterLoginFacade(db.usr.getString("user_id"), db.usr.getString("email"),db.usr.getString("password"));
                UserHomeMenu ip = new UserHomeMenu();
                ip.userInterface();         ////Test on current logged user
            }
            else {
                System.out.println("---- Sorry your Login is unsuccessful, Please verify your email/password and try again!!  ----");
            }

        }



    //////////////////////////////////////////// Check existed ID + Email(Used/Whitelisted) !//////////////////////////////////////////
    public void CheckUserID(String user_id) throws SQLException {
        DatabaseManager db = new DatabaseManager();
        Scanner input = new Scanner(System.in);
        db.st = db.conn.createStatement();
        db.rs = db.st.executeQuery("select user_id from users where user_id = '" +user_id+ "'");
        if( db.rs.next()){
                System.out.println("--- You can't use this user ID as it already exist, choose another user ID ---");
                System.out.println("");
                System.out.println("Enter another user id:");
                setUser_id(input.nextLine());
            }
            else{
                System.out.println(" The user_id used is unique !");
            }
        }
    public void CheckUserEmail(String email) throws SQLException {
        DatabaseManager db = new DatabaseManager();
        Scanner input = new Scanner(System.in);
        db.st = db.conn.createStatement();
        db.rs = db.st.executeQuery("select user_id from users where email = '" +email+ "'");
        while( db.rs.next()){
            System.out.println("Error: You can't use this email as it already exist , please use another email");
            System.out.println("");
            System.out.println("New Email : ");
            setEmail(input.nextLine());
        }
        // check if email is whitelisted

        String sql = "SELECT * FROM whitelist WHERE email = ?";
        PreparedStatement stmt = db.conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        while(!rs.next()) {
            System.out.println("Error: email not whitelisted");
            System.out.println("");
            System.out.println("Please send a request to whitelist your email and enter it again : ");
            setEmail(input.nextLine());
        }
        if(rs.next() && db.rs.next() ){
            System.out.println(" Email used successfully ");
        }
    }

    //To string function
    @Override
    public String toString() {
        String str = getUser_id()+" \t\t "+ getFirst_name() + " \t\t" + getLast_name();
        return str;
    }


}



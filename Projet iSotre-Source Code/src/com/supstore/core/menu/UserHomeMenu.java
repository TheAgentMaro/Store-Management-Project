package com.supstore.core.menu;

import com.supstore.core.facade.RegisterLoginFacade;
import com.supstore.core.management.StoreManagement;
import com.supstore.core.management.UserManagement;

import java.sql.SQLException;
import java.util.Scanner;
public class UserHomeMenu {

    RegisterLoginFacade registerLoginFacade;
    public UserHomeMenu(){
        this.registerLoginFacade = RegisterLoginFacade.loggedRegisterLoginFacade;
    }

    public void userInterface() throws SQLException {

        Scanner input = new Scanner(System.in);
        String selection = "";


        label:
        while (!selection.equals("x")) {

            System.out.println("|+++++++++++++++++++++++++++++++++|");
            System.out.println("|   -Choose what you want to do-  |");
            System.out.println("|+++++++++++++++++++++++++++++++++|");
            System.out.println("|1. Create new user account       |");
            System.out.println("|2. View user account             |");
            System.out.println("|3. Update your account           |");
            System.out.println("|4. Delete your acocunt           |");
            System.out.println("|5. Access to Store Management    |");
            System.out.println("|x. Exit                          |");
            System.out.println("|_________________________________|");
            System.out.print("Enter choice: ");

            selection = input.nextLine();
            System.out.println();

            switch (selection) {
                case "1" -> {
                    UserManagement CU = new UserManagement();
                    CU.createUser();
                }
                case "2" -> {
                    UserManagement RU = new UserManagement();
                    RU.readUser();
                }
                case "3" -> {
                    UserManagement UU = new UserManagement();
                    UU.updateUser();
                }
                case "4" -> {
                    UserManagement DU = new UserManagement();
                    DU.deleteUser();
                }
                case "5" -> {
                    StoreManagement SM  = new StoreManagement();
                    SM.displayMainMenuUser();
                }
                case "x" -> {
                    break label;
                }
            }
        }
    }

}

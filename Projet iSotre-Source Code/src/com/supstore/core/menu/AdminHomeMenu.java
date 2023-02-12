package com.supstore.core.menu;

import com.supstore.core.facade.RegisterLoginFacade;
import com.supstore.core.management.AdminManagement;
import com.supstore.core.management.StoreManagement;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminHomeMenu {


    RegisterLoginFacade registerLoginFacade;
    public AdminHomeMenu(){
        this.registerLoginFacade = RegisterLoginFacade.loggedRegisterLoginFacade;
    }

    public void adminInterface() throws SQLException {

        Scanner input = new Scanner(System.in);
        String selection = "";

        label:
        while (!selection.equals("x")) {

            System.out.println("|+++++++++++++++++++++++++++++++++|");
            System.out.println("|   Choose what you want to do :  |");
            System.out.println("|+++++++++++++++++++++++++++++++++|");
            System.out.println("|1. Whitelist email               |");
            System.out.println("|2. Create store                  |");
            System.out.println("|3. Delete store                  |");
            System.out.println("|4. Access to Store Management    |");
            System.out.println("|5. List employees for store      |");
            System.out.println("|6. Update User                   |");
            System.out.println("|7. Delete User                   |");
            System.out.println("|x. Exit                          |");
            System.out.println("|_________________________________|");
            System.out.println("");
            System.out.print("Enter option: ");



            selection = input.nextLine();
            System.out.println();

            switch (selection) {
                case "1" -> {
                    AdminManagement WE = new AdminManagement();
                    WE.whitelistEmail();
                }
                case "2" -> {
                    AdminManagement CS = new AdminManagement();
                    CS.createStore();
                }
                case "3" -> {
                    AdminManagement DS = new AdminManagement();
                    DS.deleteStore();
                }
                case "4" -> {
                    StoreManagement SM  = new StoreManagement();
                    SM.StoreManagement();
                }
                case "5" -> {
                    AdminManagement LSE = new AdminManagement();
                    LSE.listStoreEmployees();
                }
                case "6" -> {
                    AdminManagement UU = new AdminManagement();
                    UU.updateUser();
                }
                case "7" -> {
                    AdminManagement DU = new AdminManagement();
                    DU.deleteUser();
                }
                case "x" -> {
                    break label;
                }
            }
        }
    }

}

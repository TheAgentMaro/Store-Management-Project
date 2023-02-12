package com.supstore.core.facade;

import java.sql.SQLException;
import java.util.Scanner;

public class iStoreMainInterface {
    public static void main(String[] args) throws SQLException {

        Scanner input = new Scanner(System.in);
        String selection = "";
        label:
        while(!selection.equals("x"))
        {
            //display the
            System.out.println("+------***************************-----+|");
            System.out.println("+---------| Welcome to iStore |---------|");
            System.out.println("+------**************************-------|");
            System.out.println("|                                       |");
            System.out.println("|  --| Please choose any from below |-- |");
            System.out.println("|                                       |");
            System.out.println("|  1: Sign up for new account           |");
            System.out.println("|  2: Login to existing account         |");
            System.out.println("|  x: Exit !                            |");
            System.out.println("|                                       |");
            System.out.println("|+-----*************************-------+|");
            System.out.println("");
            System.out.println("");
            System.out.print("Enter choice: ");

            //get the selection from the user
            selection = input.nextLine();
            System.out.println();

            switch (selection) {
                case "1":
                    //open a new account
                    RegisterLoginFacade UA = new RegisterLoginFacade();
                    UA.createNewUser();
                    break;
                case "2":
                    //go to the online system
                    RegisterLoginFacade log = new RegisterLoginFacade();
                    log.showMainPage();
                    break;
                case "x":
                    break label;
            }
        }
    }
}
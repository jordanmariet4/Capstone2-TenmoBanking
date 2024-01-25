package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);
    // private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printUsers(User[] Users) {
        System.out.println("--------------------------------------------");
        System.out.println("Users");
        System.out.printf("%-4s %-20s \n", "ID", "Name");
        System.out.println("--------------------------------------------");
        for (User tenmoUser : Users) {//maybe add logic to not print self using if does not equal current user
                System.out.printf("%-5d %-20s%n", tenmoUser.getId(), tenmoUser.getUsername());
            }
        System.out.println("--------------------------------------------");

    }
    public void printRequest(Transfer[] transfers) {
            System.out.println("--------------------------------------------");
            System.out.println("Users");
            System.out.printf("%-4s %-20s %-10s\n", "ID", "Name", "Amount");
            System.out.println("--------------------------------------------");
            for (Transfer transfer : transfers) {
                if (transfer.getTransfer_status_id() == 1) {
                    System.out.printf("%-5d %-20s %-10.2f%n", transfer.getTransfer_id(), transfer.getUsername(), transfer.getAmount());
                }
            }

            System.out.println("--------------------------------------------");
        }



    public void printTransfers(Transfer[] transfers) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfers");
        System.out.printf("%-4s %-20s %-10s\n", "ID", "Name", "Amount");
        System.out.println("--------------------------------------------");
        for (Transfer transfer : transfers) {
            System.out.printf("%-5d %-20s %-10.2f%n", transfer.getTransfer_id(), transfer.getUsername(), transfer.getAmount());
        }
        System.out.println("--------------------------------------------");
    }

    public void printTransferDetails(Transfer transfers) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println("Id: " + transfers.getTransfer_id());
        System.out.println("From: " + transfers.getFrom_username());
        System.out.println("To: " +transfers.getTo_username());
        System.out.println("Type: " + transfers.getTransfer_type_desc());
        System.out.println("Status: " + transfers.getTransfer_status_desc());
        System.out.println("Amount: " +transfers.getAmount());

    }
    public void printRequestMenu() {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject");
        System.out.println();
    }
}

package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import com.techelevator.tenmo.model.Transfer;


import java.math.BigDecimal;
import java.util.Scanner;

public class App {
    private final Scanner in;

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private final AccountService accountService = new AccountService();
    private final UserService tenmo_userService = new UserService();
    private final TransferService transferService = new TransferService();

    public App() {
        this.in = new Scanner(System.in);
    }

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        int userId = currentUser.getUser().getId();
        accountService.setAuthToken(currentUser.getToken());
        Account account = accountService.getAccountByUserId(userId);
        System.out.println("Your current balance is: $" + account.getBalance());

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        int userId = currentUser.getUser().getId();
        transferService.setAuthToken(currentUser.getToken());
        Transfer[] transfer = transferService.viewTransferHistory(userId);
        consoleService.printTransfers(transfer);
        System.out.println("Please enter transfer ID to view details (0 to cancel): ");
        int transferID = Integer.parseInt(in.nextLine());
        if (transferID == 0) {
            System.out.println("Transaction canceled.");
        } else {
            Transfer transfer1 = transferService.viewTransferDetails(transferID);
            consoleService.printTransferDetails(transfer1);
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        int userId = currentUser.getUser().getId();
        transferService.setAuthToken(currentUser.getToken());
        Transfer[] transfers = transferService.viewPendingRequests(userId);
        if (transfers.length > 0) {
        consoleService.printRequest(transfers);
        viewCurrentBalance();
        System.out.println("Please enter Transfer ID to approve/reject (0 to cancel): ");
        int transferID = Integer.parseInt(in.nextLine());


        Transfer transfer = null;
        for (Transfer transfered : transfers) {
            if (transfered.getTransfer_id() == transferID) {
                transfer = transfered;
                break;
            }
        }


            int menuSelection = -1;
            while (menuSelection != 0) {
                consoleService.printRequestMenu();
                menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");

                if (menuSelection == 1) {
                    transferService.updateBalance(transfer);
                    transfer.setTransfer_status_id(2);
                    transferService.updateTransferStatus(transfer);
                    transferService.insertTransfer(transfer);
                    System.out.println("Transfer Approved");
                    viewCurrentBalance();
                    break;

                } else if (menuSelection == 2) {
                    transfer.setTransfer_status_id(3);
                    transferService.updateTransferStatus(transfer);
                    transferService.insertTransfer(transfer);
                    System.out.println("Request Rejected");
                    break;
                } else if (menuSelection == 0) {
                    System.out.println("Cancelled");
                    break;
                }
            }
        }else{
            System.out.println("No Pending Request");
        }
    }


    private void sendBucks() {
        // TODO Auto-generated method stub
        User[] users = tenmo_userService.listUsers();
        transferService.setAuthToken(currentUser.getToken());
        consoleService.printUsers(users);
        System.out.println("Enter ID of user you are sending to (0 to cancel): ");
        int recipientID = Integer.parseInt(in.nextLine());
        if (recipientID == 0) {
            System.out.println("Transaction canceled.");
        } else {
            System.out.println("Enter amount: ");
            String input = in.nextLine();
            BigDecimal amount = new BigDecimal(input);

            int userId = currentUser.getUser().getId();
            Account account = accountService.getAccountByUserId(userId);

            if (recipientID != userId && amount.compareTo(account.getBalance()) <= 0 && amount.compareTo(BigDecimal.ZERO) > 0) {
                Transfer transfer = new Transfer();
                transfer.setAccount_to(recipientID);
                transfer.setAccount_from(userId);
                transfer.setAmount(amount);
                transfer.setTransfer_status_id(2);
                transferService.sendBucks(transfer);
                transferService.insertTransfer(transfer);
                System.out.println("Your transfer has been approved");
                viewCurrentBalance();
            } else {
                System.out.println("****Please Try Again****");
            }
        }
    }


    private void requestBucks() {
        // TODO Auto-generated method stub
        int userToId = currentUser.getUser().getId();
        transferService.setAuthToken(currentUser.getToken());
        User[] users = tenmo_userService.listUsers();
        consoleService.printUsers(users);

        System.out.println("Enter ID of user you are requesting from (0 to cancel): ");


        int userFromId = Integer.parseInt(in.nextLine());

        if (userFromId == 0) {
            System.out.println("Transaction canceled.");
        } else {
            System.out.println("Enter amount: ");
            String input = in.nextLine();
            BigDecimal amount = new BigDecimal(input);

            Account accountTo = accountService.getAccountByUserId(userToId);
            Account accountFrom = accountService.getAccountByUserId(userFromId);

            if (accountTo != null && accountFrom != null && !accountTo.equals(accountFrom) && amount.compareTo(BigDecimal.ZERO) > 0) {

                // Check if transfer_status_id is 1 (assuming this is a property of Transfer)
                int transferStatusId = 1;
                if (transferStatusId == 1) {
                    Transfer transfer = new Transfer();
                    transfer.setAccount_to(accountTo.getAccount_id());
                    transfer.setAccount_from(accountFrom.getAccount_id());
                    transfer.setAmount(amount);
                    transfer.setTransfer_status_id(1);
                    transfer.setTransfer_type_id(1);

                    transferService.requestBucks(transfer);
                    System.out.println("Your request is pending");
                } else {
                    System.out.println("Request cannot be processed. Transfer status is not valid.");
                }

            } else {
                System.out.println("****Please Try Again****");
            }
        }
    }
}



package com.pluralsight;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // testing loadTransactions method.
//        System.out.println("Transactions:");
//        for (Transaction transaction : transactions) {
//            System.out.println(transaction);
//        }

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        String line;
        //creating a file if it does not exist
        File file = new File("transactions.csv");
        try {
            //handling case if file does not exist
            if (!file.exists()){
                file.createNewFile();
                System.out.println("File was created");
            }
            BufferedReader br = new BufferedReader( new FileReader(FILE_NAME));
            while ((line = br.readLine()) !=null ){
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                //new transaction object with values above
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                //adding transaction to an ArrayList
                transactions.add(transaction);
            }
            br.close();
        } catch (Exception e) {
            System.err.println( "An error has occurred, please try again ");
            e.printStackTrace();
        }
    }

    private static void addDeposit(Scanner scanner) {
        try {
            System.out.println("Enter the date in the following format yyyy-MM-dd");
            String dateInput = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER); //parse String into LocalDate

            System.out.println("Enter the time in the following format HH:mm:ss");
            String timeInput = scanner.nextLine();
            LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);

            System.out.println("Enter description: ");
            String description = scanner.nextLine();

            System.out.println("Enter a vendor");
            String vendor = scanner.nextLine();

            System.out.println("Enter amount");
            double amount = scanner.nextDouble();
//          scanner.nextLine(); //consumes extra line

            //check if entered number is positive
            if ( amount <= 0 ){
                System.out.println("In order to deposit funds the amount must be positive, please try again");
            }

            Transaction transaction = new Transaction(date, time, description, vendor, amount);
            transactions.add(transaction);

            System.out.println( "Thank you for the deposit of $" + amount);
        } catch (Exception e) {
            System.out.println("Invalid input");
        }


    }

    private static void addPayment(Scanner scanner) {
        try {
            System.out.println("Enter the date in the following format yyyy-MM-dd");
            String dateInput = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateInput, DATE_FORMATTER); //parse String into LocalDate

            System.out.println("Enter the time in the following format HH:mm:ss");
            String timeInput = scanner.nextLine();
            LocalTime time = LocalTime.parse(timeInput, TIME_FORMATTER);

            System.out.println("Enter description: ");
            String description = scanner.nextLine();

            System.out.println("Enter a vendor");
            String vendor = scanner.nextLine();

            System.out.println("Enter amount");
            double amount = scanner.nextDouble();
            scanner.nextLine(); //consumes extra line

            //check if entered number is positive
            if ( amount <= 0 ){
                System.out.println("In order to make a payment the amount must be positive, please try again");
                return;
            }
            double negativeAmount = amount * -1;

            Transaction transaction = new Transaction(date, time, description, vendor, negativeAmount);
            transactions.add(transaction);

            System.out.println( "Thank you for the payment of $" + amount);
        } catch (Exception e) {
            System.out.println("Invalid input");
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.println("All transactions:" );
        //for each loop iterates through each Transaction object in the list and calls toString method
        for (Transaction transaction : transactions) {
            System.out.println(transaction.toString());
        }
    }

    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}
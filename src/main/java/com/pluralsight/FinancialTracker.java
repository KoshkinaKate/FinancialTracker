package com.pluralsight;

import java.io.*;
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
            scanner.nextLine(); //consumes extra line

            //check if entered number is positive
            if ( amount <= 0 ){
                System.out.println("In order to deposit funds the amount must be positive, please try again");
                return; //finishes method if above is true
            }

            Transaction transaction = new Transaction(date, time, description, vendor, amount);
            transactions.add(transaction);

            BufferedWriter myWriter = new BufferedWriter(new FileWriter (FILE_NAME, true)); //true means it appends and not overwrite
            myWriter.write(transaction.toString());
            myWriter.newLine();
            myWriter.close();


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
            BufferedWriter myWriter = new BufferedWriter(new FileWriter (FILE_NAME, true)); //true means it appends and not overwrite
            myWriter.write(transaction.toString());
            myWriter.newLine();
            myWriter.close();

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
    //displaying all transactions
    private static void displayLedger() {
        System.out.println("All transactions:" );
        //for each loop iterates through each Transaction object in the list and calls toString method
        for (Transaction transaction : transactions) {
//            System.out.println(transaction.toString());
        System.out.println("Date: " + transaction.getDate() + "|" + "Time: " + transaction.getTime() + "|" + " Description: " +
        transaction.getDescription() + "Vendor: " + transaction.getVendor() + "|" + " Amount: " + transaction.getAmount());
        }

    }
    //displaying positive transactions
    private static void displayDeposits() {
        System.out.println("Deposit transactions: ");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() >= 0){
//                System.out.println(transaction.toString());
            System.out.println("Date: " + transaction.getDate() + "|" + "Time: " + transaction.getTime() + "|" + " Description: " +
            transaction.getDescription() + "Vendor: " + transaction.getVendor() + "|" + " Amount: " + transaction.getAmount());
            }
        }
    }
    //displaying negative transactions
    private static void displayPayments() {
        System.out.println("Payment transactions: ");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() <= 0){
//                System.out.println(transaction.toString());
            System.out.println("Date: " + transaction.getDate() + "|" + "Time: " + transaction.getTime() + "|" + " Description: " +
            transaction.getDescription() + "Vendor: " + transaction.getVendor() + "|" + " Amount: " + transaction.getAmount());
            }


        }
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
            LocalDate today = LocalDate.now();


            switch (input) {
                case "1": //month to date transactions
                    LocalDate firstDayOfMonth = today.withDayOfMonth(1);
                    filterTransactionsByDate(firstDayOfMonth , today);
                    break;

                case "2": //previous month transactions
                    LocalDate firstDayOfPrevMonth = today.minusMonths(1).withDayOfMonth(1);
                    LocalDate lastDayOfPrevMonth = firstDayOfPrevMonth.withDayOfMonth(firstDayOfPrevMonth.lengthOfMonth());
                    filterTransactionsByDate(firstDayOfPrevMonth, lastDayOfPrevMonth );
                    break;

                case "3": //current year transactions
                    LocalDate firstDayOfYear = today.withDayOfYear(1);
                    filterTransactionsByDate(firstDayOfYear, today);
                    break;

                case "4": //previous year transactions
                    LocalDate firstDayOfPrevYear = today.minusYears(1).withDayOfYear(1);
                    LocalDate lastDayOfPrevYear = today.minusYears(1).withMonth(12).withDayOfMonth(31);
                    filterTransactionsByDate(firstDayOfPrevYear, lastDayOfPrevYear);
                    break;

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
        boolean running = false; //as of now - not transactions fount yet
        System.out.println("Transactions from " + startDate + " to " + endDate + ":");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();

            if ((transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                    (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate))) {

                System.out.println("Date: " + transaction.getDate());
                System.out.println("Time: " + transaction.getTime());
                System.out.println("Vendor: " + transaction.getVendor());
                System.out.println("Description: " + transaction.getDescription());
                System.out.println("Amount: " + transaction.getAmount());
                System.out.println("==========================");
                running = true; // found at least one transaction match
            }
        }

        if (!running) {
            System.out.println("No transactions found");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        boolean running = false;
        System.out.println("Results for Vendor " + vendor);

        for (Transaction transaction : transactions) {
            String vendorName = transaction.getVendor();
            if (vendorName.equalsIgnoreCase(vendor)){
                System.out.println("Date: " + transaction.getDate());
                System.out.println("Time: " + transaction.getTime());
                System.out.println("Description: " + transaction.getDescription());
                System.out.println("Amount: " + transaction.getAmount());
                System.out.println("==========================");
                running = true;
            }
        }
        if (!running) {
            System.out.println("No Vendors found");
        }
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}
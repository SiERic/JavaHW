package me.sieric.db;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console application, implements an interactive phone book
 * Supported commands:
 * 0
 *   exit program
 * 1 NAME NUMBER
 *   add a new record (NAME, NUMBER)
 * 2 NAME
 *   print all telephone numbers with given NAME
 * 3 NUMBER
 *   print all users with given NUMBER
 * 4 NAME NUMBER
 *   delete record (NAME, NUMBER)
 * 5 NAME NUMBER NEW_NAME
 *   change name for record (NAME, NUMBER)
 * 6 NAME NUMBER NEW_NUMBER
 *   change number for record (NAME, NUMBER)
 * 7
 *   print all records
 * 8
 *   help
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System. in);
    private static PhoneBook phoneBook = new PhoneBook("PhoneBook");

    public static void main(String[] args) {
        printUsage();
        while (true) {
            try {
                String command = SCANNER.next();
                switch (command) {
                      case "0": {
                        phoneBook.clear();
                        return;
                    } case "1": {
                        addRecord(readRecord());
                        break;
                    } case "2": {
                        findByName(readName());
                        break;
                    } case "3": {
                        findByNumber(readNumber());
                        break;
                    } case "4": {
                        deleteRecord(readRecord());
                        break;
                    } case "5": {
                        changeName(readRecord(), readName());
                        break;
                    } case "6": {
                        changeNumber(readRecord(), readNumber());
                        break;
                    } case "7": {
                        printAll();
                        break;
                    } case "8": {
                        printUsage();
                        break;
                    }
                }
            } catch (NoSuchElementException e) {
                phoneBook.clear();
                return;
            }
        }
    }

    private static PhoneBook.PhoneBookRecord readRecord() {
        return new PhoneBook.PhoneBookRecord(readName(), readNumber());
    }

    private static String readName() {
        return SCANNER.next();
    }

    private static String readNumber() {
        return SCANNER.next();
    }

    /** Prints a usage information */
    private static void printUsage() {
        System.out.println("Interactive phone book");
        System.out.println("Usage:");
        System.out.println("0");
        System.out.println("  exit program");
        System.out.println("1 NAME NUMBER");
        System.out.println("  add a new record (NAME, NUMBER)");
        System.out.println("2 NAME");
        System.out.println("  print all telephone numbers with given NAME");
        System.out.println("3 NUMBER");
        System.out.println("  print all users with given NUMBER");
        System.out.println("4 NAME NUMBER");
        System.out.println("  delete record (NAME, NUMBER)");
        System.out.println("5 NAME NUMBER NEW_NAME");
        System.out.println("  change name for record (NAME, NUMBER)");
        System.out.println("6 NAME NUMBER NEW_NUMBER");
        System.out.println("  change number for record (NAME, NUMBER)");
        System.out.println("7");
        System.out.println("  print all records");
        System.out.println("8");
        System.out.println("  help");
    }

    /**
     * Adds a new record into phone book
     * Even if it already exists
     * @param record - a record to add
     */
    private static void addRecord(PhoneBook.PhoneBookRecord record) {
        if (phoneBook.add(record)) {
            System.out.println("Record added successfully");
        } else {
            System.out.println("Record already exists in the phone book");
        }
    }

    /**
     * Prints all records with given name
     * @param name - a name to find by
     */
    private static void findByName(String name) {
        var phonesByName = phoneBook.getByName(name);
        if (phonesByName.isEmpty()) {
            System.out.println("There are no records with this name in the phone book");
        } else {
            for (var record : phonesByName) {
                System.out.println(record.getName() + " " + record.getNumber());
            }
        }
    }

    /**
     * Prints all records with given name
     * @param number - a number to find by
     */
    private static void findByNumber(String number) {
        var phonesByNumber = phoneBook.getByNumber(number);
        if (phonesByNumber.isEmpty()) {
            System.out.println("There are no records with this number in the phone book");
        } else {
            for (var record : phonesByNumber) {
                System.out.println(record.getName() + " " + record.getNumber());
            }
        }
    }

    /**
     * Deletes one of the records with given name and number
     * Prints a message about the success of the operation
     * @param record - a record to delete
     */
    private static void deleteRecord(PhoneBook.PhoneBookRecord record) {
        if (phoneBook.delete(record)) {
            System.out.println("Record deleted successfully");
        } else {
            System.out.println("Record does not exist");
        }
    }

    /**
     * Change a name in a record with given name and number to newName
     * Prints a message about the success of the operation
     * @param record - a record to change
     * @param newName - a new name
     */
    private static void changeName(PhoneBook.PhoneBookRecord record, String newName) {
        if (phoneBook.changeName(record, newName)) {
            System.out.println("Name changed successfully");
        } else {
            System.out.println("Record does not exists");
        }
    }

    /**
     * Change a number in a record with given name and number to newNumber
     * Prints a message about the success of the operation
     * @param record - a record to change
     * @param newNumber - a new number
     */
    private static void changeNumber(PhoneBook.PhoneBookRecord record, String newNumber) {
        if (phoneBook.changeNumber(record, newNumber)) {
            System.out.println("Number changed successfully");
        } else {
            System.out.println("Record does not exists");
        }
    }

    /** Prints all of the records in the phone book */
    private static void printAll() {
        for (var record : phoneBook.getAll()) {
            System.out.println(record.getName() + " " + record.getNumber());
        }
    }
}

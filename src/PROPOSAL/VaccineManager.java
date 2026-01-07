package PROPOSAL;

import java.time.LocalDate;
import java.io.*;
import java.util.*;

public class VaccineManager {
    static LinkedList<Vaccines> inventory = new LinkedList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadDataFromFile("C:\\Users\\ASUS\\Documents\\JAVA\\VaccineProject\\vaccines.txt");

        boolean running = true;
        while (running) {
            System.out.println("\n--- VACCINE INVENTORY SYSTEM ---");
            System.out.println("1. View Inventory (Traverse List)");
            System.out.println("2. Add New Batch (Insert to Linked List)");
            System.out.println("3. Dispense a Vial (Pop from Stack)");
            System.out.println("4. Remove Empty/Expired Batches");
            System.out.println("5. Exit");
            System.out.print("Select option: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayInventory();
                    break;
                case "2":
                    addNewBatch();
                    break;
                case "3":
                    dispenseVaccine();
                    break;
                case "4":
                    cleanupInventory();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    public static void loadDataFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String name = data[0].trim();
                    int qty = Integer.parseInt(data[1].trim());
                    String mfr = data[2].trim();
                    String exp = data[3].trim();
                    
                    inventory.add(new Vaccines(name, qty, mfr, exp));
                }
            }
            System.out.println("Data loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static void displayInventory() {
        if (inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            System.out.println("\nCurrent Stock:");
            for (Vaccines batch : inventory) {
                System.out.println(batch);
            }
        }
    }

    public static void addNewBatch() {
        System.out.print("Enter Vaccine Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Manufacturer: ");
        String mfr = scanner.nextLine();
        System.out.print("Enter Expiry (YYYY-MM-DD): ");
        String exp = scanner.nextLine();

        inventory.add(new Vaccines(name, qty, mfr, exp));
        System.out.println("New shipment added to Linked List.");
    }

    public static void dispenseVaccine() {
        System.out.print("Enter Vaccine Name to dispense: ");
        String searchName = scanner.nextLine();
        boolean found = false;

        for (Vaccines batch : inventory) {
            if (batch.getVaccineName().equalsIgnoreCase(searchName) && batch.getCurrentQuantity() > 0) {
                batch.dispenseVial(); 
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Vaccine not found or out of stock.");
        }
    }

    public static void cleanupInventory() {
        Iterator<Vaccines> iterator = inventory.iterator();
        int removedCount = 0;
        
        String currentDate = LocalDate.now().toString(); 
        
        System.out.println("Checking for expired batches before: " + currentDate);

        while (iterator.hasNext()) {
            Vaccines batch = iterator.next();
            
            if (batch.getCurrentQuantity() == 0) {
                System.out.println("Removing empty batch: " + batch.getVaccineName());
                iterator.remove();
                removedCount++;
            } 
            else if (batch.getExpiryDate().compareTo(currentDate) < 0) {
                System.out.println("Removing expired batch (" + batch.getExpiryDate() + "): " + batch.getVaccineName());
                iterator.remove();
                removedCount++;
            }
        }
        
        if (removedCount > 0) {
            System.out.println("Cleanup complete. Removed " + removedCount + " batches.");
        } else {
            System.out.println("No empty or expired batches found.");
        }
    }
}
import model.*;
import service.LostAndFoundManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class providing a command-line interface (CLI)
 * for the Campus Lost and Found Matching System.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final LostAndFoundManager manager = new LostAndFoundManager();

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   WELCOME TO CAMPUS LOST & FOUND MATCHING SYSTEM");
        System.out.println("   Helping students & staff reunite with their belongings");
        System.out.println("============================================================");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readInt("Enter your choice (1-10): ", 1, 10);
            System.out.println();

            switch (choice) {
                case 1:
                    reportLostItem();
                    break;
                case 2:
                    reportFoundItem();
                    break;
                case 3:
                    viewAllLostItems();
                    break;
                case 4:
                    viewAllFoundItems();
                    break;
                case 5:
                    runAutomatedMatching();
                    break;
                case 6:
                    checkMatchesForSpecificItem();
                    break;
                case 7:
                    searchItems();
                    break;
                case 8:
                    claimOrResolveItem();
                    break;
                case 9:
                    viewItemDetails();
                    break;
                case 10:
                    System.out.println("Saving data... Thank you for using Campus Lost & Found!");
                    manager.persistAll();
                    running = false;
                    break;
            }
            if (running) {
                System.out.println("\nPress ENTER to return to the main menu...");
                scanner.nextLine();
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n====================== MAIN MENU ======================");
        System.out.println(" 1. Report a Lost Item");
        System.out.println(" 2. Report a Found Item");
        System.out.println(" 3. View All Lost Items");
        System.out.println(" 4. View All Found Items");
        System.out.println(" 5. Run Automated System Matcher (Find All Matches)");
        System.out.println(" 6. Find Potential Matches for a Specific Item");
        System.out.println(" 7. Search Items (Keyword, Category, Location)");
        System.out.println(" 8. Claim / Mark Item as Resolved");
        System.out.println(" 9. View Item Details by ID");
        System.out.println(" 10. Save & Exit");
        System.out.println("=======================================================");
    }

    // ---------------------------------------------------------
    // 1. Report Lost Item
    // ---------------------------------------------------------
    private static void reportLostItem() {
        System.out.println("--- [Report a Lost Item] ---");
        String id = manager.generateNextLostId();
        System.out.println("Assigned Item ID: " + id);

        String title = readString("Item Title (e.g. 'Blue Hydro Flask'): ", false);
        
        System.out.println("Select Category:");
        Category.displayOptions();
        int catChoice = readInt("Category choice (1-6): ", 1, 6);
        Category category = Category.fromChoice(catChoice);

        String location = readString("Where did you lose it? (e.g. 'Main Library 2nd Floor'): ", false);
        LocalDate date = readDate("Date lost (YYYY-MM-DD or press Enter for today): ");
        String description = readString("Detailed Description (color, brand, distinguishing marks): ", false);
        String ownerName = readString("Your Full Name: ", false);
        String contactEmail = readString("Your Campus Email / Phone: ", false);
        double reward = readDouble("Optional Reward offer in $ (enter 0 if none): ", 0, 10000);

        LostItem newItem = new LostItem(
                id, title, category, location, date, description, contactEmail, ItemStatus.OPEN, ownerName, reward
        );

        manager.addLostItem(newItem);
        System.out.println("\n[SUCCESS] Lost item registered successfully with ID: " + id);

        // Immediate match check
        System.out.println("\nChecking system for immediate matches...");
        List<MatchResult> instantMatches = manager.findMatchesForLost(newItem);
        if (instantMatches.isEmpty()) {
            System.out.println("No matching found items currently on record. We will keep searching!");
        } else {
            System.out.printf("FOUND %d POTENTIAL MATCH(ES) ALREADY TURNED IN!\n", instantMatches.size());
            for (MatchResult match : instantMatches) {
                match.displaySummary();
            }
        }
    }

    // ---------------------------------------------------------
    // 2. Report Found Item
    // ---------------------------------------------------------
    private static void reportFoundItem() {
        System.out.println("--- [Report a Found Item] ---");
        String id = manager.generateNextFoundId();
        System.out.println("Assigned Item ID: " + id);

        String title = readString("Item Title (e.g. 'Black North Face Umbrella'): ", false);

        System.out.println("Select Category:");
        Category.displayOptions();
        int catChoice = readInt("Category choice (1-6): ", 1, 6);
        Category category = Category.fromChoice(catChoice);

        String location = readString("Where did you find it? (e.g. 'Gymnasium Turnstiles'): ", false);
        LocalDate date = readDate("Date found (YYYY-MM-DD or press Enter for today): ");
        String description = readString("Visible Description: ", false);
        String finderName = readString("Your Name / Department: ", false);
        String storageLocation = readString("Current Storage Location (e.g. 'Security Desk', 'Room 101'): ", false);
        String contactEmail = readString("Contact Email / Phone: ", false);
        String verificationQuestion = readString("Verification Question to prove ownership (e.g. 'What is the sticker on the back?'): ", true);

        FoundItem newItem = new FoundItem(
                id, title, category, location, date, description, contactEmail, ItemStatus.OPEN,
                finderName, storageLocation, verificationQuestion
        );

        manager.addFoundItem(newItem);
        System.out.println("\n[SUCCESS] Found item registered successfully with ID: " + id);

        // Immediate match check
        System.out.println("\nChecking system for missing reports...");
        List<MatchResult> instantMatches = manager.findMatchesForFound(newItem);
        if (instantMatches.isEmpty()) {
            System.out.println("No matching lost reports found yet. Item safely cataloged.");
        } else {
            System.out.printf("FOUND %d POTENTIAL OWNER(S) LOOKING FOR THIS ITEM!\n", instantMatches.size());
            for (MatchResult match : instantMatches) {
                match.displaySummary();
            }
        }
    }

    // ---------------------------------------------------------
    // 3. View All Lost Items
    // ---------------------------------------------------------
    private static void viewAllLostItems() {
        System.out.println("========================== ALL LOST ITEMS ==========================");
        List<LostItem> items = manager.getLostItems();
        if (items.isEmpty()) {
            System.out.println("No lost items currently registered.");
            return;
        }
        for (LostItem item : items) {
            System.out.println(item.getSummaryLine());
        }
        System.out.println("Total: " + items.size() + " item(s)");
    }

    // ---------------------------------------------------------
    // 4. View All Found Items
    // ---------------------------------------------------------
    private static void viewAllFoundItems() {
        System.out.println("========================== ALL FOUND ITEMS ==========================");
        List<FoundItem> items = manager.getFoundItems();
        if (items.isEmpty()) {
            System.out.println("No found items currently registered.");
            return;
        }
        for (FoundItem item : items) {
            System.out.println(item.getSummaryLine());
        }
        System.out.println("Total: " + items.size() + " item(s)");
    }

    // ---------------------------------------------------------
    // 5. Run Automated System Matcher
    // ---------------------------------------------------------
    private static void runAutomatedMatching() {
        System.out.println("======================= AUTOMATED MATCH ENGINE =======================");
        System.out.println("Scanning database for matching Lost and Found records...");
        List<MatchResult> matches = manager.findAllSystemMatches();

        if (matches.isEmpty()) {
            System.out.println("No high-confidence matches found between open lost and found items.");
            return;
        }

        System.out.printf("Discovered %d potential match pairing(s):\n\n", matches.size());
        for (int i = 0; i < matches.size(); i++) {
            System.out.printf("[MATCH CANDIDATE #%d]\n", (i + 1));
            matches.get(i).displaySummary();
            System.out.println();
        }
        System.out.println("Tip: Use Option 8 to mark items as CLAIMED once returned!");
    }

    // ---------------------------------------------------------
    // 6. Check Matches for Specific Item
    // ---------------------------------------------------------
    private static void checkMatchesForSpecificItem() {
        System.out.println("--- [Find Matches for an Item] ---");
        String id = readString("Enter Item ID (e.g. L-101 or F-201): ", false).trim();
        Item item = manager.findById(id);

        if (item == null) {
            System.out.println("No item found with ID: " + id);
            return;
        }

        if (item instanceof LostItem) {
            LostItem lost = (LostItem) item;
            System.out.printf("Searching matches for Lost Item [%s: %s]...\n\n", lost.getId(), lost.getTitle());
            List<MatchResult> matches = manager.findMatchesForLost(lost);
            displayMatchList(matches);
        } else if (item instanceof FoundItem) {
            FoundItem found = (FoundItem) item;
            System.out.printf("Searching matches for Found Item [%s: %s]...\n\n", found.getId(), found.getTitle());
            List<MatchResult> matches = manager.findMatchesForFound(found);
            displayMatchList(matches);
        }
    }

    private static void displayMatchList(List<MatchResult> matches) {
        if (matches.isEmpty()) {
            System.out.println("No matching candidate items found.");
            return;
        }
        System.out.printf("Found %d candidate match(es):\n", matches.size());
        for (MatchResult match : matches) {
            match.displaySummary();
        }
    }

    // ---------------------------------------------------------
    // 7. Search Items
    // ---------------------------------------------------------
    private static void searchItems() {
        System.out.println("--- [Search Campus Items] ---");
        String query = readString("Enter search keyword (e.g. 'bottle', 'library', 'apple', 'card'): ", false);
        List<Item> results = manager.searchByKeyword(query);

        if (results.isEmpty()) {
            System.out.println("No items matched your search query: \"" + query + "\"");
            return;
        }

        System.out.printf("\nFound %d matching item(s):\n", results.size());
        for (Item item : results) {
            String type = (item instanceof LostItem) ? "[LOST] " : "[FOUND]";
            System.out.println(type + " " + item.getSummaryLine());
        }
    }

    // ---------------------------------------------------------
    // 8. Claim / Resolve Item
    // ---------------------------------------------------------
    private static void claimOrResolveItem() {
        System.out.println("--- [Claim / Resolve Item] ---");
        String id = readString("Enter Item ID to resolve (e.g. L-101 or F-201): ", false).trim();
        Item item = manager.findById(id);

        if (item == null) {
            System.out.println("Item not found with ID: " + id);
            return;
        }

        if (item.getStatus() == ItemStatus.CLAIMED) {
            System.out.println("This item is already marked as CLAIMED.");
            return;
        }

        item.displayDetails();
        String confirm = readString("Are you sure you want to mark this item as CLAIMED / RETURNED? (y/n): ", false);
        if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
            boolean success = manager.markAsClaimed(id);
            if (success) {
                System.out.println("[SUCCESS] Item " + id + " has been marked as CLAIMED.");
            } else {
                System.out.println("[ERROR] Failed to update item status.");
            }
        } else {
            System.out.println("Operation canceled.");
        }
    }

    // ---------------------------------------------------------
    // 9. View Item Details
    // ---------------------------------------------------------
    private static void viewItemDetails() {
        System.out.println("--- [View Item Details] ---");
        String id = readString("Enter Item ID (e.g. L-101 or F-201): ", false).trim();
        Item item = manager.findById(id);

        if (item == null) {
            System.out.println("Item not found with ID: " + id);
            return;
        }

        // Polymorphic call to displayDetails()
        item.displayDetails();
    }

    // ---------------------------------------------------------
    // Robust User Input Utility Methods
    // ---------------------------------------------------------
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return max; // Return exit choice if EOF reached
            }
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 0.0;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return 0.0;
            }
            try {
                double value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter an amount between %.2f and %.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid decimal number.");
            }
        }
    }

    private static String readString(String prompt, boolean allowEmpty) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return "";
            }
            String input = scanner.nextLine().trim();
            if (allowEmpty || !input.isEmpty()) {
                return input;
            }
            System.out.println("This field cannot be empty. Please enter a value.");
        }
    }

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return LocalDate.now();
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD (e.g., 2026-10-25) or press Enter for today.");
            }
        }
    }
}

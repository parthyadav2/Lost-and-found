package util;

import model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading items to/from plain text files.
 * Uses standard Java I/O (BufferedReader / PrintWriter) without external libraries.
 */
public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final String LOST_FILE = DATA_DIR + File.separator + "lost_items.txt";
    private static final String FOUND_FILE = DATA_DIR + File.separator + "found_items.txt";

    public static void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static List<LostItem> loadLostItems() {
        ensureDataDirectory();
        List<LostItem> list = new ArrayList<>();
        File file = new File(LOST_FILE);

        if (!file.exists()) {
            // Seed with sample data for demonstration
            List<LostItem> seedData = getSampleLostItems();
            saveLostItems(seedData);
            return seedData;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    LostItem item = LostItem.fromFileString(line);
                    if (item != null) {
                        list.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not read lost items file: " + e.getMessage());
        }

        return list;
    }

    public static List<FoundItem> loadFoundItems() {
        ensureDataDirectory();
        List<FoundItem> list = new ArrayList<>();
        File file = new File(FOUND_FILE);

        if (!file.exists()) {
            // Seed with sample data for demonstration
            List<FoundItem> seedData = getSampleFoundItems();
            saveFoundItems(seedData);
            return seedData;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    FoundItem item = FoundItem.fromFileString(line);
                    if (item != null) {
                        list.add(item);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not read found items file: " + e.getMessage());
        }

        return list;
    }

    public static void saveLostItems(List<LostItem> items) {
        ensureDataDirectory();
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOST_FILE))) {
            for (LostItem item : items) {
                writer.println(item.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving lost items: " + e.getMessage());
        }
    }

    public static void saveFoundItems(List<FoundItem> items) {
        ensureDataDirectory();
        try (PrintWriter writer = new PrintWriter(new FileWriter(FOUND_FILE))) {
            for (FoundItem item : items) {
                writer.println(item.toFileString());
            }
        } catch (IOException e) {
            System.err.println("Error saving found items: " + e.getMessage());
        }
    }

    /**
     * Realistic sample lost items for immediate classroom testing.
     */
    private static List<LostItem> getSampleLostItems() {
        List<LostItem> samples = new ArrayList<>();
        LocalDate today = LocalDate.now();

        samples.add(new LostItem(
                "L-101",
                "Blue Hydro Flask Water Bottle",
                Category.OTHER,
                "Main Library 2nd Floor",
                today.minusDays(2),
                "32oz dark blue Hydro Flask with NASA and coding stickers",
                "alex.smith@campus.edu",
                ItemStatus.OPEN,
                "Alex Smith",
                10.00
        ));

        samples.add(new LostItem(
                "L-102",
                "Apple AirPods Pro Case",
                Category.ELECTRONICS,
                "Student Center Cafeteria",
                today.minusDays(3),
                "White AirPods Pro with small green silicone case",
                "maria.g@campus.edu",
                ItemStatus.OPEN,
                "Maria Garcia",
                25.00
        ));

        samples.add(new LostItem(
                "L-103",
                "Student ID & Room Keycard",
                Category.STUDENT_ID_KEYS,
                "Science Building Room 204",
                today.minusDays(1),
                "Campus ID card for Jordan Lee with red dorm lanyard and silver key",
                "jlee@campus.edu",
                ItemStatus.OPEN,
                "Jordan Lee",
                0.00
        ));

        samples.add(new LostItem(
                "L-104",
                "Calculus Early Transcendentals Textbook",
                Category.BOOKS_STATIONERY,
                "Math Building Hallway",
                today.minusDays(4),
                "Hardcover 8th edition with blue spine and yellow sticky tabs",
                "devon.w@campus.edu",
                ItemStatus.OPEN,
                "Devon White",
                15.00
        ));

        return samples;
    }

    /**
     * Realistic sample found items for immediate classroom testing.
     */
    private static List<FoundItem> getSampleFoundItems() {
        List<FoundItem> samples = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // High match with L-101
        samples.add(new FoundItem(
                "F-201",
                "Blue Hydro Flask",
                Category.OTHER,
                "Library 2nd Floor Study Room",
                today.minusDays(1),
                "Navy blue water bottle covered in stickers including NASA sticker",
                "frontdesk@library.campus.edu",
                ItemStatus.OPEN,
                "Staff Librarian Taylor",
                "Library Circulation Desk",
                "What other sticker is on the bottle beside NASA?"
        ));

        // High match with L-102
        samples.add(new FoundItem(
                "F-202",
                "AirPods in Green Case",
                Category.ELECTRONICS,
                "Student Union Dining Area",
                today.minusDays(2),
                "Apple wireless earbuds found on booth table",
                "security@campus.edu",
                ItemStatus.OPEN,
                "Dining Worker Sam",
                "Campus Security Lost & Found Office",
                "What name appears on the Bluetooth connection?"
        ));

        // High match with L-103
        samples.add(new FoundItem(
                "F-203",
                "Campus ID Card and Key",
                Category.STUDENT_ID_KEYS,
                "Science Complex 2nd Floor",
                today.minusDays(1),
                "Dorm key attached to red campus lanyard with student card",
                "custodial@campus.edu",
                ItemStatus.OPEN,
                "Janitor Higgins",
                "Science Hall Admin Office Room 101",
                "What is the student ID number printed on the card?"
        ));

        // An unmatched found item
        samples.add(new FoundItem(
                "F-204",
                "Black North Face Umbrella",
                Category.CLOTHING_ACCESSORIES,
                "Gymnasium Entrance",
                today,
                "Compact folding umbrella left near the turnstiles",
                "gymdesk@campus.edu",
                ItemStatus.OPEN,
                "Coach Davis",
                "Rec Center Front Desk",
                "Is there a name written on the strap tag?"
        ));

        return samples;
    }
}

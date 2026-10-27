package model;

import java.time.LocalDate;

/**
 * Represents an item that was found on campus.
 * Demonstrates Inheritance from Item.
 */
public class FoundItem extends Item {
    private String finderName;
    private String storageLocation;
    private String verificationQuestion; // Question to verify legitimate owner

    public FoundItem(String id, String title, Category category, String location, LocalDate date,
                     String description, String contactEmail, ItemStatus status,
                     String finderName, String storageLocation, String verificationQuestion) {
        super(id, title, category, location, date, description, contactEmail, status);
        this.finderName = finderName;
        this.storageLocation = storageLocation;
        this.verificationQuestion = verificationQuestion;
    }

    public String getFinderName() {
        return finderName;
    }

    public void setFinderName(String finderName) {
        this.finderName = finderName;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getVerificationQuestion() {
        return verificationQuestion;
    }

    public void setVerificationQuestion(String verificationQuestion) {
        this.verificationQuestion = verificationQuestion;
    }

    @Override
    public void displayDetails() {
        System.out.println("------------------------------------------------------------");
        System.out.println(" [FOUND ITEM REPORT] ID: " + getId());
        System.out.println(" Title:        " + getTitle());
        System.out.println(" Category:     " + getCategory().getDisplayName());
        System.out.println(" Found At:     " + getLocation());
        System.out.println(" Date Found:   " + getDate());
        System.out.println(" Description:  " + getDescription());
        System.out.println(" Turned in By: " + finderName);
        System.out.println(" Stored At:    " + storageLocation);
        System.out.println(" Contact:      " + getContactEmail());
        if (verificationQuestion != null && !verificationQuestion.trim().isEmpty()) {
            System.out.println(" Claim Check:  " + verificationQuestion);
        }
        System.out.println(" Status:       " + getStatus());
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Converts to pipe-delimited format for text file storage.
     */
    public String toFileString() {
        // id|title|category|location|date|description|contactEmail|status|finderName|storageLocation|verificationQuestion
        return String.join("|",
                getId(),
                getTitle().replace("|", " "),
                getCategory().name(),
                getLocation().replace("|", " "),
                getDate().toString(),
                getDescription().replace("|", " "),
                getContactEmail().replace("|", " "),
                getStatus().name(),
                finderName.replace("|", " "),
                storageLocation.replace("|", " "),
                verificationQuestion.replace("|", " ")
        );
    }

    /**
     * Parses a pipe-delimited string back into a FoundItem.
     */
    public static FoundItem fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 11) return null;
        try {
            String id = parts[0];
            String title = parts[1];
            Category cat = Category.valueOf(parts[2]);
            String loc = parts[3];
            LocalDate date = LocalDate.parse(parts[4]);
            String desc = parts[5];
            String email = parts[6];
            ItemStatus status = ItemStatus.valueOf(parts[7]);
            String finder = parts[8];
            String storage = parts[9];
            String verifyQ = parts[10];
            return new FoundItem(id, title, cat, loc, date, desc, email, status, finder, storage, verifyQ);
        } catch (Exception e) {
            return null;
        }
    }
}

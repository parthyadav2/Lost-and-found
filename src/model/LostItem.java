package model;

import java.time.LocalDate;

/**
 * Represents an item that was lost by a student/staff.
 * Demonstrates Inheritance from Item.
 */
public class LostItem extends Item {
    private String ownerName;
    private double reward;

    public LostItem(String id, String title, Category category, String location, LocalDate date,
                    String description, String contactEmail, ItemStatus status,
                    String ownerName, double reward) {
        super(id, title, category, location, date, description, contactEmail, status);
        this.ownerName = ownerName;
        this.reward = reward;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    @Override
    public void displayDetails() {
        System.out.println("------------------------------------------------------------");
        System.out.println(" [LOST ITEM REPORT] ID: " + getId());
        System.out.println(" Title:        " + getTitle());
        System.out.println(" Category:     " + getCategory().getDisplayName());
        System.out.println(" Lost At:      " + getLocation());
        System.out.println(" Date Lost:    " + getDate());
        System.out.println(" Description:  " + getDescription());
        System.out.println(" Owner Name:   " + ownerName);
        System.out.println(" Contact:      " + getContactEmail());
        if (reward > 0) {
            System.out.printf(" Reward:       $%.2f\n", reward);
        }
        System.out.println(" Status:       " + getStatus());
        System.out.println("------------------------------------------------------------");
    }

    /**
     * Converts to pipe-delimited format for text file storage.
     */
    public String toFileString() {
        // id|title|category|location|date|description|contactEmail|status|ownerName|reward
        return String.join("|",
                getId(),
                getTitle().replace("|", " "),
                getCategory().name(),
                getLocation().replace("|", " "),
                getDate().toString(),
                getDescription().replace("|", " "),
                getContactEmail().replace("|", " "),
                getStatus().name(),
                ownerName.replace("|", " "),
                String.valueOf(reward)
        );
    }

    /**
     * Parses a pipe-delimited string back into a LostItem.
     */
    public static LostItem fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 10) return null;
        try {
            String id = parts[0];
            String title = parts[1];
            Category cat = Category.valueOf(parts[2]);
            String loc = parts[3];
            LocalDate date = LocalDate.parse(parts[4]);
            String desc = parts[5];
            String email = parts[6];
            ItemStatus status = ItemStatus.valueOf(parts[7]);
            String owner = parts[8];
            double reward = Double.parseDouble(parts[9]);
            return new LostItem(id, title, cat, loc, date, desc, email, status, owner, reward);
        } catch (Exception e) {
            return null;
        }
    }
}

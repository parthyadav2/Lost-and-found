package model;

import java.time.LocalDate;

/**
 * Abstract base class representing a general campus item.
 * Demonstrates OOP principles: Encapsulation and Abstraction.
 */
public abstract class Item {
    private String id;
    private String title;
    private Category category;
    private String location;
    private LocalDate date;
    private String description;
    private String contactEmail;
    private ItemStatus status;

    public Item(String id, String title, Category category, String location, LocalDate date, String description, String contactEmail, ItemStatus status) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.location = location;
        this.date = date;
        this.description = description;
        this.contactEmail = contactEmail;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    /**
     * Abstract method to display formatted details of an item.
     * Demonstrates Polymorphism in subclasses.
     */
    public abstract void displayDetails();

    /**
     * Returns a simple one-line summary for listing.
     */
    public String getSummaryLine() {
        return String.format("[%s] %-20s | Cat: %-18s | Loc: %-16s | Date: %s | Status: %s",
                id,
                truncate(title, 20),
                category.getDisplayName(),
                truncate(location, 16),
                date.toString(),
                status.name());
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        if (text.length() <= maxLen) return text;
        return text.substring(0, maxLen - 3) + "...";
    }
}

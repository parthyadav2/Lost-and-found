package model;

/**
 * Enumeration representing categories of lost and found items.
 */
public enum Category {
    ELECTRONICS,
    STUDENT_ID_KEYS,
    BAGS_WALLETS,
    BOOKS_STATIONERY,
    CLOTHING_ACCESSORIES,
    OTHER;

    /**
     * Prints a user-friendly list of categories with numbering.
     */
    public static void displayOptions() {
        Category[] categories = values();
        for (int i = 0; i < categories.length; i++) {
            System.out.printf("   [%d] %s\n", (i + 1), categories[i].getDisplayName());
        }
    }

    /**
     * Converts a menu number (1-based) to Category.
     */
    public static Category fromChoice(int choice) {
        Category[] categories = values();
        if (choice >= 1 && choice <= categories.length) {
            return categories[choice - 1];
        }
        return OTHER;
    }

    public String getDisplayName() {
        return name().replace('_', ' ');
    }
}

package model;

import java.util.List;

/**
 * Encapsulates the result of comparing a LostItem with a FoundItem.
 */
public class MatchResult implements Comparable<MatchResult> {
    private LostItem lostItem;
    private FoundItem foundItem;
    private int matchScore; // 0 to 100 percentage
    private List<String> matchReasons;

    public MatchResult(LostItem lostItem, FoundItem foundItem, int matchScore, List<String> matchReasons) {
        this.lostItem = lostItem;
        this.foundItem = foundItem;
        this.matchScore = matchScore;
        this.matchReasons = matchReasons;
    }

    public LostItem getLostItem() {
        return lostItem;
    }

    public FoundItem getFoundItem() {
        return foundItem;
    }

    public int getMatchScore() {
        return matchScore;
    }

    public List<String> getMatchReasons() {
        return matchReasons;
    }

    /**
     * Orders matches by highest score first.
     */
    @Override
    public int compareTo(MatchResult other) {
        return Integer.compare(other.matchScore, this.matchScore);
    }

    public void displaySummary() {
        System.out.println("----------------------------------------------------------------------");
        System.out.printf(" >> MATCH CONFIDENCE: %d%% <<\n", matchScore);
        System.out.printf("    Lost:  [%s] %s (Lost by: %s, at %s on %s)\n",
                lostItem.getId(), lostItem.getTitle(), lostItem.getOwnerName(), lostItem.getLocation(), lostItem.getDate());
        System.out.printf("    Found: [%s] %s (Found at: %s on %s, Stored at: %s)\n",
                foundItem.getId(), foundItem.getTitle(), foundItem.getLocation(), foundItem.getDate(), foundItem.getStorageLocation());
        System.out.print("    Matching Factors: ");
        System.out.println(String.join(" | ", matchReasons));
        System.out.println("----------------------------------------------------------------------");
    }
}

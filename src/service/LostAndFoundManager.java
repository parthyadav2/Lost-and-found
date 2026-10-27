package service;

import model.*;
import util.FileHandler;

import java.time.LocalDate;
import java.util.*;

/**
 * Controller service managing lost and found inventories, matching, and searches.
 */
public class LostAndFoundManager {
    private List<LostItem> lostItems;
    private List<FoundItem> foundItems;
    private MatchingEngine matchingEngine;

    public LostAndFoundManager() {
        this.matchingEngine = new MatchingEngine();
        this.lostItems = FileHandler.loadLostItems();
        this.foundItems = FileHandler.loadFoundItems();
    }

    public List<LostItem> getLostItems() {
        return lostItems;
    }

    public List<FoundItem> getFoundItems() {
        return foundItems;
    }

    /**
     * Generates a unique sequential ID for a new lost item.
     */
    public String generateNextLostId() {
        int max = 100;
        for (LostItem item : lostItems) {
            try {
                if (item.getId().startsWith("L-")) {
                    int val = Integer.parseInt(item.getId().substring(2));
                    if (val > max) max = val;
                }
            } catch (NumberFormatException ignored) {}
        }
        return "L-" + (max + 1);
    }

    /**
     * Generates a unique sequential ID for a new found item.
     */
    public String generateNextFoundId() {
        int max = 200;
        for (FoundItem item : foundItems) {
            try {
                if (item.getId().startsWith("F-")) {
                    int val = Integer.parseInt(item.getId().substring(2));
                    if (val > max) max = val;
                }
            } catch (NumberFormatException ignored) {}
        }
        return "F-" + (max + 1);
    }

    public void addLostItem(LostItem item) {
        lostItems.add(item);
        FileHandler.saveLostItems(lostItems);
    }

    public void addFoundItem(FoundItem item) {
        foundItems.add(item);
        FileHandler.saveFoundItems(foundItems);
    }

    /**
     * Finds matching found items for a given lost item.
     */
    public List<MatchResult> findMatchesForLost(LostItem lost) {
        List<MatchResult> results = new ArrayList<>();
        for (FoundItem found : foundItems) {
            MatchResult res = matchingEngine.evaluateMatch(lost, found, 40);
            if (res != null) {
                results.add(res);
            }
        }
        Collections.sort(results);
        return results;
    }

    /**
     * Finds matching lost items for a given found item.
     */
    public List<MatchResult> findMatchesForFound(FoundItem found) {
        List<MatchResult> results = new ArrayList<>();
        for (LostItem lost : lostItems) {
            MatchResult res = matchingEngine.evaluateMatch(lost, found, 40);
            if (res != null) {
                results.add(res);
            }
        }
        Collections.sort(results);
        return results;
    }

    /**
     * Scans the whole campus database and returns all candidate matches.
     */
    public List<MatchResult> findAllSystemMatches() {
        List<MatchResult> results = new ArrayList<>();
        for (LostItem lost : lostItems) {
            if (lost.getStatus() == ItemStatus.OPEN) {
                for (FoundItem found : foundItems) {
                    if (found.getStatus() == ItemStatus.OPEN) {
                        MatchResult res = matchingEngine.evaluateMatch(lost, found, 45);
                        if (res != null) {
                            results.add(res);
                        }
                    }
                }
            }
        }
        Collections.sort(results);
        return results;
    }

    /**
     * Searches across all items by keyword.
     */
    public List<Item> searchByKeyword(String keyword) {
        List<Item> matches = new ArrayList<>();
        String query = keyword.toLowerCase().trim();

        for (LostItem item : lostItems) {
            if (matchesKeyword(item, query)) {
                matches.add(item);
            }
        }

        for (FoundItem item : foundItems) {
            if (matchesKeyword(item, query)) {
                matches.add(item);
            }
        }

        return matches;
    }

    private boolean matchesKeyword(Item item, String query) {
        return item.getId().toLowerCase().contains(query)
                || item.getTitle().toLowerCase().contains(query)
                || item.getDescription().toLowerCase().contains(query)
                || item.getLocation().toLowerCase().contains(query)
                || item.getCategory().getDisplayName().toLowerCase().contains(query);
    }

    /**
     * Finds an item by ID (either Lost or Found).
     */
    public Item findById(String id) {
        for (LostItem item : lostItems) {
            if (item.getId().equalsIgnoreCase(id)) return item;
        }
        for (FoundItem item : foundItems) {
            if (item.getId().equalsIgnoreCase(id)) return item;
        }
        return null;
    }

    /**
     * Resolves an item and marks it CLAIMED.
     */
    public boolean markAsClaimed(String id) {
        Item item = findById(id);
        if (item != null) {
            item.setStatus(ItemStatus.CLAIMED);
            FileHandler.saveLostItems(lostItems);
            FileHandler.saveFoundItems(foundItems);
            return true;
        }
        return false;
    }

    public void persistAll() {
        FileHandler.saveLostItems(lostItems);
        FileHandler.saveFoundItems(foundItems);
    }
}

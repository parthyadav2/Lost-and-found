package service;

import model.Category;
import model.FoundItem;
import model.ItemStatus;
import model.LostItem;
import model.MatchResult;

import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Matching engine to compute similarity between Lost and Found items.
 * Uses category matching, keyword extraction, location overlap, and date proximity.
 */
public class MatchingEngine {

    // Common words to ignore when comparing text descriptions
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
            "of", "with", "by", "from", "my", "i", "it", "lost", "found", "is",
            "was", "has", "have", "some", "this", "that", "near", "around"
    ));

    /**
     * Evaluates compatibility between one LostItem and one FoundItem.
     * Returns MatchResult if score is >= minThreshold, else null.
     */
    public MatchResult evaluateMatch(LostItem lost, FoundItem found, int minThreshold) {
        // Both items must still be OPEN
        if (lost.getStatus() != ItemStatus.OPEN || found.getStatus() != ItemStatus.OPEN) {
            return null;
        }

        int score = 0;
        List<String> reasons = new ArrayList<>();

        // 1. Category Matching (Highest Weight: 40 points)
        if (lost.getCategory() == found.getCategory()) {
            score += 40;
            reasons.add("Exact Category Match (" + lost.getCategory().getDisplayName() + ")");
        } else if (lost.getCategory() == Category.OTHER || found.getCategory() == Category.OTHER) {
            score += 10;
            reasons.add("Partial Category Fit (General/Other)");
        }

        // 2. Keyword Matching on Title and Description (Up to 35 points)
        Set<String> lostWords = extractKeywords(lost.getTitle() + " " + lost.getDescription());
        Set<String> foundWords = extractKeywords(found.getTitle() + " " + found.getDescription());

        Set<String> matchingWords = new HashSet<>(lostWords);
        matchingWords.retainAll(foundWords); // Intersection of keywords

        if (!matchingWords.isEmpty()) {
            int keywordPoints = Math.min(35, matchingWords.size() * 12);
            score += keywordPoints;
            reasons.add("Shared Keywords (" + String.join(", ", matchingWords) + ")");
        }

        // 3. Location Proximity (Up to 20 points)
        String lostLoc = lost.getLocation().toLowerCase().trim();
        String foundLoc = found.getLocation().toLowerCase().trim();

        if (lostLoc.equals(foundLoc)) {
            score += 20;
            reasons.add("Identical Location (" + lost.getLocation() + ")");
        } else if (lostLoc.contains(foundLoc) || foundLoc.contains(lostLoc)) {
            score += 15;
            reasons.add("Nearby Location Area (" + lost.getLocation() + " / " + found.getLocation() + ")");
        } else {
            Set<String> lostLocWords = extractKeywords(lostLoc);
            Set<String> foundLocWords = extractKeywords(foundLoc);
            lostLocWords.retainAll(foundLocWords);
            if (!lostLocWords.isEmpty()) {
                score += 10;
                reasons.add("Common Location Keyword: " + String.join(", ", lostLocWords));
            }
        }

        // 4. Date Proximity (Up to 10 points)
        if (found.getDate() != null && lost.getDate() != null) {
            long daysBetween = ChronoUnit.DAYS.between(lost.getDate(), found.getDate());
            // Found item usually reported on the same day or within 14 days after being lost
            if (daysBetween >= 0 && daysBetween <= 14) {
                score += 10;
                reasons.add("Consistent Timeline (Found " + daysBetween + " day(s) after report)");
            } else if (daysBetween == -1) {
                // Same day timezone or minor discrepancy
                score += 5;
                reasons.add("Close Date Proximity");
            }
        }

        // Cap score at 100
        score = Math.min(100, score);

        if (score >= minThreshold) {
            return new MatchResult(lost, found, score, reasons);
        }

        return null;
    }

    /**
     * Splits text into lowercase alphanumeric keywords, filtering stopwords.
     */
    private Set<String> extractKeywords(String text) {
        Set<String> keywords = new HashSet<>();
        if (text == null) return keywords;

        String[] tokens = text.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", " ").split("\\s+");
        for (String token : tokens) {
            token = token.trim();
            if (token.length() > 2 && !STOP_WORDS.contains(token)) {
                keywords.add(token);
            }
        }
        return keywords;
    }
}

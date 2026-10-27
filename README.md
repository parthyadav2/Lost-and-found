# Campus Lost and Found Matching System (Java)

A console-based Java application designed for academic coursework demonstrating Core Object-Oriented Programming (OOP) concepts without complex external dependencies or backend frameworks.

---

##  Project Overview
This project solves the everyday campus problem of misplaced belongings (student IDs, laptops, water bottles, keys, books) by connecting students who lost items with campus security or finders.

### Key Highlights
- **100% Pure Core Java**: Uses only standard Java SE libraries (`java.util`, `java.io`, `java.time`).
- **Smart Matching Engine**: Automatically calculates a similarity score (0–100%) between lost and found reports using category matching, keyword extraction, location overlap, and date proximity.
- **Immediate Alerting**: Notifies the user immediately if a newly reported item matches an existing item.
- **File Persistence**: Reads and writes plain-text records (`data/lost_items.txt` and `data/found_items.txt`), with pre-loaded mock campus data on initial launch.
- **Robust CLI**: Complete input validation guarding against non-number inputs or bad date formats.

---

##  Project Structure

```
campus-lost-and-found/
├── compile.bat                  # One-click Windows compile script
├── run.bat                      # One-click Windows launch script
├── README.md                    # Project documentation
├── data/                        # Text-based file storage
│   ├── lost_items.txt
│   └── found_items.txt
└── src/
    ├── Main.java                # Interactive console UI & menu
    ├── model/                   # OOP Data models
    │   ├── Item.java            # Abstract base class
    │   ├── LostItem.java        # Subclass for lost belongings
    │   ├── FoundItem.java       # Subclass for found belongings
    │   ├── Category.java        # Enum for item classifications
    │   ├── ItemStatus.java      # Enum for item status (OPEN, CLAIMED)
    │   └── MatchResult.java     # Data carrier for match pairings & scores
    ├── service/                 # Business logic
    │   ├── MatchingEngine.java  # Scoring & similarity algorithm
    │   └── LostAndFoundManager.java # Inventory controller & operations
    └── util/
        └── FileHandler.java     # Simple BufferedReader/PrintWriter I/O
```

---

##  Object-Oriented Programming (OOP) Concepts Demonstrated

When submitting or presenting this project for your Java class, you can highlight:

1. **Encapsulation**:
   - All class fields (`id`, `title`, `description`, etc.) are declared `private`.
   - Access and modifications are controlled through public getters and setters.

2. **Inheritance**:
   - `Item` is an abstract base class containing common fields shared by all belongings.
   - `LostItem` extends `Item` and introduces owner details and optional reward amount.
   - `FoundItem` extends `Item` and introduces finder info, physical storage location, and verification questions.

3. **Polymorphism & Abstraction**:
   - `Item` defines an `abstract void displayDetails()` method.
   - Both `LostItem` and `FoundItem` provide their own unique implementation to print specialized details.
   - The UI handles collections of `Item` polymorphically (e.g., in search).

4. **Enums**:
   - `Category` (`ELECTRONICS`, `STUDENT_ID_KEYS`, `BAGS_WALLETS`, `BOOKS_STATIONERY`, `CLOTHING_ACCESSORIES`, `OTHER`).
   - `ItemStatus` (`OPEN`, `CLAIMED`).

5. **Collections Framework & Generics**:
   - Uses `ArrayList<LostItem>`, `ArrayList<FoundItem>`, and `HashSet<String>` for keyword comparisons.
   - Implements `Comparable<MatchResult>` to sort potential matches in descending order of confidence score.

6. **File I/O**:
   - Uses standard Java `BufferedReader`, `FileReader`, `PrintWriter`, and `FileWriter` for saving and reading text files.

---

##  How the Matching Algorithm Works

The `MatchingEngine` compares each active lost item against active found items and scores them out of 100 points:

| Factor | Weight | Details |
|---|---|---|
| **Category Match** | 40% | Exact match gives 40 pts; `OTHER` gives 10 pts |
| **Keyword Overlap** | Up to 35% | Tokenizes title & description, discards common stopwords ("the", "a", "lost", "in"), awards 12 pts per shared keyword |
| **Location Match** | Up to 20% | 20 pts for exact match; 15 pts if one contains the other; 10 pts for shared building/area keywords |
| **Date Timeline** | Up to 10% | Verifies item was found on or within 14 days after the lost date |

If total score $\ge$ 45%, it is flagged as a candidate match.

---

##  How to Compile and Run

### Option 1: Using the provided scripts (Windows)
Double-click `compile.bat` then `run.bat`, or in terminal:
```cmd
compile.bat
run.bat
```

### Option 2: Using the Java Command Line directly
From the `campus-lost-and-found` folder:

1. **Compile**:
   ```bash
   javac -d bin src/model/*.java src/service/*.java src/util/*.java src/Main.java
   ```

2. **Run**:
   ```bash
   java -cp bin Main
   ```

---

## 📋 Features in Menu
1. **Report a Lost Item**: Enters details; automatically alerts if a matching item is already in storage.
2. **Report a Found Item**: Enters details + storage office + claim verification question; alerts if someone reported it missing.
3. **View All Lost Items**: Formatted table summary of open lost items.
4. **View All Found Items**: Formatted table summary of open found items.
5. **Run Automated System Matcher**: Scans the campus database and ranks all top candidate matches.
6. **Find Potential Matches for a Specific Item**: Targeted check for any item ID.
7. **Search Items**: Keyword search across all descriptions, locations, categories, and titles.
8. **Claim / Mark Item as Resolved**: Marks items as CLAIMED when returned.
9. **View Item Details by ID**: Displays full info including reward or claim verification questions.
10. **Save & Exit**: Writes current records to disk.

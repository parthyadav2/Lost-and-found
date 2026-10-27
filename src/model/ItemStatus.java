package model;

/**
 * Status of an item in the system.
 */
public enum ItemStatus {
    OPEN,       // Still looking or unclaimed
    CLAIMED     // Successfully returned to owner
}

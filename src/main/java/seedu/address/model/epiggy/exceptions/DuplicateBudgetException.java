package seedu.address.model.epiggy.exceptions;

//@@author tehwenyi

/**
 * Signals that the operation will result in duplicate Budgets (Budgets are considered duplicates if they have the same
 * dates).
 */
public class DuplicateBudgetException extends RuntimeException {
    public DuplicateBudgetException() {
        super("Operation would result in duplicate budgets");
    }
}

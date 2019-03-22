package seedu.address.model.epiggy.item;

/**
 * Represents a Price.
 * Guarantees: immutable}
 */
public class Price {
    // Change message constraints when we change amount from int to float
    public static final String MESSAGE_CONSTRAINTS =
            "Cost should only contain whole numbers of at least value 1.";
    public static final String VALIDATION_REGEX = "\\d+";
    private final int amount;

    public Price(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * Returns true if a given string is a valid price.
     */
    public static boolean isValidPrice(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return Integer.toString(amount);
    }
}

package seedu.address.model.epiggy.item;

import static java.util.Objects.requireNonNull;

import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author tehwenyi

/**
 * Represents a time period.
 * Guarantees: immutable
 */
public class Period {
    public static final String MESSAGE_CONSTRAINTS =
            "Time period is in terms of days and should only contain whole numbers of at least value 1.\n"
                    + "Time period cannot exceed 1 million days (1,000,000)";
    public static final String VALIDATION_REGEX = "\\b([1-9]|[1-8][0-9]|9[0-9]|[1-8][0-9]{2}|9[0-8][0-9]|99[0-9]"
            + "|[1-8][0-9]{3}|9[0-8][0-9]{2}|99[0-8][0-9]|999[0-9]|[1-8][0-9]{4}|9[0-8][0-9]{3}|99[0-8][0-9]{2}"
            + "|999[0-8][0-9]|9999[0-9]|[1-8][0-9]{5}|9[0-8][0-9]{4}|99[0-8][0-9]{3}|999[0-8][0-9]{2}|9999[0-8][0-9]"
            + "|99999[0-9]|1000000)\\b";
    private final int timePeriod;

    public Period(int timePeriod) {
        checkArgument(isValidPeriod(timePeriod), MESSAGE_CONSTRAINTS);
        this.timePeriod = timePeriod;
    }

    public Period(String period) {
        requireNonNull(period);
        checkArgument(isValidPeriod(period), MESSAGE_CONSTRAINTS);
        this.timePeriod = Integer.parseInt(period);
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    /**
     * Returns true if a given string is a valid time period.
     */
    public static boolean isValidPeriod(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid time period.
     */
    public static boolean isValidPeriod(int test) {
        if (test < 0 || test > 2147483647) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Period)) {
            return false;
        }

        Period p = (Period) o;
        return this.timePeriod == p.getTimePeriod();
    }

    @Override
    public String toString() {
        return Integer.toString(timePeriod);
    }
}

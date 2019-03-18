package seedu.address.model.epiggy.item;

import java.util.HashMap;

/**
 * Represents a Date in ePiggy.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Date {

    public static final String MESSAGE_CONSTRAINTS =
            "Date should be in the format dd/mm/yyyy, dd.mm.yyyy or "
                    + "dd-mm-yyyy and year should be no earlier than 1600";
    public static final String VALIDATION_REGEX = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]"
            + "|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|"
            + "[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|"
            + "[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|"
            + "2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
    private static final HashMap<Integer, Integer> daysInMonth;
    private final int day;
    private final int month;
    private final int year;

    static {
        daysInMonth = new HashMap<Integer, Integer>();
        daysInMonth.put(1, 31);
        daysInMonth.put(2, 28);
        daysInMonth.put(3, 31);
        daysInMonth.put(4, 30);
        daysInMonth.put(5, 31);
        daysInMonth.put(6, 30);
        daysInMonth.put(7, 31);
        daysInMonth.put(8, 31);
        daysInMonth.put(9, 30);
        daysInMonth.put(10, 31);
        daysInMonth.put(11, 30);
        daysInMonth.put(12, 31);
    }

    public Date(String date) {
        this.day = Integer.valueOf(date.substring(0, 2));
        this.month = Integer.valueOf(date.substring(3, 5));
        this.year = Integer.valueOf(date.substring(6, 10));
    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    /**
     * Adds the number of days required to add to start day and calls the recursive function
     * @param addedDays
     * @return Date after adding the days
     */
    public Date addDays (int addedDays) {
        return addDays(this.day + addedDays, this.month, this.year);
    }

    /**
     * Recursive function which adds the number of days to the date
     * @param days which is the sum of days to add and start day
     * @param month of the start date
     * @param year of the start date
     * @return Date after adding the days
     */
    public Date addDays(int days, int month, int year) {
        if (days > 0 && days <= getNoOfDaysInMonth(month, year)) {
            return new Date(days, month, year);
        } else if (days > 0) {
            days = days - getNoOfDaysInMonth(month, year);
            month = month + 1;
            if (month > 12) {
                month = 1;
                year = year + 1;
            }
        } else {
            //throw exception
        }
        return addDays(days, month, year);
    }

    /**
     * Gets the number of days in the month of that year
     * @param month of the date
     * @param year of the date
     * @return Number of days in that month of that year
     */
    private int getNoOfDaysInMonth(int month, int year) {
        if (month == 2 && isLeap(year)) {
            return daysInMonth.get(month) + 1;
        }
        return daysInMonth.get(month);
    }

    /**
     * Check if given year is a leap year
     * @param year
     * @return true if year is leap year
     */
    public boolean isLeap(int year) {
        if (year % 4 == 0) {
            if (year % 100 == 0) {
                if (year % 400 == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Getter for day
     * @return day
     */
    public int getDay() {
        return this.day;
    }

    /**
     * Getter for month
     * @return month
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Getter for year
     * @return year
     */
    public int getYear() {
        return this.year;
    }

    /**
     * Check if given dd mm yy format is valid
     * @return true if it is valid date format
     */
    public static boolean isValidDate(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getDay())
                .append("/")
                .append(getMonth())
                .append("/")
                .append(getYear());
        return builder.toString();
    }
}

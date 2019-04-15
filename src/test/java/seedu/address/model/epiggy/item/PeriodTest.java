package seedu.address.model.epiggy.item;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_SECONDEXTRA;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author tehwenyi

public class PeriodTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Period(null));
    }

    @Test
    public void constructor_invalidPeriod_throwsIllegalArgumentException() {
        String invalidPeriod = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Period(invalidPeriod));
    }

    @Test
    public void constructor_validPeriod_success() {
        Period validPeriod = new Period("31");
        assertEquals(31, validPeriod.getTimePeriod());
        validPeriod = new Period(25);
        assertEquals(25, validPeriod.getTimePeriod());
    }

    @Test
    public void isValidPeriod() {
        // null period
        Assert.assertThrows(NullPointerException.class, () -> Period.isValidPeriod(null));

        // blank period
        assertFalse(Period.isValidPeriod("")); // empty string
        assertFalse(Period.isValidPeriod(" ")); // spaces only

        // invalid types
        assertFalse(Period.isValidPeriod("1.20")); // double not an integer
        assertFalse(Period.isValidPeriod("1.0")); // double should not auto parse to integer
        assertFalse(Period.isValidPeriod("-4")); // cannot be negative
        assertFalse(Period.isValidPeriod("7g")); // letters not allowed
        assertFalse(Period.isValidPeriod("a 31")); // letters
        assertFalse(Period.isValidPeriod("31 5")); // split up numbers
        assertFalse(Period.isValidPeriod("#$1")); // special characters not allowed
        assertFalse(Period.isValidPeriod("54$5^")); // special characters not allowed
        assertFalse(Period.isValidPeriod("thirteen")); // words not allowed
        assertFalse(Period.isValidPeriod("1000001")); // number too big
        assertFalse(Period.isValidPeriod(" 5")); // leading space
        assertFalse(Period.isValidPeriod("18 ")); // trailing space

        // valid period
        assertTrue(Period.isValidPeriod("1000000")); // largest number
        assertTrue(Period.isValidPeriod("1")); // smallest accepted number

        // invalid int period
        assertFalse(Period.isValidPeriod(-2)); // negative integer

        // valid int period
        assertTrue(Period.isValidPeriod(1000000)); // largest accepted integer
        assertTrue(Period.isValidPeriod(0)); // zero
    }

    @Test
    public void equals() {
        // same values -> returns true
        Period one = new Period(VALID_PERIOD_FIRSTEXTRA);
        Period two = new Period(VALID_PERIOD_SECONDEXTRA);
        Period oneCopy = new Period(VALID_PERIOD_FIRSTEXTRA);
        assertTrue(one.equals(oneCopy));

        // same object -> returns true
        assertTrue(one.equals(one));

        // null -> returns false
        assertFalse(one.equals(null));

        // different type -> returns false
        assertFalse(one.equals(5));

        // different budget -> returns false
        assertFalse(one.equals(two));
    }

    @Test
    public void toStringTest() {
        Period one = new Period(VALID_PERIOD_FIRSTEXTRA);

        // same string -> return true
        assertTrue(one.toString().equals(VALID_PERIOD_FIRSTEXTRA));

        // different strings -> return false
        assertFalse(one.toString().equals(VALID_PERIOD_SECONDEXTRA));
    }
}

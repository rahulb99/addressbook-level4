package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysPerson;

import org.junit.Test;

import guitests.guihandles.PersonCardHandle;
import seedu.address.model.person.Expense;
import seedu.address.testutil.PersonBuilder;

public class ExpenseCardTest extends GuiUnitTest {

    @Test
    public void display() {
        // no tags
        Expense expenseWithNoTags = new PersonBuilder().withTags(new String[0]).build();
        PersonCard personCard = new PersonCard(expenseWithNoTags, 1);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, expenseWithNoTags, 1);

        // with tags
        Expense expenseWithTags = new PersonBuilder().build();
        personCard = new PersonCard(expenseWithTags, 2);
        uiPartRule.setUiPart(personCard);
        assertCardDisplay(personCard, expenseWithTags, 2);
    }

    @Test
    public void equals() {
        Expense expense = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(expense, 0);

        // same expense, same index -> returns true
        PersonCard copy = new PersonCard(expense, 0);
        assertTrue(personCard.equals(copy));

        // same object -> returns true
        assertTrue(personCard.equals(personCard));

        // null -> returns false
        assertFalse(personCard.equals(null));

        // different types -> returns false
        assertFalse(personCard.equals(0));

        // different expense, same index -> returns false
        Expense differentExpense = new PersonBuilder().withName("differentName").build();
        assertFalse(personCard.equals(new PersonCard(differentExpense, 0)));

        // same expense, different index -> returns false
        assertFalse(personCard.equals(new PersonCard(expense, 1)));
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedExpense} correctly and matches
     * {@code expectedId}.
     */
    private void assertCardDisplay(PersonCard personCard, Expense expectedExpense, int expectedId) {
        guiRobot.pauseForHuman();

        PersonCardHandle personCardHandle = new PersonCardHandle(personCard.getRoot());

        // verify id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", personCardHandle.getId());

        // verify expense details are displayed correctly
        assertCardDisplaysPerson(expectedExpense, personCardHandle);
    }
}

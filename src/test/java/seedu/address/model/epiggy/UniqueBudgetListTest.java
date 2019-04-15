package seedu.address.model.epiggy;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_FIRSTEXTRA;
import static seedu.address.testutil.TypicalBudgets.FIRST_EXTRA;
import static seedu.address.testutil.TypicalBudgets.ONE;
import static seedu.address.testutil.TypicalBudgets.SECOND_EXTRA;
import static seedu.address.testutil.TypicalBudgets.TWO;
import static seedu.address.testutil.TypicalBudgets.getMaximumNumberOfBudgets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.epiggy.exceptions.DuplicateBudgetException;
import seedu.address.testutil.epiggy.BudgetBuilder;

//@@author tehwenyi

public class UniqueBudgetListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueBudgetList uniqueBudgetList = new UniqueBudgetList();

    @Test
    public void addAtIndex_nullBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueBudgetList.addAtIndex(0, null);
    }

    @Test
    public void addAtIndex_indexTooBig_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.addAtIndex(UniqueBudgetList.MAXIMUM_SIZE + 1, ONE);
    }

    @Test
    public void addAtIndex_negativeIndex_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.addAtIndex(-1, ONE);
    }

    @Test
    public void replaceAtIndex_nullBudget_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueBudgetList.replaceAtIndex(0, null);
    }

    @Test
    public void replaceAtIndex_indexTooBig_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.replaceAtIndex(UniqueBudgetList.MAXIMUM_SIZE, ONE);
    }

    @Test
    public void replaceAtIndex_negativeIndex_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.replaceAtIndex(-1, ONE);
    }

    @Test
    public void replaceAtIndex_editedBudgetIsSameBudget_success() {
        uniqueBudgetList.addAtIndex(0, ONE);
        uniqueBudgetList.replaceAtIndex(0, ONE);
        UniqueBudgetList expectedUniqueBudgetList = new UniqueBudgetList();
        expectedUniqueBudgetList.addAtIndex(0, ONE);
        assertEquals(uniqueBudgetList, expectedUniqueBudgetList);
    }

    @Test
    public void replaceAtIndex_editedBudgetHasSameIdentity_success() {
        uniqueBudgetList.addAtIndex(0, FIRST_EXTRA);
        Budget editedOne = new BudgetBuilder().withAmount(VALID_AMOUNT_FIRSTEXTRA).withPeriod(VALID_PERIOD_FIRSTEXTRA)
                .withDate(VALID_DATE_FIRSTEXTRA).build();
        uniqueBudgetList.replaceAtIndex(0, editedOne);
        UniqueBudgetList expectedUniqueBudgetList = new UniqueBudgetList();
        expectedUniqueBudgetList.addAtIndex(0, editedOne);
        assertEquals(uniqueBudgetList, expectedUniqueBudgetList);
    }

    @Test
    public void replaceAtIndex_editedBudgetHasDifferentIdentity_success() {
        uniqueBudgetList.addAtIndex(0, ONE);
        uniqueBudgetList.replaceAtIndex(0, TWO);
        UniqueBudgetList expectedUniqueBudgetList = new UniqueBudgetList();
        expectedUniqueBudgetList.addAtIndex(0, TWO);
        assertEquals(uniqueBudgetList, expectedUniqueBudgetList);
    }

    @Test
    public void getBudgetAtIndex_emptyUniqueBudgetList_throwsNullPointerException() {
        UniqueBudgetList emptyUniqueBudgetList = new UniqueBudgetList();
        thrown.expect(IndexOutOfBoundsException.class);
        emptyUniqueBudgetList.getBudgetAtIndex(0);
    }

    @Test
    public void getBudgetAtIndex_indexTooBig_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.getBudgetAtIndex(uniqueBudgetList.getBudgetListSize());
    }

    @Test
    public void getBudgetAtIndex_negativeIndex_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.getBudgetAtIndex(-1);
    }

    @Test
    public void getBudgetAtIndex_success() {
        uniqueBudgetList.addAtIndex(0, ONE);
        uniqueBudgetList.addAtIndex(1, TWO);
        assertEquals(ONE, uniqueBudgetList.getBudgetAtIndex(0));
        assertEquals(TWO, uniqueBudgetList.getBudgetAtIndex(1));
    }

    @Test
    public void getBudgetIndexBasedOnDate_emptyUniqueBudgetList_success() {
        UniqueBudgetList emptyUniqueBudgetList = new UniqueBudgetList();
        assertEquals(-1, emptyUniqueBudgetList.getBudgetIndexBasedOnDate(new Date()));
    }

    @Test
    public void getBudgetIndexBasedOnDate_nullDate_throwsNullPointerException() {
        uniqueBudgetList.addAtIndex(0, ONE);
        thrown.expect(NullPointerException.class);
        uniqueBudgetList.getBudgetIndexBasedOnDate(null);
    }

    @Test
    public void getBudgetIndexBasedOnDate_budgetNotFound_success() {
        uniqueBudgetList.addAtIndex(0, ONE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            date = sdf.parse("01/01/2000");
        } catch (ParseException e) {
            System.out.println("Date should be in the format dd/mm/yyyy.");
        }
        assertEquals(-1, uniqueBudgetList.getBudgetIndexBasedOnDate(date));
    }

    @Test
    public void getBudgetIndexBasedOnDate_budgetSuccess_success() {
        uniqueBudgetList.addAtIndex(0, FIRST_EXTRA);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(VALID_DATE_FIRSTEXTRA);
        } catch (ParseException e) {
            System.out.println("Date should be in the format dd/mm/yyyy.");
        }
        assertEquals(0, uniqueBudgetList.getBudgetIndexBasedOnDate(date));
    }

    @Test
    public void getCurrentBudgetIndex_emptyUniqueBudgetList_success() {
        UniqueBudgetList emptyUniqueBudgetList = new UniqueBudgetList();
        assertEquals(-1, uniqueBudgetList.getCurrentBudgetIndex());
    }

    @Test
    public void getCurrentBudgetIndex_budgetDoesNotExist_success() {
        uniqueBudgetList.addAtIndex(0, FIRST_EXTRA);
        assertEquals(-1, uniqueBudgetList.getCurrentBudgetIndex());
    }

    @Test
    public void getCurrentBudgetIndex_success() {
        Budget currentBudget = new BudgetBuilder().withDate(new Date()).build();
        uniqueBudgetList.addAtIndex(0, currentBudget);
        assertEquals(0, uniqueBudgetList.getCurrentBudgetIndex());
    }

    @Test
    public void getBudgetListSize_emptyUniqueBudgetList_success() {
        UniqueBudgetList emptyUniqueBudgetList = new UniqueBudgetList();
        assertEquals(0, emptyUniqueBudgetList.getBudgetListSize());
    }

    @Test
    public void getBudgetListSize_success() {
        uniqueBudgetList.addAtIndex(0, FIRST_EXTRA);
        uniqueBudgetList.addAtIndex(1, SECOND_EXTRA);
        assertEquals(2, uniqueBudgetList.getBudgetListSize());
    }

    @Test
    public void addBudgetList_replacesOwnListWithProvidedBudgetList_success() {
        uniqueBudgetList.addAtIndex(0, ONE);
        List<Budget> expectedBudgetList = new ArrayList<Budget>();
        expectedBudgetList.add(0, TWO);
        UniqueBudgetList expectedUniqueBudgetList = new UniqueBudgetList();
        expectedUniqueBudgetList.addAtIndex(0, TWO);
        uniqueBudgetList.addBudgetList(expectedBudgetList);
        assertEquals(expectedUniqueBudgetList, uniqueBudgetList);
    }

    @Test
    public void addBudgetList_replacesOwnListWithDuplicateBudgetList_throwsDuplicateBudgetException() {
        uniqueBudgetList.addAtIndex(0, ONE);
        List<Budget> expectedBudgetList = new ArrayList<Budget>();
        expectedBudgetList.add(0, ONE);
        expectedBudgetList.add(1, ONE);
        thrown.expect(DuplicateBudgetException.class);
        uniqueBudgetList.addBudgetList(expectedBudgetList);
    }

    @Test
    public void addBudgetList_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueBudgetList.addBudgetList((List<Budget>) null);
    }

    @Test
    public void remove_indexTooBig_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.remove(uniqueBudgetList.getBudgetListSize());
    }

    @Test
    public void remove_negativeIndex_throwsIndexOutOfBoundsException() {
        thrown.expect(IndexOutOfBoundsException.class);
        uniqueBudgetList.remove(-1);
    }

    @Test
    public void remove_existingBudget_success() {
        uniqueBudgetList.addAtIndex(0, ONE);
        uniqueBudgetList.remove(0);
        UniqueBudgetList expectedUniqueBudgetList = new UniqueBudgetList();
        assertEquals(expectedUniqueBudgetList, uniqueBudgetList);
    }

    @Test
    public void limitSize_exceedingBudgetListSize_success() {
        List<Budget> budgetList = getMaximumNumberOfBudgets();
        budgetList.add(FIRST_EXTRA);
        uniqueBudgetList.addBudgetList(budgetList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueBudgetList.asUnmodifiableObservableList().remove(0);
    }

    @Test
    public void equals() {
        // same object -> returns true
        uniqueBudgetList.addAtIndex(0, FIRST_EXTRA);
        uniqueBudgetList.addAtIndex(1, SECOND_EXTRA);
        assertTrue(uniqueBudgetList.equals(uniqueBudgetList));

        // same internal list -> returns true
        UniqueBudgetList copyOfUniqueBudgetList = uniqueBudgetList;
        assertTrue(copyOfUniqueBudgetList.equals(uniqueBudgetList));

        // different order -> returns false
        UniqueBudgetList differentUniqueBudgetList = new UniqueBudgetList();
        differentUniqueBudgetList.addAtIndex(0, SECOND_EXTRA);
        differentUniqueBudgetList.addAtIndex(1, FIRST_EXTRA);
        assertFalse(differentUniqueBudgetList.equals(uniqueBudgetList));

        // different budgets -> returns false
        differentUniqueBudgetList.remove(1);
        assertFalse(differentUniqueBudgetList.equals(uniqueBudgetList));
    }
}

package seedu.address.model.epiggy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_SECONDEXTRA;
import static seedu.address.testutil.TypicalBudgets.FIRST_EXTRA;
import static seedu.address.testutil.TypicalBudgets.SECOND_EXTRA;

import java.util.Calendar;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Period;
import seedu.address.testutil.Assert;
import seedu.address.testutil.epiggy.BudgetBuilder;

//@@author tehwenyi

public class BudgetTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullParameters_throwsNullPointerException() {
        Cost nullCost = null;
        Period nullPeriod = null;
        Date nullDate = null;
        Assert.assertThrows(NullPointerException.class, () -> new Budget(nullCost, nullPeriod, nullDate));
    }

    @Test
    public void constructor_createCurrentBudget_success() {
        Date todaysDate = new Date();
        Budget validBudget = new BudgetBuilder().withDate(todaysDate).build();
        Calendar c = Calendar.getInstance();
        c.setTime(todaysDate);
        c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(BudgetBuilder.DEFAULT_PERIOD));
        Date expectedEndDate = c.getTime();
        assertEquals(Double.parseDouble(BudgetBuilder.DEFAULT_AMOUNT), validBudget.getBudgetedAmount().getAmount());
        assertEquals(Integer.parseInt(BudgetBuilder.DEFAULT_PERIOD), validBudget.getPeriod().getTimePeriod());
        assertEquals(todaysDate, validBudget.getStartDate());
        assertEquals(expectedEndDate, validBudget.getEndDate());
        assertEquals(Double.parseDouble(BudgetBuilder.DEFAULT_AMOUNT), validBudget.getRemainingAmount().getAmount());
        assertEquals(Integer.parseInt(BudgetBuilder.DEFAULT_PERIOD), validBudget.getRemainingDays().getTimePeriod());
        assertEquals(Budget.CURRENT_BUDGET, validBudget.getStatus());
    }

    @Test
    public void setRemainingDays_success() {
        Budget validBudget = new BudgetBuilder().build();
        validBudget.setRemainingDays(new Period(2));
        assertEquals(2, validBudget.getRemainingDays().getTimePeriod());
    }

    @Test
    public void setRemainingAmount_success() {
        Budget validBudget = new BudgetBuilder().build();
        validBudget.setRemainingAmount(new Cost(100));
        assertEquals(100, validBudget.getRemainingAmount().getAmount());
    }

    @Test
    public void resetRemainingAmount_success() {
        Budget validBudget = new BudgetBuilder().build();
        validBudget.resetRemainingAmount();
        assertEquals(Integer.parseInt(BudgetBuilder.DEFAULT_AMOUNT), validBudget.getRemainingAmount().getAmount());
    }

    @Test
    public void deductRemainingAmount() {
        Budget budget = new BudgetBuilder().build();
        // deduct positive cost
        Cost positiveCost = new Cost(10);
        budget.deductRemainingAmount(positiveCost);
        Cost resultingCost = new Cost(BudgetBuilder.DEFAULT_AMOUNT).deduct(positiveCost);
        assertEquals(resultingCost.getAmount(), budget.getRemainingAmount().getAmount());

        // deduct negative cost
        Cost negativeCost = new Cost(-10);
        budget = new BudgetBuilder().build();
        resultingCost = new Cost(BudgetBuilder.DEFAULT_AMOUNT).deduct(negativeCost);
        budget.deductRemainingAmount(negativeCost);
        assertEquals(resultingCost.getAmount(), budget.getRemainingAmount().getAmount());
    }

    @Test
    public void getPositiveRemainingAmount() {
        Budget positiveBudget = new BudgetBuilder().withAmount("100").build();
        assertEquals(100, positiveBudget.getPositiveRemainingAmount().getAmount());

        Budget negativeBudget = new BudgetBuilder().withAmount("100").build();
        negativeBudget.deductRemainingAmount(new Cost(200.00));
        assertEquals(100, negativeBudget.getPositiveRemainingAmount().getAmount());
    }

    @Test
    public void equals() {
        // same values -> returns true
        Budget oneCopy = new BudgetBuilder().withAmount(VALID_AMOUNT_FIRSTEXTRA).withPeriod(VALID_PERIOD_FIRSTEXTRA)
                .withDate(VALID_DATE_FIRSTEXTRA).build();
        assertEquals(FIRST_EXTRA, oneCopy);

        // same object -> returns true
        assertTrue(FIRST_EXTRA.equals(FIRST_EXTRA));

        // null -> returns false
        assertFalse(FIRST_EXTRA.equals(null));

        // different type -> returns false
        assertFalse(FIRST_EXTRA.equals(5));

        // different budget -> returns false
        assertFalse(FIRST_EXTRA.equals(SECOND_EXTRA));

        // different name -> returns false
        Budget editedOne = new BudgetBuilder(FIRST_EXTRA).withAmount(VALID_AMOUNT_SECONDEXTRA).build();
        assertFalse(FIRST_EXTRA.equals(editedOne));

        // different phone -> returns false
        editedOne = new BudgetBuilder(FIRST_EXTRA).withPeriod(VALID_PERIOD_SECONDEXTRA).build();
        assertFalse(FIRST_EXTRA.equals(editedOne));

        // different email -> returns false
        editedOne = new BudgetBuilder(FIRST_EXTRA).withDate(VALID_DATE_SECONDEXTRA).build();
        assertFalse(FIRST_EXTRA.equals(editedOne));
    }

    @Test
    public void toStringTest() {
        // same values -> returns true
        Budget oneCopy = new BudgetBuilder(FIRST_EXTRA).build();
        String oneString = "$100 from 03 Mar 2019 till 09 Mar 2019.\n"
                + "0 days remaining and $100 remaining.";

        // same budget
        assertEquals(FIRST_EXTRA.toString(), oneCopy.toString());

        // same object
        assertEquals(FIRST_EXTRA.toString(), FIRST_EXTRA.toString());

        // different budget
        assertNotEquals(FIRST_EXTRA.toString(), SECOND_EXTRA.toString());

        // different name
        Budget editedOne = new BudgetBuilder(FIRST_EXTRA).withAmount(VALID_AMOUNT_SECONDEXTRA).build();
        assertNotEquals(FIRST_EXTRA.toString(), editedOne.toString());

        // different phone -> returns false
        editedOne = new BudgetBuilder(FIRST_EXTRA).withPeriod(VALID_PERIOD_SECONDEXTRA).build();
        assertNotEquals(FIRST_EXTRA.toString(), editedOne.toString());

        // different email -> returns false
        editedOne = new BudgetBuilder(FIRST_EXTRA).withDate(VALID_DATE_SECONDEXTRA).build();
        assertNotEquals(FIRST_EXTRA.toString(), editedOne.toString());
    }
}

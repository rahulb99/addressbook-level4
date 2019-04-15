package seedu.address.logic.commands.epiggy;

//@@author tehwenyi

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.DESC_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_FIRSTEXTRA;
import static seedu.address.logic.commands.epiggy.EditBudgetCommand.createEditedBudget;
import static seedu.address.testutil.TypicalBudgets.FIRST_EXTRA;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.epiggy.EditBudgetCommand.EditBudgetDetails;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEPiggy;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.epiggy.Allowance;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Period;
import seedu.address.model.person.Person;
import seedu.address.testutil.epiggy.BudgetBuilder;
import seedu.address.testutil.epiggy.EditBudgetDetailsBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for EditBudgetCommand.
 */
public class EditBudgetCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();
    private Date todaysDate = new Date();

    @Test
    public void execute_allFieldsSpecified_success() throws Exception {
        ModelStubWithOneCurrentBudget modelStub = new ModelStubWithOneCurrentBudget();

        EditBudgetDetails details = DESC_FIRSTEXTRA;
        Budget budgetToEdit = new BudgetBuilder().build();
        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        CommandResult commandResult = new EditBudgetCommand(details).execute(modelStub, commandHistory);

        assertEquals(String.format(EditBudgetCommand.MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(editedBudget), modelStub.budgets);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_amountSpecified_success() throws Exception {
        ModelStubWithOneCurrentBudget modelStub = new ModelStubWithOneCurrentBudget();

        Budget budgetToEdit = new BudgetBuilder().build();
        EditBudgetDetails details = new EditBudgetDetails();
        details.setPeriod(budgetToEdit.getPeriod());
        details.setStartDate(budgetToEdit.getStartDate());
        details.setAmount(new Cost(200));

        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        CommandResult commandResult = new EditBudgetCommand(details).execute(modelStub, commandHistory);

        assertEquals(String.format(EditBudgetCommand.MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(editedBudget), modelStub.budgets);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_periodSpecified_success() throws Exception {
        ModelStubWithOneCurrentBudget modelStub = new ModelStubWithOneCurrentBudget();

        Budget budgetToEdit = new BudgetBuilder().build();
        EditBudgetDetails details = new EditBudgetDetails();
        details.setStartDate(budgetToEdit.getStartDate());
        details.setAmount(budgetToEdit.getBudgetedAmount());
        details.setPeriod(new Period(26));

        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        CommandResult commandResult = new EditBudgetCommand(details).execute(modelStub, commandHistory);

        assertEquals(String.format(EditBudgetCommand.MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(editedBudget), modelStub.budgets);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_dateSpecified_success() throws Exception {
        ModelStubWithOneCurrentBudget modelStub = new ModelStubWithOneCurrentBudget();

        Budget budgetToEdit = new BudgetBuilder().build();
        EditBudgetDetails details = new EditBudgetDetails();
        details.setAmount(budgetToEdit.getBudgetedAmount());
        details.setPeriod(budgetToEdit.getPeriod());

        Date newDate = new GregorianCalendar(2010, Calendar.FEBRUARY, 11).getTime();
        details.setStartDate(newDate);

        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        CommandResult commandResult = new EditBudgetCommand(details).execute(modelStub, commandHistory);

        assertEquals(String.format(EditBudgetCommand.MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(editedBudget), modelStub.budgets);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_noCurrentBudget_failure() throws Exception {
        EditBudgetDetails details = new EditBudgetDetailsBuilder().withAmount(VALID_AMOUNT_FIRSTEXTRA).build();

        EditBudgetCommand editBudgetCommand = new EditBudgetCommand(details);
        ModelStubWithNoCurrentBudget modelStub = new ModelStubWithNoCurrentBudget();

        thrown.expect(CommandException.class);
        thrown.expectMessage(EditBudgetCommand.MESSAGE_EDIT_BUDGET_DOES_NOT_EXIST_FAIL);
        editBudgetCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_currentAndFutureBudgetPresent_success() throws Exception {
        Date todaysDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Budget futureBudget = new BudgetBuilder().withDate(cal.getTime()).build();

        ModelStubWithCurrentBudgetAndAnotherBudget modelStub =
                new ModelStubWithCurrentBudgetAndAnotherBudget(futureBudget);

        EditBudgetDetails details = DESC_FIRSTEXTRA;
        Budget budgetToEdit = new BudgetBuilder().build();
        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        ArrayList<Budget> editedBudgetList = new ArrayList<Budget>();
        editedBudgetList.add(futureBudget);
        editedBudgetList.add(editedBudget);
        CommandResult commandResult = new EditBudgetCommand(details).execute(modelStub, commandHistory);

        assertEquals(String.format(EditBudgetCommand.MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget),
                commandResult.getFeedbackToUser());
        assertEquals(editedBudgetList, modelStub.budgets);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_currentAndOldBudgetPresent_success() throws Exception {
        Date todaysDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        Budget oldBudget = new BudgetBuilder().withDate(cal.getTime()).build();

        ModelStubWithCurrentBudgetAndAnotherBudget modelStub =
                new ModelStubWithCurrentBudgetAndAnotherBudget(oldBudget);

        EditBudgetDetails details = DESC_FIRSTEXTRA;
        Budget budgetToEdit = new BudgetBuilder().build();
        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        ArrayList<Budget> editedBudgetList = new ArrayList<Budget>();
        editedBudgetList.add(editedBudget);
        editedBudgetList.add(oldBudget);
        CommandResult commandResult = new EditBudgetCommand(details).execute(modelStub, commandHistory);

        assertEquals(String.format(EditBudgetCommand.MESSAGE_EDIT_BUDGET_SUCCESS, editedBudget),
                commandResult.getFeedbackToUser());
        assertEquals(editedBudgetList, modelStub.budgets);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_currentAndFutureBudgetPresent_overlappingBudgetFailure() throws Exception {
        Date todaysDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 2);
        Budget futureBudget = new BudgetBuilder().withDate(cal.getTime()).build();

        ModelStubWithCurrentBudgetAndAnotherBudget modelStub =
                new ModelStubWithCurrentBudgetAndAnotherBudget(futureBudget);

        EditBudgetDetails details = new EditBudgetDetailsBuilder().withDate(cal.getTime()).build();
        Budget budgetToEdit = new BudgetBuilder().build();
        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        EditBudgetCommand editBudgetCommand = new EditBudgetCommand(details);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);
        editBudgetCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void execute_currentAndOldBudgetPresent_overlappingBudgetFailure() throws Exception {
        ModelStubWithCurrentBudgetAndAnotherBudget modelStub =
                new ModelStubWithCurrentBudgetAndAnotherBudget(FIRST_EXTRA);

        EditBudgetDetails details = DESC_FIRSTEXTRA;
        Budget budgetToEdit = new BudgetBuilder().build();
        Budget editedBudget = createEditedBudget(budgetToEdit, details);
        EditBudgetCommand editBudgetCommand = new EditBudgetCommand(details);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);
        editBudgetCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void isAnyFieldUpdated() {
        // pass in empty EditBudgetDetails object -> return false
        EditBudgetDetails details = new EditBudgetDetails();
        EditBudgetDetails detailsHolder = new EditBudgetDetails(details);
        assertFalse(detailsHolder.isAnyFieldEdited());

        // no fields edited in original -> return false
        assertFalse(details.isAnyFieldEdited());

        // amount edited -> return true
        details.setAmount(new Cost(10000));
        assertTrue(details.isAnyFieldEdited());

        // period edited -> return true
        EditBudgetDetails periodDetailsChange = new EditBudgetDetails();
        periodDetailsChange.setPeriod(new Period(999));
        assertTrue(periodDetailsChange.isAnyFieldEdited());

        // date edited -> return true
        EditBudgetDetails dateDetailsChange = new EditBudgetDetails();
        dateDetailsChange.setStartDate(new Date());
        assertTrue(dateDetailsChange.isAnyFieldEdited());

        // all fields edited -> return true
        details.setPeriod(new Period(999));
        details.setStartDate(new Date());
        assertTrue(details.isAnyFieldEdited());

        // filled EditBudgetDetails object passed in -> return true
        detailsHolder = new EditBudgetDetails(details);
        assertTrue(detailsHolder.isAnyFieldEdited());
    }

    @Test
    public void equals() {
        final EditBudgetCommand standardCommand = new EditBudgetCommand(DESC_FIRSTEXTRA);

        // same values -> returns true
        EditBudgetDetails copyDetails = new EditBudgetDetails(DESC_FIRSTEXTRA);
        EditBudgetCommand commandWithSameValues = new EditBudgetCommand(copyDetails);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        EditBudgetCommand copyOfStandardCommand = standardCommand;
        assertTrue(standardCommand.equals(copyOfStandardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different details -> returns false
        assertFalse(standardCommand.equals(new EditBudgetCommand(DESC_SECONDEXTRA)));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void reverseFilteredExpensesList() {
            throw new AssertionError("This method should not be called.");
        }
        @Override
        public ObservableList<Budget> getBudgetList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void sortExpenses(Comparator<Expense> comparator) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Expense> getExpenseList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getEPiggyFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEPiggyFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addExpense(Expense expense) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addAllowance(Allowance allowance) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Expense> selectedExpenseProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addBudget(int index, Budget budget) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean budgetsOverlap(Date startDate, Date endDate, Budget earlierBudget) {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public void deleteBudgetAtIndex(int index) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteExpense(Expense toDelete) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public int getCurrentBudgetIndex() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public SimpleObjectProperty<Cost> getSavings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public SimpleObjectProperty<Goal> getGoal() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGoal(Goal goal) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedExpense(Expense expense) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setEPiggy(ReadOnlyEPiggy newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyEPiggy getEPiggy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setCurrentBudget(Budget editedBudget) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Expense> getFilteredExpenseList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredBudgetList(Predicate<Budget> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredExpensesList(Predicate<Expense> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoEPiggy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoEPiggy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoEPiggy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoEPiggy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitEPiggy() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<Person> selectedPersonProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setExpense(Expense target, Expense editedExpense) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Expense getSelectedExpense() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithOneCurrentBudget extends ModelStub {
        final ArrayList<Budget> budgets = new ArrayList<>();
        private Date todaysDate = new Date();

        ModelStubWithOneCurrentBudget() {
            Budget currentBudget = new BudgetBuilder().withDate(todaysDate).build();
            budgets.add(currentBudget);
        }

        @Override
        public int getCurrentBudgetIndex() {
            return 0;
        }

        @Override
        public void setCurrentBudget(Budget editedBudget) {
            budgets.set(0, editedBudget);
        }

        @Override
        public void updateFilteredBudgetList(Predicate<Budget> predicate) {
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            return FXCollections.observableArrayList(budgets);
        }

        @Override
        public void commitEPiggy() {
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithNoCurrentBudget extends ModelStub {
        final ArrayList<Budget> budgets = new ArrayList<>();

        @Override
        public int getCurrentBudgetIndex() {
            return -1;
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            return FXCollections.observableArrayList(budgets);
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithCurrentBudgetAndAnotherBudget extends ModelStub {
        final ArrayList<Budget> budgets = new ArrayList<>();
        private Date todaysDate = new Date();
        private int currentBudgetIndex = 0;

        ModelStubWithCurrentBudgetAndAnotherBudget(Budget anotherBudget) {
            Budget currentBudget = new BudgetBuilder().withDate(todaysDate).build();
            budgets.add(currentBudget);
            if (todaysDate.after(anotherBudget.getStartDate())) {
                // old budget
                budgets.add(anotherBudget);
            } else {
                // future budget
                budgets.add(0, anotherBudget);
                currentBudgetIndex = 1;
            }
        }

        @Override
        public int getCurrentBudgetIndex() {
            return currentBudgetIndex;
        }

        @Override
        public void setCurrentBudget(Budget editedBudget) {
            budgets.set(currentBudgetIndex, editedBudget);
        }

        @Override
        public void updateFilteredBudgetList(Predicate<Budget> predicate) {
        }

        @Override
        public ObservableList<Budget> getFilteredBudgetList() {
            return FXCollections.observableArrayList(budgets);
        }

        @Override
        public void commitEPiggy() {
        }
    }
}

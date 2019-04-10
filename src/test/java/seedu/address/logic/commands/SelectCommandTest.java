package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.epiggy.TypicalExpenses.getTypicalEPiggy;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalIndexes;

/**
 * Contains integration tests (interaction with the Model) for {@code SelectCommand}.
 */
public class SelectCommandTest {
    private Model model = new ModelManager(getTypicalEPiggy(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalEPiggy(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastExpenseIndex = Index.fromOneBased(model.getFilteredExpenseList().size());

        assertExecutionSuccess(TypicalIndexes.INDEX_FIRST_EXPENSE);
        assertExecutionSuccess(TypicalIndexes.INDEX_SECOND_EXPENSE);
        assertExecutionSuccess(lastExpenseIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredExpenseList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        CommandTestUtil.showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_EXPENSE);
        CommandTestUtil.showPersonAtIndex(expectedModel, TypicalIndexes.INDEX_FIRST_EXPENSE);

        assertExecutionSuccess(TypicalIndexes.INDEX_FIRST_EXPENSE);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        CommandTestUtil.showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_EXPENSE);
        CommandTestUtil.showPersonAtIndex(expectedModel, TypicalIndexes.INDEX_FIRST_EXPENSE);

        Index outOfBoundsIndex = TypicalIndexes.INDEX_SECOND_EXPENSE;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getEPiggy().getExpenseList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        SelectCommand selectFirstCommand = new SelectCommand(TypicalIndexes.INDEX_FIRST_EXPENSE);
        SelectCommand selectSecondCommand = new SelectCommand(TypicalIndexes.INDEX_SECOND_EXPENSE);

        // same object -> returns true
        assertTrue(selectFirstCommand.equals(selectFirstCommand));

        // same values -> returns true
        SelectCommand selectFirstCommandCopy = new SelectCommand(TypicalIndexes.INDEX_FIRST_EXPENSE);
        // assertTrue(selectFirstCommand.equals(selectFirstCommandCopy));

        // different types -> returns false
        assertFalse(selectFirstCommand.equals(1));

        // null -> returns false
        assertFalse(selectFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(selectFirstCommand.equals(selectSecondCommand));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index},
     * and checks that the model's selected person is set to the person at {@code index} in the filtered person list.
     */
    private void assertExecutionSuccess(Index index) {
        SelectCommand selectCommand = new SelectCommand(index);
        String expectedMessage = String.format(SelectCommand.MESSAGE_SELECT_EXPENSE_SUCCESS, index.getOneBased());
        expectedModel.setSelectedExpense(model.getFilteredExpenseList().get(index.getZeroBased()));

        assertCommandSuccess(selectCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        SelectCommand selectCommand = new SelectCommand(index);
        CommandTestUtil.assertCommandFailure(selectCommand, model, commandHistory, expectedMessage);
    }
}

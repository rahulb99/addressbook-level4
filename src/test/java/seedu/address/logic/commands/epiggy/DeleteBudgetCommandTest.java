package seedu.address.logic.commands.epiggy;

//@@author tehwenyi

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showBudgetAtIndex;
import static seedu.address.testutil.TypicalBudgets.getTypicalEPiggy;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_BUDGET;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_BUDGET;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.epiggy.Budget;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteBudgetCommand}.
 */
public class DeleteBudgetCommandTest {

    private Model model = new ModelManager(getTypicalEPiggy(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Budget budgetToDelete = model.getFilteredBudgetList().get(INDEX_FIRST_BUDGET.getZeroBased());
        DeleteBudgetCommand deleteBudgetCommand = new DeleteBudgetCommand(INDEX_FIRST_BUDGET);

        String expectedMessage = String.format(DeleteBudgetCommand.MESSAGE_DELETE_BUDGET_SUCCESS, budgetToDelete);

        ModelManager expectedModel = new ModelManager(model.getEPiggy(), new UserPrefs());
        expectedModel.deleteBudgetAtIndex(INDEX_FIRST_BUDGET.getZeroBased());
        expectedModel.commitEPiggy();

        assertCommandSuccess(deleteBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredBudgetList().size() + 1);
        DeleteBudgetCommand deleteBudgetCommand = new DeleteBudgetCommand(outOfBoundIndex);

        assertCommandFailure(deleteBudgetCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_BUDGET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showBudgetAtIndex(model, INDEX_FIRST_BUDGET);

        Budget budgetToDelete = model.getFilteredBudgetList().get(INDEX_FIRST_BUDGET.getZeroBased());
        DeleteBudgetCommand deleteBudgetCommand = new DeleteBudgetCommand(INDEX_FIRST_BUDGET);

        String expectedMessage = String.format(DeleteBudgetCommand.MESSAGE_DELETE_BUDGET_SUCCESS, budgetToDelete);

        Model expectedModel = new ModelManager(model.getEPiggy(), new UserPrefs());
        expectedModel.deleteBudgetAtIndex(INDEX_FIRST_BUDGET.getZeroBased());
        expectedModel.commitEPiggy();
        showNoBudget(expectedModel);

        assertCommandSuccess(deleteBudgetCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showBudgetAtIndex(model, INDEX_FIRST_BUDGET);

        Index outOfBoundIndex = INDEX_SECOND_BUDGET;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getEPiggy().getBudgetList().size());

        DeleteBudgetCommand deleteBudgetCommand = new DeleteBudgetCommand(outOfBoundIndex);

        assertCommandFailure(deleteBudgetCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_BUDGET_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        Budget budgetToDelete = model.getFilteredBudgetList().get(INDEX_FIRST_BUDGET.getZeroBased());
        DeleteBudgetCommand deleteBudgetCommand = new DeleteBudgetCommand(INDEX_FIRST_BUDGET);
        Model expectedModel = new ModelManager(model.getEPiggy(), new UserPrefs());
        expectedModel.deleteBudgetAtIndex(INDEX_FIRST_BUDGET.getZeroBased());
        expectedModel.commitEPiggy();

        // delete -> first budget deleted
        deleteBudgetCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered budget list to show all persons
        expectedModel.undoEPiggy();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first budget deleted again
        expectedModel.redoEPiggy();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredBudgetList().size() + 1);
        DeleteBudgetCommand deleteBudgetCommand = new DeleteBudgetCommand(outOfBoundIndex);

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteBudgetCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_BUDGET_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() {
        DeleteBudgetCommand deleteFirstCommand = new DeleteBudgetCommand(INDEX_FIRST_BUDGET);
        DeleteBudgetCommand deleteSecondCommand = new DeleteBudgetCommand(INDEX_SECOND_BUDGET);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteBudgetCommand deleteFirstCommandCopy = new DeleteBudgetCommand(INDEX_FIRST_BUDGET);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different budget -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoBudget(Model model) {
        model.updateFilteredBudgetList(p -> false);

        assertTrue(model.getFilteredBudgetList().isEmpty());
    }
}

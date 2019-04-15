package systemtests.epiggy;

//@@author tehwenyi

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE;

import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PERIOD_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PERIOD_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_FIRSTEXTRA;
import static seedu.address.testutil.TypicalBudgets.FIRST_EXTRA;
import static seedu.address.testutil.TypicalBudgets.ONE;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.epiggy.AddBudgetCommand;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Period;
import seedu.address.testutil.epiggy.BudgetBuilder;
import seedu.address.testutil.epiggy.BudgetUtil;

public class AddBudgetCommandSystemTest extends EPiggySystemTestWithEmptyData {

    private String messageHistory = "";

    @Test
    public void add() {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a budget to a non-empty ePiggy, command with leading spaces and trailing spaces
         * -> added
         */
        Budget toAdd = FIRST_EXTRA;
        String command = "   " + AddBudgetCommand.COMMAND_WORD + "  " + AMOUNT_DESC_FIRSTEXTRA + "  "
                + PERIOD_DESC_FIRSTEXTRA + " " + DATE_DESC_FIRSTEXTRA;
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding first budget to the list -> First budget deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding Amy to the list -> Amy added again */
        command = RedoCommand.COMMAND_WORD;
        model.addBudget(0, toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a budget with all fields same as another budget in the address book except date -> added */
        toAdd = new BudgetBuilder(FIRST_EXTRA).withDate(VALID_DATE_SECONDEXTRA).build();
        command = AddBudgetCommand.COMMAND_WORD + AMOUNT_DESC_FIRSTEXTRA + PERIOD_DESC_FIRSTEXTRA
                + DATE_DESC_SECONDEXTRA;
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty address book -> added */
        messageHistory = deleteAllBudgets() + messageHistory;
        assertCommandSuccess(ONE);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate budget -> rejected */
        command = BudgetUtil.getAddBudgetCommand(ONE);
        assertCommandFailure(command, AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);

        /* Case: add a duplicate budget except with different amount -> rejected */
        toAdd = new BudgetBuilder(ONE).withPeriod(VALID_AMOUNT_FIRSTEXTRA).build();
        command = BudgetUtil.getAddBudgetCommand(toAdd);
        assertCommandFailure(command, AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);

        /* Case: add a duplicate budget except with different period -> rejected */
        toAdd = new BudgetBuilder(ONE).withDate(VALID_PERIOD_FIRSTEXTRA).build();
        command = BudgetUtil.getAddBudgetCommand(toAdd);
        assertCommandFailure(command, AddBudgetCommand.MESSAGE_OVERLAPPING_BUDGET);

        /* Case: missing amount -> rejected */
        command = AddBudgetCommand.COMMAND_WORD + PERIOD_DESC_FIRSTEXTRA + DATE_DESC_FIRSTEXTRA;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddBudgetCommand.MESSAGE_USAGE));

        /* Case: missing period -> rejected */
        command = AddBudgetCommand.COMMAND_WORD + AMOUNT_DESC_FIRSTEXTRA + DATE_DESC_FIRSTEXTRA;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddBudgetCommand.MESSAGE_USAGE));

        /* Case: missing date -> rejected */
        command = AddBudgetCommand.COMMAND_WORD + AMOUNT_DESC_FIRSTEXTRA + PERIOD_DESC_FIRSTEXTRA;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddBudgetCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "addsBudget " + BudgetUtil.getBudgetDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid amount -> rejected */
        command = AddBudgetCommand.COMMAND_WORD + INVALID_AMOUNT_DESC + PERIOD_DESC_FIRSTEXTRA + DATE_DESC_FIRSTEXTRA;
        assertCommandFailure(command, Cost.MESSAGE_CONSTRAINTS);

        /* Case: invalid period -> rejected */
        command = AddBudgetCommand.COMMAND_WORD + AMOUNT_DESC_FIRSTEXTRA + INVALID_PERIOD_DESC + DATE_DESC_FIRSTEXTRA;
        assertCommandFailure(command, Period.MESSAGE_CONSTRAINTS);

        /* Case: invalid date -> rejected */
        command = AddBudgetCommand.COMMAND_WORD + AMOUNT_DESC_FIRSTEXTRA + PERIOD_DESC_FIRSTEXTRA + INVALID_DATE_DESC;
        assertCommandFailure(command, MESSAGE_INVALID_DATE);
    }

    /**
     * Executes the {@code AddBudgetCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddBudgetCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Storage} and {@code BudgetListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EPiggySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see EPiggySystemTestWithEmptyData#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Budget toAdd) {
        assertCommandSuccess(BudgetUtil.getAddBudgetCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Budget)}. Executes {@code command}
     * instead.
     *
     * @see AddBudgetCommandSystemTest#assertCommandSuccess(Budget)
     */
    private void assertCommandSuccess(String command, Budget toAdd) {
        Model expectedModel = getModel();
        expectedModel.addBudget(0, toAdd);
        String expectedMessage = String.format(AddBudgetCommand.MESSAGE_SUCCESS, toAdd);
        // assertEquals(messageHistory, getResultDisplay().getText());
        assertCommandSuccess(command, expectedModel, expectedMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Budget)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Storage} and {@code BudgetListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     *
     * @see AddBudgetCommandSystemTest#assertCommandSuccess(String, Budget)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        messageHistory = "========================\n" + "ePiggy: " + expectedResultMessage + "\n\n" + "You: " + command
                + "\n" + messageHistory;
        assertApplicationDisplaysExpected("", messageHistory, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Storage} and {@code BudgetListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code EPiggySystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     *
     * @see EPiggySystemTestWithEmptyData#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        messageHistory = "========================\n" + "ePiggy: " + expectedResultMessage + "\n\n" + "You: " + command
                + "\n" + messageHistory;
        assertApplicationDisplaysExpected(command, messageHistory, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}

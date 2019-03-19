package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Budget;

/**
 * Allows user to view the budget set and its details.
 */
public class ViewBudgetCommand extends Command {
    public static final String COMMAND_WORD = "viewBudget";
    public static final String COMMAND_ALIAS = "vb";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the budget set for ePiggy. ";

    public static final String MESSAGE_SUCCESS = "Budget details shown above.";

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        // The display of budget should be done in model, eg. model.getBudgetDetails
        Budget budget = model.getBudget().getValue();
        String budgetMessage = "--Budget Details--\n"
                + "Budgeted Amount: $" + budget.getPrice()
                + "\nStart Date: " + budget.getStartDate()
                + "\nEnd Date: " + budget.getEndDate()
                + "\n--Current Status--\n"
                + "Remaining Days: " + budget.getRemainingDays()
                + "\nRemaining Amount: $" + budget.getRemainingAmount();
        return new CommandResult(budgetMessage);
    }
}

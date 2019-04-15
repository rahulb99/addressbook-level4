package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.model.epiggy.UniqueBudgetList.MAXIMUM_SIZE;

import java.util.Date;
import java.util.List;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Budget;

//@@author tehwenyi

/**
 * Sets a budget for the total expenses.
 */
public class AddBudgetCommand extends Command {
    public static final String COMMAND_WORD = "addBudget";
    public static final String COMMAND_ALIAS = "ab";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a budget to ePiggy.\n"
            + "Parameters: "
            + PREFIX_COST + "BUDGET_IN_DOLLARS "
            + PREFIX_PERIOD + "TIME_PERIOD_IN_DAYS "
            + PREFIX_DATE + "START_DATE_IN_DD/MM/YYYY \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_COST + "500 "
            + PREFIX_PERIOD + "30 "
            + PREFIX_DATE + "01/04/2019";

    public static final String MESSAGE_SUCCESS = "Budget has been added.\nAdded budget's details:\n%1$s";
    public static final String MESSAGE_FAIL = "Budget is too old to be added."
            + " The budget list can only accommodate a maximum of " + MAXIMUM_SIZE
            + " budgets. \nIf you wish to add this budget,"
            + " please delete one of the existing budgets and add this budget again.";
    public static final String MESSAGE_OVERLAPPING_BUDGET = "Budgets should not overlap.\n"
            + "Please ensure that the time duration of the budget you have entered "
            + "does not overlap with any existing budget.";

    private final Budget toAdd;

    /**
     * Creates an AddBudgetCommand to add the specified {@code Budget}
     */
    public AddBudgetCommand(Budget budget) {
        requireNonNull(budget);
        toAdd = budget;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        Date startDate = toAdd.getStartDate();
        Date endDate = toAdd.getEndDate();

        List<Budget> currentList = model.getFilteredBudgetList();
        if (currentList.size() == 0) {
            model.addBudget(0, toAdd);
            model.commitEPiggy();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        }

        int index = 0;
        Budget earlierBudget;
        Budget laterBudget = currentList.get(0);
        while (index < currentList.size()) {
            earlierBudget = currentList.get(index);
            if (index > 0) {
                laterBudget = currentList.get(index - 1);
            }
            if (model.budgetsOverlap(startDate, endDate, earlierBudget)) {
                throw new CommandException(MESSAGE_OVERLAPPING_BUDGET);
            }
            if (!startDate.before(earlierBudget.getEndDate())) {
                if (index == 0 || !endDate.after(laterBudget.getStartDate())) {
                    model.addBudget(index, toAdd);
                    model.commitEPiggy();
                    return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
                } else {
                    throw new CommandException(MESSAGE_OVERLAPPING_BUDGET);
                }
            }
            index++;
        }
        if (index < MAXIMUM_SIZE) {
            model.addBudget(index, toAdd);
            model.commitEPiggy();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        }
        return new CommandResult(MESSAGE_FAIL);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddBudgetCommand // instanceof handles nulls
                && toAdd.equals(((AddBudgetCommand) other).toAdd));
    }
}

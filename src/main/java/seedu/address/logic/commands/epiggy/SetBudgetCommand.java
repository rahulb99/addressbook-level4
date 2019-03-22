package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Budget;

/**
 * Sets a budget for the total expenses.
 */
public class SetBudgetCommand extends Command {
    public static final String COMMAND_WORD = "setBudget";
    public static final String COMMAND_ALIAS = "sb";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sets a budget for ePiggy. "
            + "Parameters: "
            + PREFIX_COST + "BUDGET IN DOLLARS "
            + PREFIX_PERIOD + "TIME PERIOD IN DAYS "
            + PREFIX_DATE + "START DATE IN DD/MM/YYYY \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_COST + "500 "
            + PREFIX_PERIOD + "4 "
            + PREFIX_DATE + "04/02/2019";

    public static final String MESSAGE_SUCCESS = "Budget is set at: %1$s";
    //    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    // public static final String MESSAGE_DUPLICATE_EXPENSE = "This expense already exists in the address book";

    private final Budget toSet;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public SetBudgetCommand(Budget budget) {
        requireNonNull(budget);
        toSet = budget;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        model.setBudget(toSet);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toSet));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SetBudgetCommand // instanceof handles nulls
                && toSet.equals(((SetBudgetCommand) other).toSet));
    }
}

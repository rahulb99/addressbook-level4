package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Expense;

/**
 * Adds an expense to the expense list.
 */
public class AddExpenseCommand extends Command {

    public static final String COMMAND_WORD = "addExpense";
    public static final String COMMAND_ALIAS = "ae";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an expense to the expense book.\n"
            + "Parameters: "
            + PREFIX_NAME + "EXPENSE NAME "
            + PREFIX_COST + "COST "
            + "[" + PREFIX_TAG + "TAG]..."
            + "[" + PREFIX_DATE + "DATE] \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Chicken Rice "
            + PREFIX_COST + "3.50 "
            + PREFIX_TAG + "Lunch"
            + PREFIX_DATE + "31/02/2019 ";

    public static final String MESSAGE_SUCCESS = "New expense added.\nAdded expense's details:\n%1$s";

    private final Expense toAdd;

    public AddExpenseCommand(Expense expense) {
        this.toAdd = expense;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        model.addExpense(toAdd);
        model.commitEPiggy();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}

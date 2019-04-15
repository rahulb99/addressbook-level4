package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.epiggy.ExpenseContainsKeywordsPredicate;


//@@author rahulb99
/**
 * Finds and lists all expenses in ePiggy whose expense contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_ALIAS = "fd";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds the expense as specified by the user. "
            + " The keywords do not need to be in order.\n"
            + " Parameters: [n/NAME] [$/COST:COST] [t/TAG] [d/DATE:DATE]...\n"
            + " Example: " + COMMAND_WORD + " n/Mala Hotpot t/lunch t/food $/7.00:15.00 d/14/03/2019:17/03/2019\n";
    public static final String MESSAGE_SUCCESS = MESSAGE_EXPENSES_LISTED_OVERVIEW;

    private final ExpenseContainsKeywordsPredicate predicate;

    public FindCommand(ExpenseContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        model.updateFilteredExpensesList(predicate);
        model.commitEPiggy();

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, model.getFilteredExpenseList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && predicate.equals(((FindCommand) other).predicate)); // state check
    }

}

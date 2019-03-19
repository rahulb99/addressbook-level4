package seedu.address.logic.commands.epiggy;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.model.epiggy.ExpenseContainsKeywordsPredicate;

import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * Finds and lists all expenses in EPiggy whose expense contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "search";
    public static final String COMMAND_ALIAS = "s";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " : Finds the expense as specified by the user. "
            + " The keywords do not need to be in order.\n"
            + " Parameters: [n/NAME] [$/COST:COST] [t/TAG] [d/DATE:DATE]...\n"
            + " Example: " + COMMAND_WORD + " n/Mala Hotpot t/lunch t/food $/7.00:15.00 d/14-03-2019:17-03-2019";
    public static final String MESSAGE_SUCCESS = "Found expense. ";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private final ExpenseContainsKeywordsPredicate predicate;

    public FindCommand(ExpenseContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history)  {
        requireNonNull(model);
        // EventsCenter.getInstance().post(new SwapLeftPanelEvent(SwapLeftPanelEvent.PanelType.LIST));
        // model.updateFilteredExpenseList(predicate);
        model.find(predicate);
        logger.fine("Predicate Found/ Not Found");
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

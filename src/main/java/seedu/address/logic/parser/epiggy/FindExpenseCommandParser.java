package seedu.address.logic.parser.epiggy;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.validateKeywords;

import seedu.address.logic.commands.epiggy.FindExpenseCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;

//@@author rahulb99
/**
 * Parses input arguments and creates a new FindExpenseCommand object.
 */
public class FindExpenseCommandParser implements Parser<FindExpenseCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindExpenseCommand
     * and returns an FindExpenseCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindExpenseCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExpenseCommand.MESSAGE_USAGE));
        }

        //Check whether the user follow the pattern
        if (!trimmedArgs.contains("/")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExpenseCommand.MESSAGE_USAGE));
        }

        String[] splitTrimmedArgs = trimmedArgs.split("/");
        if (splitTrimmedArgs[0].equals("")) {
            //Ensure args contains at least one prefix
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExpenseCommand.MESSAGE_USAGE));
        }
        ArgumentMultimap keywordsMap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_COST,
                        CliSyntax.PREFIX_TAG, CliSyntax.PREFIX_DATE);

        validateKeywords(keywordsMap);

        ExpenseContainsKeywordsPredicate predicate = new ExpenseContainsKeywordsPredicate(keywordsMap);
        return new FindExpenseCommand(predicate);
    }

}

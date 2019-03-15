package seedu.address.logic.parser.epiggy;

import seedu.address.logic.commands.epiggy.SetGoalCommand;
import seedu.address.logic.parser.*;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.item.Price;
import seedu.address.model.epiggy.item.Name;

import java.util.stream.Stream;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;


/**
 * Parses input arguments and creates a new SetGoalCommand object
 */
public class SetGoalCommandParser implements Parser<SetGoalCommand> {


    @Override
    public SetGoalCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_COST);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_COST)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetGoalCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseItemName(argMultimap.getValue(PREFIX_NAME).get());
        Price price = ParserUtil.parseCost(argMultimap.getValue(PREFIX_COST).get());

        Goal goal = new Goal(name, price);

        return new SetGoalCommand(goal);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

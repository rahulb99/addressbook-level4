package seedu.address.logic.parser.epiggy;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Ignore;
import org.junit.Test;

import seedu.address.logic.commands.epiggy.FindExpenseCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.address.model.expense.item.Cost;
import seedu.address.model.expense.item.Name;
import seedu.address.model.tag.Tag;

//@@author rahulb99
public class FindExpenseCommandParserTest {

    private FindExpenseCommandParser parser = new FindExpenseCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindExpenseCommand.MESSAGE_USAGE));
    }

    @Test
    @Ignore
    public void parse_validArgs_returnsFindExpenseCommand() {
        // one keyword
        ArgumentMultimap keywordsMap = prepareKeywords("n/Stationary ");
        FindExpenseCommand expectedFindExpenseCommand =
                new FindExpenseCommand(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertParseSuccess(parser, " n/Stationary ", expectedFindExpenseCommand);
        assertParseSuccess(parser, " n/ station ", expectedFindExpenseCommand);
        assertParseSuccess(parser, " n/stat ", expectedFindExpenseCommand);
        assertParseSuccess(parser, " n/stutionray ", expectedFindExpenseCommand);

        // multiple keywords
        keywordsMap = prepareKeywords("n/clothes t/shopping");
        expectedFindExpenseCommand = new FindExpenseCommand(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertParseSuccess(parser, " n/clothes t/shopping", expectedFindExpenseCommand);

        // all keywords
        keywordsMap = prepareKeywords("n/kfc t/food d/09/04/2019 $/5.00");
        expectedFindExpenseCommand = new FindExpenseCommand(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertParseSuccess(parser, " n/kfc t/food d/09/04/2019 $/5.00", expectedFindExpenseCommand);
    }

    @Test
    public void parse_invalidNameKeywords_parseFail() {
        //invalid name keywords
        assertParseFailure(parser, " n/St@ti0n@ry ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Name.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_invalidTagKeywords_parseFail() {
        //invalid tag keywords
        assertParseFailure(parser, " t/Lunch@KFC  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, Tag.MESSAGE_CONSTRAINTS));
    }

    @Test
    public void parse_invalidCostKeywords_parseFail() {
        //higher bound is smaller than lower bound
        assertParseFailure(parser, " $/5.00:1.00", Cost.MESSAGE_CONSTRAINTS);

        //invalid format
        assertParseFailure(parser, " $/1.00:2.00:3.00", Cost.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " $/1.00 => 2.00", Cost.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDateKeywords_parseFail() {
        //invalid date keywords
        assertParseFailure(parser, " d/60/02/2019", "Invalid Date");
        assertParseFailure(parser, " d/01/02/2019:99/02/2019", "Invalid Date");
        assertParseFailure(parser, " d/99/02/2019:01/10/2019", "Invalid Date");

        //ending date is earlier than starting date
        assertParseFailure(parser, " d/10/01/2018:01/01/2018", "Invalid Date");

        //invalid format
        assertParseFailure(parser, " d/01/01/2018:02/02/2018:02/03/2018", "Invalid Date");
        // assertParseFailure(parser, " d/01/01/2018 /> 02/02/2018", "Invalid Date");
    }

    @Test
    public void parse_missingPrefix_parseFail() {
        assertParseFailure(parser, " /kfc ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExpenseCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExpenseCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "$ 1.00 ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindExpenseCommand.MESSAGE_USAGE));
    }

    /**
     * Returns an {@code ArgumentMultiMap} which tokenize the {@code arg} based on prefixes.
     */
    public ArgumentMultimap prepareKeywords(String arg) {
        return ArgumentTokenizer.tokenize(" " + arg,
                PREFIX_NAME, PREFIX_COST, PREFIX_TAG, PREFIX_DATE);
    }
}

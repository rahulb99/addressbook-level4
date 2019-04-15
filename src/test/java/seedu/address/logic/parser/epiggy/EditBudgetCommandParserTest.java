package seedu.address.logic.parser.epiggy;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE;
import static seedu.address.logic.commands.CommandTestUtil.AMOUNT_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.DATE_DESC_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_AMOUNT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PERIOD_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PERIOD_DESC_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.PERIOD_DESC_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_AMOUNT_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATE_SECONDEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_FIRSTEXTRA;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PERIOD_SECONDEXTRA;
import static seedu.address.logic.commands.epiggy.EditBudgetCommand.EditBudgetDetails;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.epiggy.EditBudgetCommand;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Period;
import seedu.address.testutil.epiggy.EditBudgetDetailsBuilder;

//@@author tehwenyi

public class EditBudgetCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditBudgetCommand.MESSAGE_USAGE);

    private EditBudgetCommandParser parser = new EditBudgetCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no field specified
        assertParseFailure(parser, "1", EditBudgetCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string", EditBudgetCommand.MESSAGE_NOT_EDITED);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "i/ string", EditBudgetCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_AMOUNT_DESC, Cost.MESSAGE_CONSTRAINTS); // invalid amount
        assertParseFailure(parser, "1" + INVALID_PERIOD_DESC, Period.MESSAGE_CONSTRAINTS); // invalid period
        assertParseFailure(parser, "1" + INVALID_DATE_DESC, MESSAGE_INVALID_DATE); // invalid date

        // invalid period followed by valid date
        assertParseFailure(parser, "1" + INVALID_PERIOD_DESC + DATE_DESC_FIRSTEXTRA, Period.MESSAGE_CONSTRAINTS);

        // valid period followed by invalid period. The test case for invalid period followed by valid period
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + PERIOD_DESC_SECONDEXTRA + INVALID_PERIOD_DESC, Period.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_AMOUNT_DESC + INVALID_DATE_DESC + VALID_PERIOD_FIRSTEXTRA,
                Cost.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = PERIOD_DESC_SECONDEXTRA + DATE_DESC_FIRSTEXTRA
                + AMOUNT_DESC_FIRSTEXTRA;

        EditBudgetDetails details = new EditBudgetDetailsBuilder().withAmount(VALID_AMOUNT_FIRSTEXTRA)
                .withPeriod(VALID_PERIOD_SECONDEXTRA).withDate(VALID_DATE_FIRSTEXTRA).build();
        EditBudgetCommand expectedCommand = new EditBudgetCommand(details);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        String userInput = PERIOD_DESC_SECONDEXTRA + DATE_DESC_FIRSTEXTRA;

        EditBudgetDetails details = new EditBudgetDetailsBuilder()
                .withPeriod(VALID_PERIOD_SECONDEXTRA).withDate(VALID_DATE_FIRSTEXTRA).build();
        EditBudgetCommand expectedCommand = new EditBudgetCommand(details);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // amount
        String userInput = AMOUNT_DESC_FIRSTEXTRA;
        EditBudgetDetails details = new EditBudgetDetailsBuilder().withAmount(VALID_AMOUNT_FIRSTEXTRA).build();
        EditBudgetCommand expectedCommand = new EditBudgetCommand(details);
        assertParseSuccess(parser, userInput, expectedCommand);

        // period
        userInput = PERIOD_DESC_FIRSTEXTRA;
        details = new EditBudgetDetailsBuilder().withPeriod(VALID_PERIOD_FIRSTEXTRA).build();
        expectedCommand = new EditBudgetCommand(details);
        assertParseSuccess(parser, userInput, expectedCommand);

        // date
        userInput = DATE_DESC_FIRSTEXTRA;
        details = new EditBudgetDetailsBuilder().withDate(VALID_DATE_FIRSTEXTRA).build();
        expectedCommand = new EditBudgetCommand(details);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        String userInput = PERIOD_DESC_FIRSTEXTRA + DATE_DESC_FIRSTEXTRA + PERIOD_DESC_FIRSTEXTRA + DATE_DESC_FIRSTEXTRA
                + PERIOD_DESC_SECONDEXTRA + DATE_DESC_SECONDEXTRA;

        EditBudgetDetails details = new EditBudgetDetailsBuilder().withPeriod(VALID_PERIOD_SECONDEXTRA)
                .withDate(VALID_DATE_SECONDEXTRA).build();
        EditBudgetCommand expectedCommand = new EditBudgetCommand(details);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        String userInput = INVALID_PERIOD_DESC + PERIOD_DESC_SECONDEXTRA;
        EditBudgetDetails details = new EditBudgetDetailsBuilder().withPeriod(VALID_PERIOD_SECONDEXTRA).build();
        EditBudgetCommand expectedCommand = new EditBudgetCommand(details);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = DATE_DESC_SECONDEXTRA + INVALID_PERIOD_DESC + PERIOD_DESC_SECONDEXTRA;
        details = new EditBudgetDetailsBuilder().withPeriod(VALID_PERIOD_SECONDEXTRA)
                .withDate(VALID_DATE_SECONDEXTRA).build();
        expectedCommand = new EditBudgetCommand(details);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}

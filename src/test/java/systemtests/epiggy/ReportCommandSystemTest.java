package systemtests.epiggy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.GuiRobot;
import guitests.guihandles.epiggy.ReportWindowHandle;
import seedu.address.logic.commands.epiggy.ReportCommand;
import seedu.address.logic.parser.CliSyntax;
//@@author yunjun199321
public class ReportCommandSystemTest extends EPiggySystemTestWithDefaultData {
    private static final String ERROR_MESSAGE = "ATTENTION!!!! : On some computers, this test may fail when run on "
            + "non-headless mode as FxRobot#clickOn(Node, MouseButton...) clicks on the wrong location. We suspect "
            + "that this is a bug with TestFX library that we are using. If this test fails, you have to run your "
            + "tests on headless mode. See UsingGradle.adoc on how to do so.";
    private static final GuiRobot guiRobot = new GuiRobot();
    private static final String INVALID_DATE_WORD = "invalid";
    private static final String INVALID_DATE_LARGE_DAY = "99/03/2019";
    private static final String INVALID_DATE_LARGE_MONTH = "21/99/2019";
    private static final String INVALID_DATE_LARGE_YEAR = "21/03/999999999999999";
    private static final String INVALID_DATE_NEGATIVE_DAY = "-21/99/2019";
    private static final String INVALID_DATE_NEGATIVE_MONTH = "21/-03/2019";

    private static final String INVALID_MONTH_LARGE_MONTH = "99/2019";
    private static final String INVALID_MONTH_LARGE_YEAR = "99/999999999999999999";
    private static final String INVALID_MONTH_NEGATIVE_MONTH = "-99/2019";

    private static final String INVALID_YEAR_LARGE_YEAR = "999999999999";

    private static final String USER_INPUT_DATE = "21/03/2019";
    private static final String USER_INPUT_MONTH = "03/2019";
    private static final String USER_INPUT_YEAR = "2019";

    private StringBuilder sb = new StringBuilder();
    @Test
    public void openReportWindow() {

        String command;

        //use command box
        executeCommand(ReportCommand.COMMAND_WORD);
        assertReportWindowOpen();

        // open report window and give it focus
        executeCommand(ReportCommand.COMMAND_WORD);
        sb.append("========================\n" + "ePiggy: " + ReportCommand.MESSAGE_SUCCESS + "\n\n"
                + "You: " + ReportCommand.COMMAND_WORD + "\n");
        getMainWindowHandle().focus();
        assertReportWindowOpen();

        // check report window for specified day
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + USER_INPUT_DATE;
        sb.insert(0, "========================\n" + "ePiggy: " + ReportCommand.MESSAGE_SUCCESS + "\n\n"
                + "You: " + ReportCommand.COMMAND_WORD + "\n");
        // adds 1 empty space for output format.
        assertSuccess(command, CliSyntax.PREFIX_DATE + USER_INPUT_DATE,
                ReportCommand.COMMAND_WORD + " ");

        // check report window for alias
        command = ReportCommand.COMMAND_ALIAS;
        assertSuccess(command, "", ReportCommand.COMMAND_ALIAS);

        //use menu button
        getMainMenu().openReportWindowUsingMenu();
        assertReportWindowOpen(); // close window if report window open
        getMainWindowHandle().focus();

        // check report window for specified month
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + USER_INPUT_MONTH;
        assertSuccess(command, CliSyntax.PREFIX_DATE + USER_INPUT_MONTH,
                ReportCommand.COMMAND_WORD + " ");

        // check report window for specified year
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + USER_INPUT_YEAR;
        assertSuccess(command, CliSyntax.PREFIX_DATE + USER_INPUT_YEAR,
                ReportCommand.COMMAND_WORD + " ");

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_DATE_WORD;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_DATE_WORD);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_DATE_LARGE_DAY;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_DATE_LARGE_DAY);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_DATE_LARGE_MONTH;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_DATE_LARGE_MONTH);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_DATE_LARGE_YEAR;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_DATE_LARGE_YEAR);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_DATE_NEGATIVE_DAY;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_DATE_NEGATIVE_DAY);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_DATE_NEGATIVE_MONTH;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_DATE_NEGATIVE_MONTH);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_MONTH_LARGE_MONTH;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_MONTH_LARGE_MONTH);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_MONTH_LARGE_YEAR;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_MONTH_LARGE_YEAR);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_MONTH_NEGATIVE_MONTH;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_MONTH_NEGATIVE_MONTH);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_YEAR_LARGE_YEAR;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_YEAR_LARGE_YEAR);

        // check report window for invalid date format
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + INVALID_MONTH_LARGE_MONTH;
        assertFail(command, ReportCommand.COMMAND_WORD + " "
                + CliSyntax.PREFIX_DATE + INVALID_MONTH_LARGE_MONTH);
    }

    /**
     * Asserts that the report window is open, and closes it after checking.
     */
    private void assertReportWindowOpen() {
        assertTrue(ERROR_MESSAGE, ReportWindowHandle.isWindowPresent());
        guiRobot.pauseForHuman();

        new ReportWindowHandle(guiRobot.getStage(ReportWindowHandle.REPORT_WINDOW_TITLE)).close();
        getMainWindowHandle().focus();
    }

    /**
     * Checks for report window whether correctly open.
     * check input.
     * check textarea.
     *
     * @param command User input command.
     * @param specifiedTime User input date/month/year.
     */
    private void assertSuccess(String command, String specifiedTime, String commandWord) {
        executeCommand(command);
        assertReportWindowOpen();
        assertEquals("", getCommandBox().getInput());
        assertCommandBoxShowsDefaultStyle();
        sb.insert(0, "========================\n" + "ePiggy: " + ReportCommand.MESSAGE_SUCCESS + "\n\n"
                + "You: " + commandWord + specifiedTime + "\n");
        assertEquals(sb.toString(), getResultDisplay().getText());
    }

    /**
     * Invalid report command checking.
     *
     * @param command User input command.
     * @param userInput User input date/month/year.
     */
    private void assertFail(String command, String userInput) {
        executeCommand(command);
        assertEquals(userInput, getCommandBox().getInput());
        sb.insert(0, "========================\n" + "ePiggy: Date is invalid. Date format should "
                + "be dd/mm/yyyy and date should be valid.\n\nYou: " + command + "\n");
        assertEquals(sb.toString(), getResultDisplay().getText());
        assertCommandBoxShowsErrorStyle();
    }
}

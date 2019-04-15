package systemtests.epiggy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.GuiRobot;
import guitests.guihandles.epiggy.ReportWindowHandle;
import seedu.address.logic.commands.epiggy.ReportCommand;
import seedu.address.logic.parser.CliSyntax;
//@@author yunjun199321
public class ReportCommandEmptyDataSystemTest extends EPiggySystemTestWithEmptyData {
    private static final String USER_INPUT_DATE = "21/03/2019";
    private static final String USER_INPUT_MONTH = "03/2019";
    private static final String USER_INPUT_YEAR = "2019";
    private static final String ERROR_MESSAGE = "ATTENTION!!!! : On some computers, this test may fail when run on "
            + "non-headless mode as FxRobot#clickOn(Node, MouseButton...) clicks on the wrong location. We suspect "
            + "that this is a bug with TestFX library that we are using. If this test fails, you have to run your "
            + "tests on headless mode. See UsingGradle.adoc on how to do so.";
    private static final GuiRobot guiRobot = new GuiRobot();
    private StringBuilder sb = new StringBuilder();
    @Test
    public void openReportWindow() {

        String command;

        //use menu button to open report window
        getMainMenu().openReportWindowUsingMenu();
        assertReportWindowOpen(); // close window if report window open

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
        assertSuccess(command, CliSyntax.PREFIX_DATE + USER_INPUT_DATE);

        // check report window for specified month
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + USER_INPUT_MONTH;
        assertSuccess(command, CliSyntax.PREFIX_DATE + USER_INPUT_MONTH);

        // check report window for specified year
        command = ReportCommand.COMMAND_WORD + " " + CliSyntax.PREFIX_DATE + USER_INPUT_YEAR;
        assertSuccess(command, CliSyntax.PREFIX_DATE + USER_INPUT_YEAR);
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
     * @param command User input command.
     * @param specifiedTime User input date/month/year.
     */
    private void assertSuccess(String command, String specifiedTime) {
        executeCommand(command);
        assertReportWindowOpen();
        assertEquals("", getCommandBox().getInput());
        assertCommandBoxShowsDefaultStyle();
        sb.insert(0, "========================\n" + "ePiggy: " + ReportCommand.MESSAGE_SUCCESS + "\n\n"
                + "You: " + ReportCommand.COMMAND_WORD + " " + specifiedTime + "\n");
        assertEquals(sb.toString(), getResultDisplay().getText());
    }
}

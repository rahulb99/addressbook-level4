package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.Model;
import seedu.address.ui.ReportWindow;

/**
 * Shows summary to the user.
 */
public class ReportCommand extends Command {
    public static final String COMMAND_WORD = "report";
    public static final String COMMAND_ALIAS = "rp";
    public static final String COMMAND_USAGE = COMMAND_WORD
            + ": Shows report to the user.";

    public static final String MESSAGE_SUCCESS = "Showed report.";

    private final Logger logger = LogsCenter.getLogger(getClass());


    @Override
    public CommandResult execute(Model model, CommandHistory history) {
        requireNonNull(model);
        ReportWindow summaryWindow = new ReportWindow();
        logger.info("Creates Report window");
        // set a dummy value. TODO: Changes it when other command done.
        ExpenseDisplayType expenseDisplayType = ExpenseDisplayType.MONTHLY_EXPENSE;
        switch (expenseDisplayType) {
        case DAILY_EXPENSE:
            summaryWindow.displayDailyReport();
            break;
        case MONTHLY_EXPENSE:
            summaryWindow.displayMonthlyReport();
            break;
        case YEARLY_EXPENSE:
            summaryWindow.displayDailyReport();
            break;
        case PERCENTAGE_EXPENSE:
            summaryWindow.displayExpensePercentageReport();
            break;
        default:
            summaryWindow.displayMonthlyReport();
            break;
        }

        return new CommandResult(MESSAGE_SUCCESS, false, false, true);
    }

    /**
     * Chart will be displayed according to expense display type.
     */
    public enum ExpenseDisplayType {
        MONTHLY_EXPENSE, DAILY_EXPENSE, TOTAL_EXPENSE, YEARLY_EXPENSE, PERCENTAGE_EXPENSE
    }
}

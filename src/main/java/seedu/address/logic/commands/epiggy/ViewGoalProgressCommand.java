package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;

import javafx.beans.property.SimpleObjectProperty;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.Savings;

/**
 * Views the amount required to hit the set goal.
 */
public class ViewGoalProgressCommand extends Command {

    public static final String COMMAND_WORD = "viewGoalProgress";
    public static final String COMMAND_ALIAS = "vgp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": View your goal progress. ";

    public static final String MESSAGE_SAVINGS_LESS_THAN_GOAL = "$%1$s more to go!";
    public static final String MESSAGE_SAVINGS_MORE_THAN_GOAL = "You have reached your savings goal! Congradulations!";


    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        SimpleObjectProperty<Savings> savings = model.getSavings();
        SimpleObjectProperty<Goal> goal = model.getGoal();

        float goalAmount = (float) goal.getValue().getAmount().getAmount();
        float savingsAmount = savings.getValue().getSavings();

        float diff = goalAmount - savingsAmount;

        if (diff < 0) {
            return new CommandResult(String.format(MESSAGE_SAVINGS_MORE_THAN_GOAL));
        } else {
            return new CommandResult(String.format(MESSAGE_SAVINGS_LESS_THAN_GOAL, diff));
        }
    }
}

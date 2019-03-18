package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;

import javafx.beans.property.SimpleObjectProperty;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Goal;

/**
 * Views the current goal set.
 */
public class ViewGoalCommand extends Command {

    public static final String COMMAND_WORD = "viewGoal";
    public static final String COMMAND_ALIAS = "vg";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": View your goal set. ";

    public static final String MESSAGE_SUCCESS = "Your current goal is: %1$s";

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        SimpleObjectProperty<Goal> goal = model.getGoal();
        return new CommandResult(String.format(MESSAGE_SUCCESS, goal.getValue()));
    }
}

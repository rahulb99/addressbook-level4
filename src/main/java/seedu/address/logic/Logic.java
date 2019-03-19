package seedu.address.logic;

import java.nio.file.Path;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyExpenseList;
import seedu.address.model.person.Expense;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /**
     * Returns the EPiggy.
     *
     * @see Model#getExpenseList()
     */
    ReadOnlyExpenseList getAddressBook();


    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<seedu.address.model.epiggy.Expense> getFilteredExpenseList();

    /**
     * Returns an unmodifiable view of the list of commands entered by the user.
     * The list is ordered from the least recent command to the most recent command.
     */
    ObservableList<String> getHistory();

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Set the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);


    /**
     * Selected expense in the filtered expense list.
     * null if no expense is selected.
     *
     * @see seedu.address.model.Model#selectedExpenseProperty()
     */
    ReadOnlyProperty<seedu.address.model.epiggy.Expense> selectedExpenseProperty();


    /**
     * Sets the selected expense in the filtered expense list.
     *
     * @see seedu.address.model.Model#setSelectedExpense(seedu.address.model.epiggy.Expense)
     */
    void setSelectedExpense(seedu.address.model.epiggy.Expense expense);
}

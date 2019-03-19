package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.ExpenseContainsKeywordsPredicate;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.Expense;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Expense> PREDICATE_SHOW_ALL_EXPENSES = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyExpenseList expenseList);

    /** Returns the EPiggy */
    ReadOnlyExpenseList getExpenseList();

//    /**
//     * Returns true if a expense with the same identity as {@code expense} exists in the address book.
//     */
//    boolean hasPerson(Expense expense);

    /**
     * Deletes the given expense.
     * The expense must exist in the address book.
     */
    void deleteExpense(Expense target);

    /**
     * Adds the given expense.
     * {@code expense} must not already exist in the address book.
     */
    void addExpense(Expense expense);

    /**
     * Sets the given budget.
     */
    void setBudget(Budget budget);

    /**
     * Gets the current budget.
     */
    SimpleObjectProperty<Budget> getBudget();

    /**
     * Sets the savings goal.
     */
    void setGoal(Goal goal);

    /**
     * Replaces the given expense {@code target} with {@code editedExpense}.
     * {@code target} must exist in the address book.
     * The expense identity of {@code editedExpense} must not be the same as another existing expense in the address book.
     */
    void setExpense(Expense target, Expense editedExpense);

    /** Returns an unmodifiable view of the filtered expense list */
    ObservableList<seedu.address.model.epiggy.Expense> getFilteredExpenseList();


    /**
     * Updates the filter of the filtered expense list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredExpenseList(Predicate<seedu.address.model.epiggy.Expense> predicate);

    /**
     * Returns true if the model has previous address book states to restore.
     */
    boolean canUndoExpenseList();

    /**
     * Returns true if the model has undone address book states to restore.
     */
    boolean canRedoExpenseList();

    /**
     * Restores the model's address book to its previous state.
     */
    void undoExpenseList();

    /**
     * Restores the model's address book to its previously undone state.
     */
    void redoExpenseList();

    /**
     * Saves the current address book state for undo/redo.
     */
    void commitExpenseList();

    /**
     * Selected expense in the filtered expense list.
     * null if no expense is selected.
     */
    ReadOnlyProperty<Expense> selectedExpenseProperty();

    /**
     * Returns the selected expense in the filtered expense list.
     * null if no expense is selected.
     */
    Expense getSelectedExpense();

    /**
     * Sets the selected expense in the filtered expense list.
     */
    void setSelectedExpense(Expense expense);

    /**
     * Finds expenses based on user-specified parameters
     * @param predicate
     */
    void find(ExpenseContainsKeywordsPredicate predicate);

    /**
     * To check for input of parameters
     * @return true if there are input arguments
     */
    boolean hasParameters();
}

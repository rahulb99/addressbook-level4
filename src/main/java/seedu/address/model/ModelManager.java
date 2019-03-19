package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.ExpenseContainsKeywordsPredicate;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.person.exceptions.ExpenseNotFoundException;
import seedu.address.model.person.exceptions.NoParametersException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedEPiggy versionedEPiggy;
    private final UserPrefs userPrefs;

    private final FilteredList<seedu.address.model.epiggy.Expense> filteredExpenses;
    private final SimpleObjectProperty<seedu.address.model.epiggy.Expense> selectedExpense = new SimpleObjectProperty<>();

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyExpenseList addressBook, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        versionedEPiggy = new VersionedEPiggy(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);

        filteredExpenses = new FilteredList<>(versionedEPiggy.getExpenseList());
        filteredExpenses.addListener(this::ensureSelectedExpenseIsValid);
        //TODO
    }

    public ModelManager() {
        this(new EPiggy(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    // TODO: To check if input arguments are given or not (using parser)
    @Override
    public boolean hasParameters() {
        return true;
    }

    private void requireParameters() throws NoParametersException {
        if (!hasParameters()) {
            throw new NoParametersException();
        }
    }

    //=========== EPiggy ================================================================================

    @Override
    public void setAddressBook(ReadOnlyExpenseList expenseList) {
        versionedEPiggy.resetData(expenseList);
    }

    @Override
    public ReadOnlyExpenseList getExpenseList() {
        return versionedEPiggy;
    }

//    @Override
//    public boolean hasPerson(Expense expense) {
//        requireNonNull(expense);
//        return versionedEPiggy.hasPerson(expense);
//    }

    @Override
    public void deleteExpense(Expense target) {
        versionedEPiggy.removePerson(target);
    }

    @Override
    public void addExpense(Expense expense) {
        versionedEPiggy.addExpense(expense);
        updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);
    }

    @Override
    public void setBudget(Budget budget) {
        versionedEPiggy.setBudget(budget); }

    @Override
    public SimpleObjectProperty<Budget> getBudget() {
        return versionedEPiggy.getBudget(); }

    @Override
    public void setGoal(Goal goal) {
        versionedEPiggy.setGoal(goal);
    }

    @Override
    public void setExpense(Expense target, Expense editedExpense) {
        requireAllNonNull(target, editedExpense);

        versionedEPiggy.setPerson(target, editedExpense);
    }

    //=========== Filtered Expense List Accessors =============================================================


    /**
     * Returns an unmodifiable view of the list of {@code Expense} backed by the internal list of
     * {@code versionedEPiggy}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return FXCollections.unmodifiableObservableList(filteredExpenses);
    }

    @Override
    public void updateFilteredExpenseList(Predicate<Expense> predicate) {
        requireNonNull(predicate);
        try {
            requireParameters();
        } catch (NoParametersException e) {
            e.getMessage();
        }
        filteredExpenses.setPredicate(predicate);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoExpenseList() {
        return versionedEPiggy.canUndo();
    }

    @Override
    public boolean canRedoExpenseList() {
        return versionedEPiggy.canRedo();
    }

    @Override
    public void undoExpenseList() {
        versionedEPiggy.undo();
    }

    @Override
    public void redoExpenseList() {
        versionedEPiggy.redo();
    }

    @Override
    public void commitExpenseList() {
        versionedEPiggy.commit();
    }

    //=========== Selected expense ===========================================================================

//    public ReadOnlyProperty<Expense> selectedExpenseProperty() {
//        return selectedExpense;
//    }

    @Override
    public ReadOnlyProperty<seedu.address.model.epiggy.Expense> selectedExpenseProperty() {
        return selectedExpense;
    }

    @Override
    public Expense getSelectedExpense() {
        return selectedExpense.getValue();
    }

    @Override
    public void setSelectedExpense(Expense expense) {
        if (expense != null && !filteredExpenses.contains(expense)) {
            throw new ExpenseNotFoundException(); //TODO
        }
        selectedExpense.setValue(expense);
    }

    @Override
    public void find(ExpenseContainsKeywordsPredicate predicate) { //updateFilteredExpensesList
        requireAllNonNull(predicate);
        try {
            requireParameters();
        } catch (NoParametersException e) {
            e.getMessage();
        }
        filteredExpenses.setPredicate(predicate);
    }

    /**
     * Ensures {@code selectedExpense} is a valid expense in {@code filteredPersons}.
     * TODO: change the code - ensure expense is valid
     *       for example - date is valid, amount is greater than 0, etc.
     */
    private void ensureSelectedExpenseIsValid(ListChangeListener.Change<? extends Expense> change) {
//        while (change.next()) {
//            if (selectedExpense.getValue() == null) {
//                // null is always a valid selected expense, so we do not need to check that it is valid anymore.
//                return;
//            }
//
//            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
//                    && change.getRemoved().contains(selectedExpense.getValue());
//            if (wasSelectedPersonReplaced) {
//                // Update selectedExpense to its new value.
//                int index = change.getRemoved().indexOf(selectedExpense.getValue());
//                selectedExpense.setValue(change.getAddedSubList().get(index));
//                continue;
//            }
//
//            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
//                    .anyMatch(removedPerson -> selectedExpense.getValue().isSamePerson(removedPerson));
//            if (wasSelectedPersonRemoved) {
//                // Select the expense that came before it in the list,
//                // or clear the selection if there is no such expense.
//                selectedExpense.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
//            }
//        }
    }


    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return versionedEPiggy.equals(other.versionedEPiggy)
                && userPrefs.equals(other.userPrefs)
                && filteredExpenses.equals(other.filteredExpenses)
                && Objects.equals(selectedExpense.get(), other.selectedExpense.get());
    }

}

package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.epiggy.Allowance;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final VersionedEPiggy versionedEPiggy;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Expense> filteredExpenses;
    private final FilteredList<Budget> filteredBudget;
    private final SimpleObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Expense> selectedExpense = new SimpleObjectProperty<>();

    /**
     * Initializes a ModelManager with the given ePiggy and userPrefs.
     */
    public ModelManager(ReadOnlyEPiggy ePiggy, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(ePiggy, userPrefs);

        logger.fine("Initializing with address book: " + ePiggy + " and user prefs " + userPrefs);

        versionedEPiggy = new VersionedEPiggy(ePiggy);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(versionedEPiggy.getPersonList());
        filteredPersons.addListener(this::ensureSelectedPersonIsValid);

        filteredExpenses = new FilteredList<>(versionedEPiggy.getExpenseList());
        filteredBudget = new FilteredList<>(versionedEPiggy.getBudgetList());
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
    public Path getEPiggyFilePath() {
        return userPrefs.getEPiggyFilePath();
    }

    @Override
    public void setEPiggyFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setEPiggyFilePath(addressBookFilePath);
    }

    //=========== EPiggy ================================================================================

    @Override
    public void setEPiggy(ReadOnlyEPiggy ePiggy) {
        versionedEPiggy.resetData(ePiggy);
    }

    @Override
    public ReadOnlyEPiggy getEPiggy() {
        return versionedEPiggy;
    }

    @Override
    public void setExpense(seedu.address.model.epiggy.Expense target,
                           seedu.address.model.epiggy.Expense editedExpense) {
        requireAllNonNull(target, editedExpense);

        versionedEPiggy.setExpense(target, editedExpense);
        setSelectedExpense(editedExpense);
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return versionedEPiggy.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        versionedEPiggy.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        versionedEPiggy.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void addExpense(Expense expense) {
        versionedEPiggy.addExpense(expense);
        setSelectedExpense(expense);
    }

    @Override
    public void addAllowance(Allowance allowance) {
        versionedEPiggy.addAllowance(allowance);
        setSelectedExpense(allowance);
    }

    //@@author tehwenyi
    @Override
    public void addBudget(int index, Budget budget) {
        versionedEPiggy.addBudget(index, budget); }

    //@@author tehwenyi
    /**
     * Checks if there are any overlapping budgets.
     */
    public boolean budgetsOverlap(Date startDate, Date endDate, Budget earlierBudget) {
        return versionedEPiggy.budgetsOverlap(startDate, endDate, earlierBudget);
    }

    //@@author tehwenyi
    @Override
    public void deleteBudgetAtIndex(int index) {
        versionedEPiggy.deleteBudgetAtIndex(index);
    }

    @Override
    public void deleteExpense(Expense toDelete) {
        versionedEPiggy.deleteExpense(toDelete);
        if (selectedExpenseProperty().getValue() == toDelete) {
            setSelectedExpense(null);
        }
    }

    //@@author tehwenyi
    @Override
    public ObservableList<Budget> getBudgetList() {
        return versionedEPiggy.getBudgetList();
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return versionedEPiggy.getExpenseList();
    }

    //@@author tehwenyi
    @Override
    public int getCurrentBudgetIndex() {
        return versionedEPiggy.getCurrentBudgetIndex();
    }

    @Override
    public SimpleObjectProperty<Cost> getSavings() {
        return versionedEPiggy.getSavings();
    }

    @Override
    public SimpleObjectProperty<Goal> getGoal() {
        return versionedEPiggy.getGoal();
    }

    @Override
    public void setGoal(Goal goal) {
        versionedEPiggy.setGoal(goal);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        versionedEPiggy.setPerson(target, editedPerson);
    }

    //@@author tehwenyi
    @Override
    public void setCurrentBudget(Budget editedBudget) {
        requireNonNull(editedBudget);

        versionedEPiggy.setCurrentBudget(editedBudget);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedEPiggy}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedEPiggy}
     */
    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return filteredExpenses;
    }

    /**
     * Returns an unmodifiable view of the list of {@code Budget} backed by the internal list of
     * {@code versionedEPiggy}
     */
    @Override
    public ObservableList<Budget> getFilteredBudgetList() {
        return filteredBudget;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //@@author rahulb99
    @Override
    public void updateFilteredExpensesList(Predicate<seedu.address.model.epiggy.Expense> predicate) {
        requireNonNull(predicate);
        filteredExpenses.setPredicate(predicate);
    }

    //@@author rahulb99
    /**
     * Sorts the expenses according to the keyword.
     * @param comparator expense comparator
     */
    public void sortExpenses(Comparator<Expense> comparator) {
        requireAllNonNull(comparator);
        versionedEPiggy.sortExpense(comparator);
    }

    @Override
    public void updateFilteredBudgetList(Predicate<Budget> predicate) {
        requireNonNull(predicate);
        filteredBudget.setPredicate(predicate);
    }

    //@@author rahulb99

    /**
     * Reveres the {@code filteredExpenses} list.
     */
    public void reverseFilteredExpensesList() {
        versionedEPiggy.reverseExpenseList();
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoEPiggy() {
        return versionedEPiggy.canUndo();
    }

    @Override
    public boolean canRedoEPiggy() {
        return versionedEPiggy.canRedo();
    }

    @Override
    public void undoEPiggy() {
        versionedEPiggy.undo();
    }

    @Override
    public void redoEPiggy() {
        versionedEPiggy.redo();
    }

    @Override
    public void commitEPiggy() {
        versionedEPiggy.commit();
    }

    //=========== Selected person ===========================================================================

    @Override
    public ReadOnlyProperty<Person> selectedPersonProperty() {
        return selectedPerson;
    }

    @Override
    public ReadOnlyProperty<Expense> selectedExpenseProperty() {
        return selectedExpense;
    }

    @Override
    public Expense getSelectedExpense() {
        return selectedExpense.getValue();
    }

    @Override
    public void setSelectedPerson(Person person) {
        if (person != null && !filteredPersons.contains(person)) {
            throw new PersonNotFoundException();
        }
        selectedPerson.setValue(person);
    }

    @Override
    public void setSelectedExpense(Expense expense) {
        if (expense != null && !filteredExpenses.contains(expense)) {
            throw new PersonNotFoundException();
        }
        selectedExpense.setValue(expense);
    }

    /**
     * Ensures {@code selectedPerson} is a valid person in {@code filteredPersons}.
     */
    private void ensureSelectedPersonIsValid(ListChangeListener.Change<? extends Person> change) {
        while (change.next()) {
            if (selectedPerson.getValue() == null) {
                // null is always a valid selected person, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedPersonReplaced = change.wasReplaced() && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedPerson.getValue());
            if (wasSelectedPersonReplaced) {
                // Update selectedPerson to its new value.
                int index = change.getRemoved().indexOf(selectedPerson.getValue());
                selectedPerson.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson -> selectedPerson.getValue().isSamePerson(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the person that came before it in the list,
                // or clear the selection if there is no such person.
                selectedPerson.setValue(change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
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
                && filteredPersons.equals(other.filteredPersons)
                && Objects.equals(selectedPerson.get(), other.selectedPerson.get());
    }

    @Override
    public String toString() {
        return "ModelManager{"
                + "versionedAddressBook=" + versionedEPiggy
                + ", userPrefs=" + userPrefs
                + ", filteredPersons=" + filteredPersons
                + ", filteredExpenses=" + filteredExpenses
                + ", filteredBudget=" + filteredBudget
                + ", selectedPerson=" + selectedPerson
                + ", selectedExpense=" + selectedExpense
                + '}';
    }
}

package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.beans.InvalidationListener;
//import javafx.beans.value.ObservableObjectValue;
//import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Goal;
import seedu.address.model.epiggy.item.Item;
// import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.ExpenseList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class EPiggy implements ReadOnlyExpenseList {

    private final ExpenseList expenses;
    private final ObservableList<Item> items;
    private SimpleObjectProperty<Budget> budget;
    private SimpleObjectProperty<Goal> goal;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        expenses = new ExpenseList();
        items = FXCollections.observableArrayList();
        //TODO init budget
        budget = new SimpleObjectProperty<>();
        goal = new SimpleObjectProperty<>();
    }

    public EPiggy() {}

    /**
     * Creates an EPiggy using the Persons in the {@code toBeCopied}
     */
    public EPiggy(ReadOnlyExpenseList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setExpenses(List<seedu.address.model.epiggy.Expense> expenses) {
        this.expenses.setExpenses(expenses);
        indicateModified();
    }


    /**
     * Resets the existing data of this {@code EPiggy} with {@code newData}.
     */
    public void resetData(ReadOnlyExpenseList newData) {
        requireNonNull(newData);
        this.setExpenses(newData.getExpenseList());
    }

    //// expense-level operations

//    /**
//     * Returns true if a expense with the same identity as {@code expense} exists in the address book.
//     */
//    public boolean hasPerson(seedu.address.model.epiggy.Expense expense) {
//        requireNonNull(expense);
//        return expenses.contains(expense);
//    }

    /**
     * Adds an expense to the expense book.
     */
    public void addExpense(seedu.address.model.epiggy.Expense expense) {
        expenses.add(expense);
        indicateModified();
    }

    /**
     * Sets a budget for ePiggy.
     */
    public void setBudget(Budget budget) {
        this.budget.setValue(budget);
        indicateModified();
    }

    /**
     * Gets the current budget for ePiggy.
     */
    public SimpleObjectProperty<Budget> getBudget() {
        return budget;
    }

    /**
     * Sets the saving goal for ePiggy.
     */
    public void setGoal(Goal goal) {
        this.goal.setValue(goal);
        indicateModified();
    }

    /**
     * Replaces the given expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the address book.
     * The expense identity of {@code editedExpense} must not be the same as another existing expense in the address book.
     */
    public void setPerson(seedu.address.model.epiggy.Expense target, seedu.address.model.epiggy.Expense editedExpense) {
        requireNonNull(editedExpense);

        expenses.setExpense(target, editedExpense);
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code EPiggy}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(seedu.address.model.epiggy.Expense key) {
        expenses.remove(key);
        indicateModified();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the address book has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return expenses.asUnmodifiableObservableList().size() + " expenses";
        // TODO: refine later
    }


    @Override
    public ObservableList<seedu.address.model.epiggy.Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Item> getItemList() {
        return FXCollections.unmodifiableObservableList(items);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EPiggy // instanceof handles nulls
                && expenses.equals(((EPiggy) other).expenses));
    }

    @Override
    public int hashCode() {
        return expenses.hashCode();
    }
}

package seedu.address.model.epiggy;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.ExpenseNotFoundException;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * A expense is considered unique by comparing using {@code Expense#isSamePerson(Expense)}. As such, adding and updating of
 * persons uses Expense#isSamePerson(Expense) for equality so as to ensure that the expense being added or updated is
 * unique in terms of identity in the ExpenseList. However, the removal of a expense uses Expense#equals(Object) so
 * as to ensure that the expense with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see seedu.address.model.person.Expense#isSamePerson(seedu.address.model.person.Expense)
 */
public class ExpenseList implements Iterable<seedu.address.model.epiggy.Expense> {

    private final ObservableList<seedu.address.model.epiggy.Expense> internalList = FXCollections.observableArrayList();
    private final ObservableList<seedu.address.model.epiggy.Expense> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);


//    Not required I guess
//    /**
//     * Returns true if the list contains an equivalent expense as the given argument.
//     */
//    public boolean contains(seedu.address.model.epiggy.Expense toCheck) {
//        requireNonNull(toCheck);
//        return internalList.stream().anyMatch(toCheck::isSameExpense);
//    }

    /**
     * Adds a expense to the list.
     */
    public void add(seedu.address.model.epiggy.Expense toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Replaces the expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the list.
     * The expense identity of {@code editedExpense} must not be the same as another existing expense in the list.
     */
    public void setExpense(seedu.address.model.epiggy.Expense target, seedu.address.model.epiggy.Expense editedExpense) {
        requireAllNonNull(target, editedExpense);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ExpenseNotFoundException();
        }

        internalList.set(index, editedExpense);
    }

    /**
     * Removes the equivalent expense from the list.
     * The expense must exist in the list.
     */
    public void remove(seedu.address.model.epiggy.Expense toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ExpenseNotFoundException();
        }
    }


    /**
     * Replaces the contents of this list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setExpenses(List<seedu.address.model.epiggy.Expense> expenses) {
        requireAllNonNull(expenses);
        internalList.setAll(expenses);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<seedu.address.model.epiggy.Expense> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<seedu.address.model.epiggy.Expense> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseList // instanceof handles nulls
                && internalList.equals(((ExpenseList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}

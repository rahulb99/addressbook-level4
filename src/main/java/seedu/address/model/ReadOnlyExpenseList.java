package seedu.address.model;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
//import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.item.Item;


/**
 * Unmodifiable view of expense list
 */
public interface ReadOnlyExpenseList extends Observable {

    /**
     * Returns an unmodifiable view of the expense list.
     */
    ObservableList<Expense> getExpenseList();

    /**
     * Returns an unmodifiable view of the item list.
     */
    ObservableList<Item> getItemList();
}

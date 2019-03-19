package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code EPiggy} that keeps track of its own history.
 */
public class VersionedEPiggy extends EPiggy {

    private final List<ReadOnlyExpenseList> epiggyStateList;
    private int currentStatePointer;

    public VersionedEPiggy(ReadOnlyExpenseList initialState) {
        super(initialState);

        epiggyStateList = new ArrayList<>();
        epiggyStateList.add(new EPiggy(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code EPiggy} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        epiggyStateList.add(new EPiggy(this));
        currentStatePointer++;
        indicateModified();
    }

    private void removeStatesAfterCurrentPointer() {
        epiggyStateList.subList(currentStatePointer + 1, epiggyStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(epiggyStateList.get(currentStatePointer));
    }

    /**
     * Restores the address book to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(epiggyStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < epiggyStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedEPiggy)) {
            return false;
        }

        VersionedEPiggy otherVersionedEPiggy = (VersionedEPiggy) other;

        // state check
        return super.equals(otherVersionedEPiggy)
                && epiggyStateList.equals(otherVersionedEPiggy.epiggyStateList)
                && currentStatePointer == otherVersionedEPiggy.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of addressBookState list, unable to redo.");
        }
    }
}

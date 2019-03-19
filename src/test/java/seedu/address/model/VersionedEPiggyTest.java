package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.CARL;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.AddressBookBuilder;

public class VersionedEPiggyTest {

    private final ReadOnlyExpenseList addressBookWithAmy = new AddressBookBuilder().withPerson(AMY).build();
    private final ReadOnlyExpenseList addressBookWithBob = new AddressBookBuilder().withPerson(BOB).build();
    private final ReadOnlyExpenseList addressBookWithCarl = new AddressBookBuilder().withPerson(CARL).build();
    private final ReadOnlyExpenseList emptyAddressBook = new AddressBookBuilder().build();

    @Test
    public void commit_singleAddressBook_noStatesRemovedCurrentStateSaved() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(emptyAddressBook);

        versionedEPiggy.commit();
        assertAddressBookListStatus(versionedEPiggy,
                Collections.singletonList(emptyAddressBook),
                emptyAddressBook,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        versionedEPiggy.commit();
        assertAddressBookListStatus(versionedEPiggy,
                Arrays.asList(emptyAddressBook, addressBookWithAmy, addressBookWithBob),
                addressBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleAddressBookPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 2);

        versionedEPiggy.commit();
        assertAddressBookListStatus(versionedEPiggy,
                Collections.singletonList(emptyAddressBook),
                emptyAddressBook,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtEndOfStateList_returnsTrue() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertTrue(versionedEPiggy.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 1);

        assertTrue(versionedEPiggy.canUndo());
    }

    @Test
    public void canUndo_singleAddressBook_returnsFalse() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(emptyAddressBook);

        assertFalse(versionedEPiggy.canUndo());
    }

    @Test
    public void canUndo_multipleAddressBookPointerAtStartOfStateList_returnsFalse() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 2);

        assertFalse(versionedEPiggy.canUndo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerNotAtEndOfStateList_returnsTrue() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 1);

        assertTrue(versionedEPiggy.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtStartOfStateList_returnsTrue() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 2);

        assertTrue(versionedEPiggy.canRedo());
    }

    @Test
    public void canRedo_singleAddressBook_returnsFalse() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(emptyAddressBook);

        assertFalse(versionedEPiggy.canRedo());
    }

    @Test
    public void canRedo_multipleAddressBookPointerAtEndOfStateList_returnsFalse() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertFalse(versionedEPiggy.canRedo());
    }

    @Test
    public void undo_multipleAddressBookPointerAtEndOfStateList_success() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        versionedEPiggy.undo();
        assertAddressBookListStatus(versionedEPiggy,
                Collections.singletonList(emptyAddressBook),
                addressBookWithAmy,
                Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void undo_multipleAddressBookPointerNotAtStartOfStateList_success() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 1);

        versionedEPiggy.undo();
        assertAddressBookListStatus(versionedEPiggy,
                Collections.emptyList(),
                emptyAddressBook,
                Arrays.asList(addressBookWithAmy, addressBookWithBob));
    }

    @Test
    public void undo_singleAddressBook_throwsNoUndoableStateException() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedEPiggy.NoUndoableStateException.class, versionedEPiggy::undo);
    }

    @Test
    public void undo_multipleAddressBookPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 2);

        assertThrows(VersionedEPiggy.NoUndoableStateException.class, versionedEPiggy::undo);
    }

    @Test
    public void redo_multipleAddressBookPointerNotAtEndOfStateList_success() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 1);

        versionedEPiggy.redo();
        assertAddressBookListStatus(versionedEPiggy,
                Arrays.asList(emptyAddressBook, addressBookWithAmy),
                addressBookWithBob,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleAddressBookPointerAtStartOfStateList_success() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 2);

        versionedEPiggy.redo();
        assertAddressBookListStatus(versionedEPiggy,
                Collections.singletonList(emptyAddressBook),
                addressBookWithAmy,
                Collections.singletonList(addressBookWithBob));
    }

    @Test
    public void redo_singleAddressBook_throwsNoRedoableStateException() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(emptyAddressBook);

        assertThrows(VersionedEPiggy.NoRedoableStateException.class, versionedEPiggy::redo);
    }

    @Test
    public void redo_multipleAddressBookPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(
                emptyAddressBook, addressBookWithAmy, addressBookWithBob);

        assertThrows(VersionedEPiggy.NoRedoableStateException.class, versionedEPiggy::redo);
    }

    @Test
    public void equals() {
        VersionedEPiggy versionedEPiggy = prepareAddressBookList(addressBookWithAmy, addressBookWithBob);

        // same values -> returns true
        VersionedEPiggy copy = prepareAddressBookList(addressBookWithAmy, addressBookWithBob);
        assertTrue(versionedEPiggy.equals(copy));

        // same object -> returns true
        assertTrue(versionedEPiggy.equals(versionedEPiggy));

        // null -> returns false
        assertFalse(versionedEPiggy.equals(null));

        // different types -> returns false
        assertFalse(versionedEPiggy.equals(1));

        // different state list -> returns false
        VersionedEPiggy differentAddressBookList = prepareAddressBookList(addressBookWithBob, addressBookWithCarl);
        assertFalse(versionedEPiggy.equals(differentAddressBookList));

        // different current pointer index -> returns false
        VersionedEPiggy differentCurrentStatePointer = prepareAddressBookList(
                addressBookWithAmy, addressBookWithBob);
        shiftCurrentStatePointerLeftwards(versionedEPiggy, 1);
        assertFalse(versionedEPiggy.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedEPiggy} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedEPiggy#currentStatePointer} is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedEPiggy#currentStatePointer} is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertAddressBookListStatus(VersionedEPiggy versionedEPiggy,
                                             List<ReadOnlyExpenseList> expectedStatesBeforePointer,
                                             ReadOnlyExpenseList expectedCurrentState,
                                             List<ReadOnlyExpenseList> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new EPiggy(versionedEPiggy), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedEPiggy.canUndo()) {
            versionedEPiggy.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyExpenseList expectedAddressBook : expectedStatesBeforePointer) {
            assertEquals(expectedAddressBook, new EPiggy(versionedEPiggy));
            versionedEPiggy.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyExpenseList expectedAddressBook : expectedStatesAfterPointer) {
            versionedEPiggy.redo();
            assertEquals(expectedAddressBook, new EPiggy(versionedEPiggy));
        }

        // check that there are no more states after pointer
        assertFalse(versionedEPiggy.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedEPiggy.undo());
    }

    /**
     * Creates and returns a {@code VersionedEPiggy} with the {@code addressBookStates} added into it, and the
     * {@code VersionedEPiggy#currentStatePointer} at the end of list.
     */
    private VersionedEPiggy prepareAddressBookList(ReadOnlyExpenseList... addressBookStates) {
        assertFalse(addressBookStates.length == 0);

        VersionedEPiggy versionedEPiggy = new VersionedEPiggy(addressBookStates[0]);
        for (int i = 1; i < addressBookStates.length; i++) {
            versionedEPiggy.resetData(addressBookStates[i]);
            versionedEPiggy.commit();
        }

        return versionedEPiggy;
    }

    /**
     * Shifts the {@code versionedEPiggy#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedEPiggy versionedEPiggy, int count) {
        for (int i = 0; i < count; i++) {
            versionedEPiggy.undo();
        }
    }
}

package seedu.address.model.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.epiggy.ExpenseList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.ExpenseNotFoundException;
import seedu.address.testutil.PersonBuilder;

public class UniqueEPiggyTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ExpenseList expenseList = new ExpenseList();

    @Test
    public void contains_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.contains(null);
    }

    @Test
    public void contains_personNotInList_returnsFalse() {
        assertFalse(expenseList.contains(ALICE));
    }

    @Test
    public void contains_personInList_returnsTrue() {
        expenseList.add(ALICE);
        assertTrue(expenseList.contains(ALICE));
    }

    @Test
    public void contains_personWithSameIdentityFieldsInList_returnsTrue() {
        expenseList.add(ALICE);
        Expense editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(expenseList.contains(editedAlice));
    }

    @Test
    public void add_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.add(null);
    }

    @Test
    public void add_duplicatePerson_throwsDuplicatePersonException() {
        expenseList.add(ALICE);
        thrown.expect(DuplicatePersonException.class);
        expenseList.add(ALICE);
    }

    @Test
    public void setPerson_nullTargetPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setExpense(null, ALICE);
    }

    @Test
    public void setPerson_nullEditedPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setExpense(ALICE, null);
    }

    @Test
    public void setPerson_targetPersonNotInList_throwsPersonNotFoundException() {
        thrown.expect(ExpenseNotFoundException.class);
        expenseList.setExpense(ALICE, ALICE);
    }

    @Test
    public void setPerson_editedPersonIsSamePerson_success() {
        expenseList.add(ALICE);
        expenseList.setExpense(ALICE, ALICE);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(ALICE);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setPerson_editedPersonHasSameIdentity_success() {
        expenseList.add(ALICE);
        Expense editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        expenseList.setExpense(ALICE, editedAlice);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(editedAlice);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setPerson_editedPersonHasDifferentIdentity_success() {
        expenseList.add(ALICE);
        expenseList.setExpense(ALICE, BOB);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(BOB);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setPerson_editedPersonHasNonUniqueIdentity_throwsDuplicatePersonException() {
        expenseList.add(ALICE);
        expenseList.add(BOB);
        thrown.expect(DuplicatePersonException.class);
        expenseList.setExpense(ALICE, BOB);
    }

    @Test
    public void remove_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.remove(null);
    }

    @Test
    public void remove_personDoesNotExist_throwsPersonNotFoundException() {
        thrown.expect(ExpenseNotFoundException.class);
        expenseList.remove(ALICE);
    }

    @Test
    public void remove_existingPerson_removesPerson() {
        expenseList.add(ALICE);
        expenseList.remove(ALICE);
        ExpenseList expectedExpenseList = new ExpenseList();
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setPersons_nullUniquePersonList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setPersons((ExpenseList) null);
    }

    @Test
    public void setPersons_uniquePersonList_replacesOwnListWithProvidedUniquePersonList() {
        expenseList.add(ALICE);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(BOB);
        expenseList.setPersons(expectedExpenseList);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setPersons_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setPersons((List<Expense>) null);
    }

    @Test
    public void setPersons_list_replacesOwnListWithProvidedList() {
        expenseList.add(ALICE);
        List<Expense> expenseList = Collections.singletonList(BOB);
        this.expenseList.setPersons(expenseList);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(BOB);
        assertEquals(expectedExpenseList, this.expenseList);
    }

    @Test
    public void setPersons_listWithDuplicatePersons_throwsDuplicatePersonException() {
        List<Expense> listWithDuplicateExpenses = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicatePersonException.class);
        expenseList.setPersons(listWithDuplicateExpenses);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        expenseList.asUnmodifiableObservableList().remove(0);
    }
}

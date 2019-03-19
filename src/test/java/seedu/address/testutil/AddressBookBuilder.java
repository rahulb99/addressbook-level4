package seedu.address.testutil;

import seedu.address.model.EPiggy;
import seedu.address.model.person.Expense;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code EPiggy ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private EPiggy addressBook;

    public AddressBookBuilder() {
        addressBook = new EPiggy();
    }

    public AddressBookBuilder(EPiggy addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Expense} to the {@code EPiggy} that we are building.
     */
    public AddressBookBuilder withPerson(Expense expense) {
        addressBook.addPerson(expense);
        return this;
    }

    public EPiggy build() {
        return addressBook;
    }
}

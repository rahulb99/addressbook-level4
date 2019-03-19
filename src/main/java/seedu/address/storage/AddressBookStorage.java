package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.EPiggy;
import seedu.address.model.ReadOnlyExpenseList;

/**
 * Represents a storage for {@link EPiggy}.
 */
public interface AddressBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getAddressBookFilePath();

    /**
     * Returns EPiggy data as a {@link ReadOnlyExpenseList}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyExpenseList> readAddressBook() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyExpenseList> readAddressBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyExpenseList} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyExpenseList addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyExpenseList)
     */
    void saveAddressBook(ReadOnlyExpenseList addressBook, Path filePath) throws IOException;

    /**
     * Creates a backup file for {@link ReadOnlyExpenseList}
     * @param addressBook
     * @throws IOException
     */
    void backupAddressBook(ReadOnlyExpenseList addressBook) throws IOException;

}

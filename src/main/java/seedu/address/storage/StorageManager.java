package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyEPiggy;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of EPiggy data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private EPiggyStorage ePiggyStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(EPiggyStorage ePiggyStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.ePiggyStorage = ePiggyStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ EPiggy methods ==============================

    @Override
    public Path getEPiggyFilePath() {
        return ePiggyStorage.getEPiggyFilePath();
    }

    @Override
    public Optional<ReadOnlyEPiggy> readEPiggy() throws DataConversionException, IOException {
        return readEPiggy(ePiggyStorage.getEPiggyFilePath());
    }

    @Override
    public Optional<ReadOnlyEPiggy> readEPiggy(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return ePiggyStorage.readEPiggy(filePath);
    }

    @Override
    public void saveEPiggy(ReadOnlyEPiggy ePiggy) throws IOException {
        saveEPiggy(ePiggy, ePiggyStorage.getEPiggyFilePath());
    }

    @Override
    public void saveEPiggy(ReadOnlyEPiggy ePiggy, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        ePiggyStorage.saveEPiggy(ePiggy, filePath);
    }

    @Override
    public void backupEPiggy(ReadOnlyEPiggy ePiggy) throws IOException {
        logger.fine("Creating a backup file: " + getEPiggyFilePath() + ".backup");
        ePiggyStorage.backupEPiggy(ePiggy);
    }

}

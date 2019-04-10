package seedu.address.storage.epiggy;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyEPiggy;
import seedu.address.storage.EPiggyStorage;


/**
 * A class to access epiggy data stored as a json file on the hard disk.
 */
public class JsonEPiggyStorage implements EPiggyStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonEPiggyStorage.class);

    private Path filePath;
    private Path backupFilePath;

    public JsonEPiggyStorage(Path filePath) {
        this.filePath = filePath;
        backupFilePath = Paths.get(filePath.toString() + ".backup");
    }

    @Override
    public Path getEPiggyFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyEPiggy> readEPiggy() throws DataConversionException {
        return readEPiggy(filePath);
    }

    @Override
    public Optional<ReadOnlyEPiggy> readEPiggy(Path filePath) throws DataConversionException {
        requireNonNull(filePath);

        Optional<JsonSerializableEPiggy> jsonEPiggy = JsonUtil.readJsonFile(
                filePath, JsonSerializableEPiggy.class);
        if (!jsonEPiggy.isPresent()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonEPiggy.get().toModelType());
        } catch (IllegalValueException e) {
            logger.info("Illegal values found in " + filePath + ": " + e.getMessage());
            throw new DataConversionException(e);
        }
    }

    @Override
    public void saveEPiggy(ReadOnlyEPiggy ePiggy) throws IOException {
        saveEPiggy(ePiggy, filePath);
    }

    @Override
    public void saveEPiggy(ReadOnlyEPiggy ePiggy, Path filePath) throws IOException {
        requireNonNull(ePiggy);
        requireNonNull(filePath);
        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableEPiggy(ePiggy), filePath);
    }

    @Override
    public void backupEPiggy(ReadOnlyEPiggy ePiggy) throws IOException {
        saveEPiggy(ePiggy, backupFilePath);
    }
}

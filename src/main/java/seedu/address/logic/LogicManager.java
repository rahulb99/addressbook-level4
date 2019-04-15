package seedu.address.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.EPiggyParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyEPiggy;
import seedu.address.model.epiggy.Budget;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.Goal;

import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final CommandHistory history;
    private final EPiggyParser ePiggyParser;
    private boolean addressBookModified;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        history = new CommandHistory();
        ePiggyParser = new EPiggyParser();

        // Set addressBookModified to true whenever the models' address book is modified.
        model.getEPiggy().addListener(observable -> addressBookModified = true);
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        addressBookModified = false;

        CommandResult commandResult;
        try {
            Command command = ePiggyParser.parseCommand(commandText);
            commandResult = command.execute(model, history);
        } finally {
            if (!commandText.isEmpty()) {
                history.add(commandText);
            }
        }

        if (addressBookModified) {
            logger.info("ePiggy modified, saving to file.");
            try {
                storage.saveEPiggy(model.getEPiggy());
            } catch (IOException ioe) {
                throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
            }
        }

        return commandResult;
    }

    @Override
    public ReadOnlyEPiggy getEPiggy() {
        return model.getEPiggy();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public ObservableList<Expense> getFilteredExpenseList() {
        return model.getFilteredExpenseList();
    }

    //@@author tehwenyi
    @Override
    public ObservableList<Budget> getFilteredBudgetList() {
        return model.getFilteredBudgetList();
    }

    @Override
    public ObservableValue<Cost> getSavings() {
        return model.getSavings();
    }

    @Override
    public ObservableValue<Goal> getGoal() {
        return model.getGoal();
    }

    @Override
    public ObservableList<String> getHistory() {
        return history.getHistory();
    }

    @Override
    public Path getEPiggyFilePath() {
        return model.getEPiggyFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyProperty<Person> selectedPersonProperty() {
        return model.selectedPersonProperty();
    }

    @Override
    public void setSelectedPerson(Person person) {
        model.setSelectedPerson(person);
    }

    @Override
    public void setSelectedExpense(Expense expense) {
        model.setSelectedExpense(expense);
    }

    //@@author tehwenyi
    @Override
    public void setCurrentBudget(Budget budget) {
        model.setCurrentBudget(budget); }

    //@@author tehwenyi
    @Override
    public void addBudget(int index, Budget budget) {
        model.addBudget(index, budget); }

    @Override
    public ReadOnlyProperty<Expense> selectedExpenseProperty() {
        return model.selectedExpenseProperty();
    }
}

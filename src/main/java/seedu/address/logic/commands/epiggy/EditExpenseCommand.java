package seedu.address.logic.commands.epiggy;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EXPENSES;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.epiggy.Allowance;
import seedu.address.model.epiggy.Expense;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Item;
import seedu.address.model.epiggy.item.Name;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing expense.
 */
public class EditExpenseCommand extends Command {

    public static final String COMMAND_WORD = "editExpense";
    public static final String COMMAND_ALIAS = "ee";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the expense identified "
            + "by the index in the displayed expense list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_COST + "COST] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_COST + "5 "
            + PREFIX_TAG + "food";

    public static final String MESSAGE_EDIT_EXPENSE_SUCCESS = "Expense edited.\nEdited expense's details:\n%1$s";
    public static final String MESSAGE_NOT_EDITED = "Expense not edited as there are no changes.";

    private static final String MESSAGE_ITEM_NOT_EXPENSE = "The item selected is not an expense. "
            + "Please use " + COMMAND_WORD + " to edit expenses and "
            + EditAllowanceCommand.COMMAND_WORD + " to edit allowances.";

    final Index index;
    final EditExpenseDescriptor editExpenseDescriptor;

    /**
     * @param index of the expense in the filtered expense list to edit
     * @param editExpenseDescriptor details to edit the expense with
     */
    public EditExpenseCommand(Index index, EditExpenseDescriptor editExpenseDescriptor) {
        requireNonNull(index);
        requireNonNull(editExpenseDescriptor);

        this.index = index;
        this.editExpenseDescriptor = new EditExpenseDescriptor(editExpenseDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<Expense> lastShownList = model.getFilteredExpenseList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EXPENSE_DISPLAYED_INDEX);
        }

        Expense toEdit = lastShownList.get(index.getZeroBased());
        if (toEdit instanceof Allowance) {
            throw new CommandException(MESSAGE_ITEM_NOT_EXPENSE);
        }
        Expense editedExpense = createEditedExpense(toEdit, editExpenseDescriptor);

        model.setExpense(toEdit, editedExpense);
        model.updateFilteredExpensesList(PREDICATE_SHOW_ALL_EXPENSES);
        model.commitEPiggy();
        return new CommandResult(String.format(MESSAGE_EDIT_EXPENSE_SUCCESS, editedExpense));
    }

    /**
     * Creates and returns a {@code Expense} with the details of {@code expenseToEdit}
     * edited with {@code editExpenseDescriptor}.
     */
    static Expense createEditedExpense(Expense expenseToEdit, EditExpenseDescriptor editExpenseDescriptor) {
        assert expenseToEdit != null;

        Name updatedName = editExpenseDescriptor.getName().orElse(expenseToEdit.getItem().getName());
        Cost updatedCost = editExpenseDescriptor.getCost().orElse(expenseToEdit.getItem().getCost());
        Date updatedDate = editExpenseDescriptor.getDate().orElse(expenseToEdit.getDate());
        Set<Tag> updatedTags = editExpenseDescriptor.getTags().orElse(expenseToEdit.getItem().getTags());
        return new Expense(new Item(updatedName, updatedCost, updatedTags), updatedDate);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditExpenseCommand)) {
            return false;
        }

        // state check
        EditExpenseCommand e = (EditExpenseCommand) other;
        return index.equals(e.index)
                && editExpenseDescriptor.equals(e.editExpenseDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditExpenseDescriptor {

        private seedu.address.model.epiggy.item.Name name;
        private Cost cost;
        private Date date;
        private Set<Tag> tags;

        public EditExpenseDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditExpenseDescriptor(EditExpenseDescriptor toCopy) {
            setName(toCopy.name);
            setCost(toCopy.cost);
            setDate(toCopy.date);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, cost, date, tags);
        }

        public void setName(seedu.address.model.epiggy.item.Name name) {
            this.name = name;
        }

        public void setCost(Cost cost) {
            this.cost = cost;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        public Optional<seedu.address.model.epiggy.item.Name> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<Cost> getCost() {
            return Optional.ofNullable(cost);
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            if (tags != null) {
                tags.add(new Tag("Expense"));
                return Optional.of(Collections.unmodifiableSet(tags));
            } else {
                return Optional.empty();
            }
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditExpenseDescriptor)) {
                return false;
            }

            // state check
            EditExpenseDescriptor e = (EditExpenseDescriptor) other;

            return getName().equals(e.getName())
                    && getCost().equals(e.getCost())
                    && getDate().equals(e.getDate())
                    && getTags().equals(e.getTags());
        }
    }
}

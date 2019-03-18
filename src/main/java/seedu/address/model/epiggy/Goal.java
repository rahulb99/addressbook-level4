package seedu.address.model.epiggy;

import seedu.address.model.epiggy.item.Name;
import seedu.address.model.epiggy.item.Price;

/**
 * Represents a Goal in the expense book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Goal {
    private final Name name;
    private final Price amount;

    public Goal(Name name, Price amount) {
        this.name = name;
        this.amount = amount;
    }

    public Name getName() {
        return name;
    }

    public Price getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s - $%2s", name, amount);
    }
}

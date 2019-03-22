package seedu.address.model.epiggy;

import seedu.address.model.epiggy.item.Date;
import seedu.address.model.epiggy.item.Period;
import seedu.address.model.epiggy.item.Price;

/**
 * Represents a Budget in the expense book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Budget {
    private final Price amount;
    private final Date startDate;
    private final Date endDate;
    private final Period period;
    private Price remainingAmount;
    private Period remainingDays;

    /**
     * Represents a Budget in the expense book.
     * Guarantees: details are present and not null, field values are validated, immutable.
     */
    public Budget(Price amount, Period period, Date startDate) {
        this.amount = amount;
        this.startDate = startDate;
        this.period = period;
        this.endDate = calculateEndDate(startDate, period);
    }

    /**
     * Calculates the end date = startDate + period (number of days)
     * @param startDate
     * @param period
     * @return endDate
     */
    private Date calculateEndDate(Date startDate, Period period) {
        return startDate.addDays(period.getTimePeriod());
    }

    private void setRemainingAmount(Price remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    private void setRemainingDays(Period remainingDays) {
        this.remainingDays = remainingDays;
    }

    public Price getRemainingAmount() {
        return remainingAmount;
    }

    public Price getPrice() {
        return this.amount;
    }

    public Period getPeriod() {
        return this.period;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("$")
                .append(getPrice())
                .append(" for every ")
                .append(getPeriod())
                .append(" days starting from ")
                .append(getStartDate())
                .append(" till ")
                .append(getEndDate());
        return builder.toString();
    }
}

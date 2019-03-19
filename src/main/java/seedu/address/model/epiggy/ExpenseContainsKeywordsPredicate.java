package seedu.address.model.epiggy;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.model.epiggy.item.Item;

import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * TODO: Refactor
 * Tests that a {@code Expense}'s {@code Name, Cost, Category, Date} matches any of the keywords given.
 */
public class ExpenseContainsKeywordsPredicate implements Predicate<Expense> {

    private final ArgumentMultimap keywords;

    public ExpenseContainsKeywordsPredicate(ArgumentMultimap keywords) {
        assert keywords != null : "keywords should not be null.";
        this.keywords = keywords;
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param expense the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override
    public boolean test(Expense expense) {
        assert expense != null : "Expense should not be null.";

        String nameKeywords = keywords.getValue(PREFIX_NAME).orElse("");
        List<String> tagKeywords = keywords.getAllValues(PREFIX_TAG);
        String dateKeywords = keywords.getValue(PREFIX_DATE).orElse("");
        String costKeywords = keywords.getValue(PREFIX_COST).orElse("");

        //if all keywords are absent, return false
        if (nameKeywords.equals("") && tagKeywords.isEmpty()
                && dateKeywords.equals("") && costKeywords.equals("")) {
            return false;
        }

        //if one or more keywords are present
        boolean result = true;
        if (!nameKeywords.equals("")) {
            result = result && containsNameKeywords(nameKeywords, expense);
        }

        if (!costKeywords.equals("")) {
            result = result && isWithinCostRange(costKeywords, expense);
        }

        if (!dateKeywords.equals("")) {
            result = result && isWithinDateRange(dateKeywords, expense);
        }

        if (!tagKeywords.isEmpty()) {
            result = result && checkTagKeywords(tagKeywords, expense);
        }
        
        return result;
    }

    /**
     * Return true if the {@code Name} of {@code expense} contains {@code nameKeywords}.
     * */
    public boolean containsNameKeywords(String nameKeywords, Expense expense) {
        assert nameKeywords != null : "nameKeywords should not be null.";
        List<String> splitNameKeywords = Arrays.asList(nameKeywords.trim().split("\\s+"));
        Item item = expense.getItem();
        boolean result = splitNameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(item.getName().name, keyword));
        return result;
    }

    /**
     * Return true if any of the {@code Tag} of {@code expense} contains any element of {@code tagKeywords}.
     * */
    public boolean checkTagKeywords(List<String> tagKeywords, Expense expense) {
        assert tagKeywords != null : "dateKeywords should not be null.";
        List<String> separatedTagKeywordsList = new ArrayList<>();
        for (String tag : tagKeywords) {
            separatedTagKeywordsList.addAll(Arrays.asList(tag.split("\\s+")));
        }
        Item item = expense.getItem();
        boolean result = tagKeywords.stream()
                .anyMatch(keyword -> item.getTags().stream()
                        .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword)));
        return result;
    }

    /**
     * Return true if the {@code Cost} of {@code expense} is within the range denoted by {@code costKeywords}.
     * */
    public boolean isWithinCostRange(String costKeywords, Expense expense) {
        assert costKeywords != null : "costKeywords should not be null.";
        boolean result;
        String[] splitCost = costKeywords.split(":");
        Item item = expense.getItem();
        if (splitCost.length == 1) { //if the user enters a particular cost
            double chosenCost = Double.parseDouble(splitCost[0]);
            result = item.getPrice().getAmount() == chosenCost;
        } else { //if the user enters a range of dates
            double lowerBound = Double.parseDouble(splitCost[0]);
            double higherBound = Double.parseDouble(splitCost[1]);
            result = lowerBound <= item.getPrice().getAmount()
                    && item.getPrice().getAmount() <= higherBound;
        }
        return result;
    }

    /**
     * Return true if the {@code Date} of {@code expense} is within the range denoted by {@code dateKeywords}.
     * */
    public boolean isWithinDateRange(String dateKeywords, Expense expense) {
        assert dateKeywords != null : "dateKeywords should not be null.";
        boolean result;
        String[] splitDate = dateKeywords.split(":");
        if (splitDate.length == 1) { //if the user only enter a particular date
            Date chosenDate = new Date(splitDate[0]);
            result = expense.getDate().equals(chosenDate);
        } else { //if the user enter a range of dates
            Date start = new Date(splitDate[0]);
            Date end = new Date(splitDate[1]);
            boolean isWithinRange = start.after(expense.getDate())
                    && end.before(expense.getDate());
            result = start.equals(expense.getDate())
                    || end.equals(expense.getDate())
                    || isWithinRange;
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ExpenseContainsKeywordsPredicate) other).keywords)); // state check
    }

}

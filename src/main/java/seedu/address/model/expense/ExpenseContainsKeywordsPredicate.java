package seedu.address.model.expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.model.expense.item.Item;

//@@author rahulb99
/**
 * TODO: Refactor
 * Tests that a {@code expense}'s {@code Name, Cost, Category, Date} matches any of the keywords given.
 */
public class ExpenseContainsKeywordsPredicate implements Predicate<Expense> {

    public static final int LEVENSHTIEN_THRESHOLD = 5;
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
        assert expense != null : "expense should not be null.";

        List<String> nameKeywords = keywords.getAllValues(CliSyntax.PREFIX_NAME);
        List<String> tagKeywords = keywords.getAllValues(CliSyntax.PREFIX_TAG);
        String dateKeywords = keywords.getValue(CliSyntax.PREFIX_DATE).orElse("");
        String costKeywords = keywords.getValue(CliSyntax.PREFIX_COST).orElse("");

        //if all keywords are absent, return false
        if (nameKeywords.equals("") && tagKeywords.isEmpty()
                && dateKeywords.equals("") && costKeywords.equals("")) {
            return false;
        }

        //if one or more keywords are present
        boolean result = true;
        if (!nameKeywords.equals("")) {
            result = containsNameKeywords(nameKeywords, expense);
        }

        if (!costKeywords.equals("")) {
            result = result && isWithinCostRange(costKeywords, expense);
        }

        if (!dateKeywords.equals("")) {
            try {
                result = result && isWithinDateRange(dateKeywords, expense);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!tagKeywords.isEmpty()) {
            result = result && checkTagKeywords(tagKeywords, expense);
        }

        return result;
    }

    /**
     * Return true if the {@code Name} of {@code expense} contains {@code nameKeywords}.
     * */
    public boolean containsNameKeywords(List<String> nameKeywords, Expense expense) {
        assert nameKeywords != null : "nameKeywords should not be null.\n";
        Item item = expense.getItem();
        boolean result = true;
        for (String n: nameKeywords) {
            result = result && item.getName().name.toLowerCase().contains(n.trim().toLowerCase());
            result = result || (levenshtienDist(item.getName().name, n) < LEVENSHTIEN_THRESHOLD);
        }
        return result;
    }

    /**
     * Return true if any of the {@code Tag} of {@code expense} contains any element of {@code tagKeywords}.
     * */
    public boolean checkTagKeywords(List<String> tagKeywords, Expense expense) {
        assert tagKeywords != null : "tagKeywords should not be null.\n";
        boolean result = true;
        Item item = expense.getItem();
        for (String tag : tagKeywords) {
            result = result && item.getTags().stream()
                    .anyMatch(keyword -> (keyword.tagName.trim().toLowerCase().contains(tag.trim().toLowerCase()))
                     || (levenshtienDist(keyword.tagName, tag) < LEVENSHTIEN_THRESHOLD));
        }
        return result;
    }

    /**
     * Return true if the {@code Cost} of {@code expense} is within the range denoted by {@code costKeywords}.
     * */
    public boolean isWithinCostRange(String costKeywords, Expense expense) {
        assert costKeywords != null : "costKeywords should not be null.\n";
        boolean result;
        String[] splitCost = costKeywords.split(":");
        Item item = expense.getItem();
        if (splitCost.length == 1) { //if the user enters an exact cost
            double chosenCost = Double.parseDouble(splitCost[0]);
            result = item.getCost().getAmount() == chosenCost;
        } else { //if the user enters a range of dates
            double lowerBound = Double.parseDouble(splitCost[0]);
            double higherBound = Double.parseDouble(splitCost[1]);
            result = lowerBound <= item.getCost().getAmount()
                    && item.getCost().getAmount() <= higherBound;
        }
        return result;
    }

    /**
     * Return true if the {@code Date} of {@code expense} is within the range denoted by {@code dateKeywords}.
     * */
    public boolean isWithinDateRange(String dateKeywords, Expense expense) throws ParseException {
        assert dateKeywords != null : "dateKeywords should not be null.\n";
        boolean result;
        String[] splitDate = dateKeywords.split(":");
        if (splitDate.length == 1) { //if the user only enter an exact date
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            cal.setTime(sdf.parse(dateKeywords));
            Calendar expenseCal = Calendar.getInstance();
            // SimpleDateFormat sdf1 = new SimpleDateFormat("EEE, MMM d, yyyy");
            // expenseCal.setTime(sdf1.parse(expense.getDate().toString()));
            expenseCal.setTime(expense.getDate());
            result = cal.get(Calendar.YEAR) == expenseCal.get(Calendar.YEAR)
                    && cal.get(Calendar.MONTH) == expenseCal.get(Calendar.MONTH)
                    && cal.get(Calendar.DATE) == expenseCal.get(Calendar.DATE);
        } else { //if the user enter a range of dates
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            start.setTime(sdf.parse(splitDate[0]));
            end.setTime(sdf.parse(splitDate[1]));
            Calendar expenseCal = Calendar.getInstance();
            expenseCal.setTime(expense.getDate());
            boolean isWithinRange = start.before(expenseCal)
                    && end.after(expenseCal);
            result = start.equals(expenseCal)
                    || end.equals(expenseCal)
                    || isWithinRange;
        }
        return result;
    }

    /**
     * Calculate Levenshtien distance for almost similar words.
     * @param a {@code Name}
     * @param b input keyword
     * @return levenshtien distance
     */
    public static int levenshtienDist(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ExpenseContainsKeywordsPredicate) other).keywords)); // state check
    }

}

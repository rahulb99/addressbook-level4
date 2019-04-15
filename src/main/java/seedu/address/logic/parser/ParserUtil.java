package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_CANNOT_CREATE_ALLOWANCE_TAG;
import static seedu.address.commons.core.Messages.MESSAGE_CANNOT_CREATE_EXPENSE_TAG;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.epiggy.item.Cost;
import seedu.address.model.epiggy.item.Name;
import seedu.address.model.epiggy.item.Period;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";


    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static seedu.address.model.person.Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!seedu.address.model.person.Name.isValidName(trimmedName)) {
            throw new ParseException(seedu.address.model.person.Name.MESSAGE_CONSTRAINTS);
        }
        return new seedu.address.model.person.Name(trimmedName);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Name parseItemName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String cost} into a {@code Cost}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Cost parseCost(String cost) throws ParseException {
        requireNonNull(cost);
        String trimmedCost = cost.trim();
        if (!Cost.isValidCost(trimmedCost)) {
            throw new ParseException(Cost.MESSAGE_CONSTRAINTS);
        }
        return new Cost(Double.parseDouble(trimmedCost));
    }

    /**
     * Parses a {@code String Date} into a {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Date parseDate(String date) throws ParseException {
        requireNonNull(date);
        Date parsedDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            parsedDate = dateFormat.parse(date.trim());
        } catch (java.text.ParseException parseException) {
            throw new ParseException(MESSAGE_INVALID_DATE);
        }
        return parsedDate;
    }

    /**
     * Parses a {@code String period} into a {@code Period}.
     * Leading and trailing whitespaces will be trimmed.
     */
    public static Period parsePeriod(String period) throws ParseException {
        requireNonNull(period);
        String trimmedPeriod = period.trim();
        if (!Period.isValidPeriod(trimmedPeriod)) {
            throw new ParseException(Period.MESSAGE_CONSTRAINTS);
        }
        return new Period(Integer.parseInt(trimmedPeriod));
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        if (tag.toLowerCase().equals("allowance")) {
            throw new ParseException(MESSAGE_CANNOT_CREATE_ALLOWANCE_TAG);
        } else if (tag.toLowerCase().equals("expense")) {
            throw new ParseException(MESSAGE_CANNOT_CREATE_EXPENSE_TAG);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Keywords validation.
     * @param keywordsMap user input to keyword mapping.
     * @throws ParseException if any of the keywords are invalid.
     */
    public static void validateKeywords(ArgumentMultimap keywordsMap) throws ParseException {
        List<String> nameKeywords = keywordsMap.getAllValues(PREFIX_NAME);
        List<String> tagKeywords = keywordsMap.getAllValues(PREFIX_TAG);
        List<String> dateKeywords = keywordsMap.getAllValues(PREFIX_DATE);
        List<String> costKeywords = keywordsMap.getAllValues(PREFIX_COST);

        validateNameKeywords(nameKeywords);
        validateTagKeywords(tagKeywords);
        validateDateKeywords(dateKeywords);
        validateCostKeywords(costKeywords);
    }

    /**
     * Keywords validation for sort.
     * @param keywordsMap user input to keyword mapping.
     * @throws ParseException if any of the keywords are invalid.
     */
    public static void validateKeywordsForSort(ArgumentMultimap keywordsMap) throws ParseException {
        List<String> nameKeywords = keywordsMap.getAllValues(PREFIX_NAME);
        List<String> tagKeywords = keywordsMap.getAllValues(PREFIX_TAG);
        List<String> dateKeywords = keywordsMap.getAllValues(PREFIX_DATE);
        List<String> costKeywords = keywordsMap.getAllValues(PREFIX_COST);

        if (!nameKeywords.isEmpty() && !tagKeywords.isEmpty() && !dateKeywords.isEmpty() && !costKeywords.isEmpty()) {
            throw new ParseException("Invalid userInput");
        }
    }

    /**
     * Name keyword validation.
     * @throws ParseException if name keyword is invalid (not alphanumeric).
     */
    private static void validateNameKeywords(List<String> nameKeywords) throws ParseException {
        for (String name: nameKeywords) {
            if (!Name.isValidName(name)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, Name.MESSAGE_CONSTRAINTS));
            }
        }
    }

    /**
     * Tag keyword validation.
     * @throws ParseException if tag keyword is invalid (not alphanumeric).
     */
    private static void validateTagKeywords(List<String> tagKeywords) throws ParseException {
        for (String tag: tagKeywords) {
            if (!Tag.isValidTagName(tag)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, Tag.MESSAGE_CONSTRAINTS));
            }
        }
    }

    /**
     * Name keyword validation.
     * @throws ParseException if name keyword is invalid (not alphanumeric).
     */
    private static void validateDateKeywords(List<String> dateKeywords) throws ParseException {
        for (String d: dateKeywords) {
            String d1 = d.split(":")[0];
            String d2 = d.split(":").length == 2 ? d.split(":")[1] : "";

            //If the user enters one date keyword and it is invalid
            if (d2.equals("") && !isValidDate(d1)) {
                throw new ParseException("Invalid Date");
            }

            //If the user enters a range of date keywords and any of the two dates is invalid
            if (!d2.equals("") && (!isValidDate(d1) || !isValidDate(d2))) {
                throw new ParseException("Invalid Date");
            }

            //If the ending date is earlier than the starting date
            if (!d2.equals("")) {
                Date date1 = new Date(d1);
                Date date2 = new Date(d2);
                if (date1.after(date2)) {
                    throw new ParseException("Invalid Date");
                }
            }

            //If the user enters more than one colon
            if (d.split(":").length > 2) {
                throw new ParseException("Invalid Date");
            }
        }
    }

    /**
     * Date validation.
     * @param date input date
     * @return true if date is valid, false otherwise.
     * @throws ParseException if date is valid.
     */
    private static boolean isValidDate(String date) throws ParseException {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);
        try {
            Date d = sdfrmt.parse(date);
        } catch (java.text.ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Name keyword validation.
     * @throws ParseException if name keyword is invalid (not alphanumeric).
     */
    private static void validateCostKeywords(List<String> costKeywords) throws ParseException {
        for (String cost : costKeywords) {
            String c1 = cost.split(":")[0];
            String c2 = cost.split(":").length == 2 ? cost.split(":")[1] : "";

            //If the user enters one cost keyword and it is invalid
            if (c2.equals("") && !Cost.isValidCost(c1)) {
                throw new ParseException(Cost.MESSAGE_CONSTRAINTS);
            }

            //If the user enters a range of cost keywords and any of the two cost is invalid
            if (!c2.equals("") && (!Cost.isValidCost(c1) || !Cost.isValidCost(c2))) {
                throw new ParseException(Cost.MESSAGE_CONSTRAINTS);
            }

            //If cost2 is lesser than cost1
            if (!c2.equals("")) {
                if (Float.parseFloat(c1) > Float.parseFloat(c2)) {
                    throw new ParseException(Cost.MESSAGE_CONSTRAINTS);
                }
            }

            //If the user enters more than one colon
            if (cost.split(":").length > 2) {
                throw new ParseException(Cost.MESSAGE_CONSTRAINTS);
            }
        }
    }
}

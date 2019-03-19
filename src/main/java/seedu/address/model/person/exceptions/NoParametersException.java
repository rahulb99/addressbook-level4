package seedu.address.model.person.exceptions;

public class NoParametersException extends Exception {

    public NoParametersException(String desc) {
        super(desc);
    }

    public NoParametersException() {
        super("Not logged into any user.");
    }

}

package exceptions;

public class AccountAlreadyExistsException extends Throwable {
    public AccountAlreadyExistsException() {

    }

    public AccountAlreadyExistsException(String s) {
        super(s);
    }
}

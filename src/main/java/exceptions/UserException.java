package exceptions;

public class UserException extends RuntimeException{
    public UserException(String exception_message) {
        super(exception_message);
    }
}

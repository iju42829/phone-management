package study.phonemanagement.exception.user;

public class AlreadyExistsUsernameException extends IllegalStateException {
    public AlreadyExistsUsernameException(String s) {
        super(s);
    }
}

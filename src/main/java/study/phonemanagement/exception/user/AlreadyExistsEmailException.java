package study.phonemanagement.exception.user;

public class AlreadyExistsEmailException extends IllegalStateException {
    public AlreadyExistsEmailException(String s) {
        super(s);
    }
}

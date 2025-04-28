package study.phonemanagement.exception.phone;

import java.util.NoSuchElementException;

public class PhoneNotFoundException extends NoSuchElementException {
    public PhoneNotFoundException(String s) {
        super(s);
    }
}

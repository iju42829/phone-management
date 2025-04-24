package study.phonemanagement.controller.user.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import study.phonemanagement.controller.user.request.CreateUserRequest;

@Component
public class CreateUserRequestValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return CreateUserRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreateUserRequest createUserRequest = (CreateUserRequest) target;

        if (!errors.hasErrors()) {
            if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
                errors.reject("confirmPasswordNotMisMatch", "비밀번호 확인이 일치하지 않습니다.");
            }
        }
    }
}

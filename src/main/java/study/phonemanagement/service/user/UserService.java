package study.phonemanagement.service.user;

import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.service.user.response.AddressResponse;

public interface UserService {

    Long createUser(CreateUserRequest createUserRequest);
    AddressResponse getUserAddress(CustomUserDetails customUserDetails);
}

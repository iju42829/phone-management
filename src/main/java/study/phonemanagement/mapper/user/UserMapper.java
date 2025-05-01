package study.phonemanagement.mapper.user;

import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.user.response.AddressResponse;

public interface UserMapper {
    User toUser(CreateUserRequest createUserRequest);
    AddressResponse toAddressResponse(User user);
}

package study.phonemanagement.mapper.user;

import org.springframework.stereotype.Component;
import study.phonemanagement.controller.user.request.CreateUserRequest;
import study.phonemanagement.entity.common.Address;
import study.phonemanagement.entity.user.User;
import study.phonemanagement.service.user.response.AddressResponse;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toUser(CreateUserRequest createUserRequest) {
        if (createUserRequest == null) {
            return null;
        }

        return User.builder()
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .password(createUserRequest.getPassword())
                .gender(createUserRequest.getGender())
                .address(new Address(createUserRequest.getCity(), createUserRequest.getStreet(),
                        createUserRequest.getZipcode(), createUserRequest.getDetail()))
                .build();
    }

    @Override
    public AddressResponse toAddressResponse(User user) {
        if (user == null) {
            return null;
        }

        return AddressResponse.builder()
                .city(user.getAddress().getCity())
                .street(user.getAddress().getStreet())
                .zipcode(user.getAddress().getZipcode())
                .detail(user.getAddress().getDetail())
                .build();
    }
}

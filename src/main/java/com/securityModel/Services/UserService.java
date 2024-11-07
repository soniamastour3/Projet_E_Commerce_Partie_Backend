package com.securityModel.Services;


import com.securityModel.dtos.request.UserRequest;
import com.securityModel.dtos.response.UserResponse;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    UserResponse createuser(UserRequest user);
    List<UserResponse> alluser();
    UserResponse userById(Long id);
    UserResponse updateuser(UserRequest userRequest, Long id);
    HashMap<String, String> deleteuser(Long id);
}

package com.securityModel.Services.impl;



import com.securityModel.Services.UserService;
import com.securityModel.dtos.request.UserRequest;
import com.securityModel.dtos.response.UserResponse;
import com.securityModel.modele.User;
import com.securityModel.repository.UserDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceIMPL implements UserService {
    private final UserDao userDaoconst;

    public UserServiceIMPL(UserDao userDaoconst) {
        this.userDaoconst = userDaoconst;
    }

    @Override
    public UserResponse createuser(UserRequest user) {
        User u= UserResponse.toEntity(user);
        User saveduser= userDaoconst.save(u);
        return UserResponse.fromEntity(saveduser);
    }

    @Override
    public List<UserResponse> alluser() {
        return userDaoconst.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse userById(Long id) {
        return userDaoconst.findById(id)
                .map(UserResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("User not found with this id:" +id));
    }

    @Override
    public UserResponse updateuser(UserRequest userRequest, Long id) {
        User user=userDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("User not found with this id:" +id));
        if (user != null) {
            User u = UserResponse.toEntity(userRequest);
            u.setId(id);
            User saveduser = userDaoconst.save(u);
            return UserResponse.fromEntity(saveduser);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteuser(Long id) {
        HashMap message =new HashMap<>();
        User u = userDaoconst.findById(id).orElse(null);
        if (u != null) {
            try {
                userDaoconst.deleteById(id);
                message.put("user", "Driver deleted successfully");
            }catch (Exception a){
                message.put("user", a.getMessage());
            }
        }else{
            message.put("message", "User not found: " +id);
        }
        return message;
    }


}

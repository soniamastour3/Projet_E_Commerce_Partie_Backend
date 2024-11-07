package com.securityModel.dtos.response;




import com.securityModel.dtos.request.UserRequest;
import com.securityModel.modele.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String username;

    public static UserResponse fromEntity(User entity) {
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(entity, userResponse);
        return userResponse;
    }

    public static User toEntity(UserRequest userResponse) {
        User u = new User();
        BeanUtils.copyProperties(userResponse, u);
        return u;

    }
}

package com.securityModel.dtos.response;


import com.securityModel.dtos.request.DriverRequest;
import com.securityModel.modele.Driver;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private String username;
    private String adress;

    public static DriverResponse fromEntity(Driver entity) {
        DriverResponse driverResponse = new DriverResponse();
        BeanUtils.copyProperties(entity, driverResponse);
        return driverResponse;
    }

    public static Driver toEntity(DriverRequest driverResponse) {
        Driver d = new Driver();
        BeanUtils.copyProperties(driverResponse, d);
        return d;

    }
}

package com.securityModel.Services;



import com.securityModel.dtos.request.DriverRequest;
import com.securityModel.dtos.response.DriverResponse;

import java.util.HashMap;
import java.util.List;

public interface DriverService {
    DriverResponse createdriver(DriverRequest driver);
    List<DriverResponse> alldriver();
    DriverResponse driverById(Long id);
    DriverResponse updatedriver(DriverRequest driverRequest, Long id);
    HashMap<String, String> deletedriver(Long id);
}

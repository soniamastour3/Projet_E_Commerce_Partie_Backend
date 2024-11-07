package com.securityModel.Services.impl;


import com.securityModel.Services.DriverService;
import com.securityModel.dtos.request.DriverRequest;
import com.securityModel.dtos.response.DriverResponse;
import com.securityModel.modele.Driver;
import com.securityModel.repository.DriverDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverServiceIMPL implements DriverService {
    private final DriverDao driverDaoconst;

    public DriverServiceIMPL(DriverDao driverDaoconst) {
        this.driverDaoconst = driverDaoconst;
    }

    @Override
    public DriverResponse createdriver(DriverRequest driver) {
        Driver d= DriverResponse.toEntity(driver);
        Driver saveddriver= driverDaoconst.save(d);
        return DriverResponse.fromEntity(saveddriver);
    }

    @Override
    public List<DriverResponse> alldriver() {
        return driverDaoconst.findAll().stream()
                .map(DriverResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DriverResponse driverById(Long id) {
        return driverDaoconst.findById(id)
                .map(DriverResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("Driver not found with this id:" +id));
    }

    @Override
    public DriverResponse updatedriver(DriverRequest driverRequest, Long id) {
        Driver driver=driverDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("Driver not found with this id:" +id));
        if (driver != null) {
            Driver d = DriverResponse.toEntity(driverRequest);
            d.setId(id);
            Driver saveddriver = driverDaoconst.save(d);
            return DriverResponse.fromEntity(saveddriver);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deletedriver(Long id) {
        HashMap message =new HashMap<>();
        Driver d = driverDaoconst.findById(id).orElse(null);
        if (d != null) {
            try {
                driverDaoconst.deleteById(id);
                message.put("message", "Driver deleted successfully");
            }catch (Exception a){
                message.put("message", a.getMessage());
            }
        }else{
            message.put("message", "Driver not found: " +id);
        }
        return message;
    }
}

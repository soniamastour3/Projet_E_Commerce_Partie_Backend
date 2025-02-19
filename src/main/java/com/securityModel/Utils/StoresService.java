package com.securityModel.Utils;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;

@Service
public class StoresService {
    private final Path rootLocation = Paths.get("C:\\Users\\ahmed\\Desktop\\Projet E-commerce\\Backend\\Projet_E_Commerce_Partie_Backend\\upload");



    public String store(MultipartFile file) {
        try {

            String fileName = Integer.toString(new Random().nextInt(10000));
            //extention
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'),
                    file.getOriginalFilename().length());
            //le nom de l'image
            String name  = file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf('.'));
            String original = name + fileName + ext;
            Files.copy(file.getInputStream(), this.rootLocation.resolve(original));
            return original;


        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }



    public  Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("FAIL!");
        }
    }
}

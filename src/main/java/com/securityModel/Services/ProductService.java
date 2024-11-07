package com.securityModel.Services;


import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.dtos.response.ProductResponse;
import com.securityModel.modele.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface ProductService {

    ProductResponse createproductwithimage(ProductRequest productRequest, MultipartFile file);
    ProductResponse ajoutImgSubPro(ProductRequest productRequest,Long idSubcategory,Long idProvider, MultipartFile file);
    ProductResponse createproductwithgallery(ProductRequest productRequest, List<MultipartFile> files);
    ProductResponse createproduct(ProductRequest product);
    Page<Product> getPaginateProducts(int page, int size);
    List<ProductResponse> allproduct();
    ProductResponse productById(int id);
    ProductResponse updateproductwithimage(ProductRequest productRequest, int id, MultipartFile file);
    HashMap <String, String> deleteproduct(int id);
    List<ProductResponse> findByName(String name);
    List<ProductResponse> findByPrice(double minPrice, double maxPrice);

}

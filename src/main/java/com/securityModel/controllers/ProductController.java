package com.securityModel.controllers;


import com.securityModel.Services.ProductService;
import com.securityModel.Utils.StoresService;
import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.dtos.response.ProductResponse;
import com.securityModel.modele.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private StoresService storesService;

    public final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/save")
    public ProductResponse addproduct(@RequestBody ProductRequest productRequest) {
        return productService.createproduct(productRequest);
    }

    @PostMapping(value="/saveImage", consumes = { "multipart/form-data" })
    public ProductResponse createproductwithimage(ProductRequest productRequest, MultipartFile file) {
        return productService.createproductwithimage(productRequest, file);
    }



    @PostMapping(value = "/saveImageSubPro/{subCategory_id}/{provider_id}",  consumes = { "multipart/form-data" })
    public ProductResponse ajoutImgSubPro(ProductRequest productRequest,@PathVariable  Long subCategory_id,
                                          @PathVariable Long provider_id, MultipartFile file) {
        return productService.ajoutImgSubPro(productRequest, subCategory_id, provider_id, file);
    }

    @PostMapping("/saveGallery")
    public ProductResponse createproductwithgallery(ProductRequest productRequest, List<MultipartFile> files) {
        return productService.createproductwithgallery(productRequest, files);
    }

    @GetMapping("/all")
    public List<ProductResponse> AllProduct() {
        return productService.allproduct();
    }

    @GetMapping("/getone/{id}")
    public ProductResponse productbyid(@PathVariable int id) {
        return productService.productById(id);
    }

    @PutMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
    public ProductResponse updateproduct(ProductRequest productRequest, @PathVariable int id, MultipartFile file) {
        return productService.updateproductwithimage(productRequest, id, file);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deleteproduct(@PathVariable int id) {
        return productService.deleteproduct(id);
    }

    @GetMapping("/pagination")
    public Page<Product> getPaginateProducts(int page, int size) {
        return productService.getPaginateProducts(page, size);
    }

    // Rechercher par nom
    @GetMapping("/searchByName")
    public List<ProductResponse> searchByName(@RequestParam String name) {
        return productService.findByName(name);

    }

    @GetMapping("/searchByPrice")
    public List<ProductResponse> searchByPrice(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return productService.findByPrice(minPrice, maxPrice);
    }



    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storesService.loadFile(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}

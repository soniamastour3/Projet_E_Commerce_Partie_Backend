package com.securityModel.Services.impl;



import com.securityModel.Services.ProductService;
import com.securityModel.Utils.StoresService;
import com.securityModel.dtos.request.ProductRequest;
import com.securityModel.dtos.response.ProductResponse;
import com.securityModel.modele.Gallery;
import com.securityModel.modele.Product;
import com.securityModel.modele.Provider;
import com.securityModel.modele.SubCategory;
import com.securityModel.repository.GalleryDao;
import com.securityModel.repository.ProductDao;
import com.securityModel.repository.ProviderDao;
import com.securityModel.repository.SubCategoryDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceIMPL implements ProductService {
   private final ProductDao productDaoconst;
   private final ProductDao productDao;
   private final GalleryDao galleryDao;
   private final StoresService storesService;
   private final SubCategoryDao subcategoryDaoconst;
   private final ProviderDao providerDaoconst;

   public ProductServiceIMPL(ProductDao productDaoconst, ProductDao productDao, GalleryDao galleryDao, StoresService storesService, SubCategoryDao subcategoryDaoconst, ProviderDao providerDaoconst) {
       this.productDaoconst= productDaoconst;
       this.productDao = productDao;
       this.galleryDao = galleryDao;
       this.storesService = storesService;
       this.subcategoryDaoconst = subcategoryDaoconst;
       this.providerDaoconst = providerDaoconst;
   }

    @Override
    public ProductResponse createproductwithimage(ProductRequest productRequest, MultipartFile file) {
        Product p=ProductResponse.toEntity(productRequest);
        p.setName(productRequest.getName());
        p.setRet(productRequest.getRet());
        p.setColor(productRequest.getColor());
        p.setQte(productRequest.getQte());
        p.setQte_en_stock(productRequest.getQte_en_stock());
        p.setPrice(productRequest.getPrice());
        p.setDescription(productRequest.getDescription());
        String img = storesService.store(file);
        p.setImage(img);
        Product savedproduct= productDaoconst.save(p);
        return ProductResponse.fromEntity(savedproduct);
    }

    @Override
    public ProductResponse ajoutImgSubPro(ProductRequest productRequest, Long subCategory_id, Long provider_id, MultipartFile file) {
        // Trouver la sous-catégorie par son ID
        SubCategory subcategory = subcategoryDaoconst.findById(subCategory_id)
                .orElseThrow(() -> new RuntimeException("Subcategory not found with this id: " + subCategory_id));
        // Trouver le fournisseur par son ID
        Provider provider = providerDaoconst.findById(provider_id)
                .orElseThrow(() -> new RuntimeException("Provider not found with this id: " + provider_id));
        productRequest.setSubCategory(subcategory);  // Associer la sous-catégorie
        productRequest.setProvider(provider);
        String img=storesService.store(file);
        productRequest.setImage(img);
        // Créer le produit à partir de la requête
        Product product = ProductResponse.toEntity(productRequest);
        // Associer le fournisseur
        // Sauvegarder le produit
        Product savedProduct = productDaoconst.save(product);
        // Retourner la réponse
        return ProductResponse.fromEntity(savedProduct);
    }

    @Override
    public ProductResponse createproductwithgallery(ProductRequest productRequest, List<MultipartFile> files) {
        // Convertir ProductRequest en entité Product
        Product product = ProductResponse.toEntity(productRequest);
        product.setQte_en_stock(productRequest.getQte());

        // Créer une liste pour les galeries d'images
        List<Gallery> galleries = new ArrayList<>();

        // Gérer les fichiers d'image et enregistrer les chemins dans la galerie
        for (MultipartFile file : files) {
            // Stocker l'image et obtenir le chemin d'accès
            String imgPath = storesService.store(file);

            // Créer une nouvelle entité Gallery pour chaque image
            Gallery gallery = new Gallery();
            gallery.setUrl_photo(imgPath);
            gallery.setProduct(product); // Associer la galerie au produit

            // Ajouter la galerie à la liste
            galleries.add(gallery);
        }

        // Associer les galeries au produit
        product.setGalleries(galleries);

        // Sauvegarder le produit avec ses galeries
        Product savedProduct = productDao.save(product);

        // Sauvegarder chaque galerie
        galleries.forEach(gallery -> galleryDao.save(gallery));

        // Retourner la réponse sous forme de ProductResponse
        return ProductResponse.fromEntity(savedProduct);
    }

//    @Override
//    public ProductResponse createproductwithgallery(ProductRequest productRequest, List<MultipartFile> files) {
//        // Convert ProductRequest to Product entity
//        Product p = ProductResponse.toEntity(productRequest);
//        p.setQte_en_stock(productRequest.getQte());
//
//        // Handle image files and save paths in Product
//        List<String> imagePaths = new ArrayList<>();
//
//            for (MultipartFile file : files) {
//                // Store the image and add the path to the list
//                String imgPath = storesService.store(file);
//                imagePaths.add(imgPath);
//
//            p.setGalleries(imagePaths);
//        }
//
//        // Save the product
//        Product savedProduct = productDao.save(p);
//
//        // Return the response as ProductResponse
//        return ProductResponse.fromEntity(savedProduct);
//    }

    @Override
    public ProductResponse createproduct(ProductRequest product){
       Product p=ProductResponse.toEntity(product);
       p.setQte_en_stock(product.getQte());
       Product savedproduct= productDaoconst.save(p);
       return ProductResponse.fromEntity(savedproduct);
   }
   @Override
    public List<ProductResponse> allproduct(){
       return productDaoconst.findAll().stream()
               .map(ProductResponse::fromEntity)
               .collect(Collectors.toList());
   }

    @Override
    public ProductResponse productById(int id) {
        return productDaoconst.findById(id)
        .map(ProductResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("Product not found with this id:" +id));
    }

    @Override
    public ProductResponse updateproductwithimage(ProductRequest productRequest, int id, MultipartFile file) {
        Product product=productDaoconst.findById(id).orElseThrow(()->
        new RuntimeException("Product not found with this id:" +id));
        if (product != null) {
            Product p = ProductResponse.toEntity(productRequest);
            p.setId(id);
            // Si le nom de p est null, on utilise le nom du produit. Sinon, on garde le nom actuel de p.
            p.setName(p.getName() == null ? product.getName() : p.getName());
            p.setRet(p.getRet() == null ? product.getRet() : p.getRet());
            p.setColor(p.getColor() == null ? product.getColor() : p.getColor());
            p.setQte(p.getQte() == null ? product.getQte() : p.getQte());
            p.setQte_en_stock(p.getQte_en_stock()== null ? product.getQte_en_stock() : p.getQte_en_stock());
            p.setPrice(p.getPrice() == null ? product.getPrice() : p.getPrice());
            p.setDescription(p.getDescription() == null ? product.getDescription() : p.getDescription());
            String img = storesService.store(file);
            p.setImage(img);
            Product savedproduct = productDaoconst.save(p);
            return ProductResponse.fromEntity(savedproduct);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deleteproduct(int id) {
       HashMap message =new HashMap<>();
       Product p = productDaoconst.findById(id).orElse(null);
       if (p != null) {
           try {
               productDaoconst.deleteById(id);
               message.put("message", "Product deleted successfully");
           }catch (Exception a){
               message.put("message", a.getMessage());
           }
       }else{
           message.put("message", "Product not found: " +id);
       }
       return message;
    }
    @Override
    public Page<Product> getPaginateProducts(int page, int size){
       Pageable pageable = PageRequest.of(page, size);
       return productDaoconst.findAll(pageable);
    }

//Filtrer par nom
    public List<ProductResponse> findByName(String name) {
        return productDaoconst.findByName(name).stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }
    // Filtrer par prix
    public List<ProductResponse> findByPrice(double minPrice, double maxPrice) {
        return productDaoconst.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }


}

package com.securityModel.Services.impl;



import com.securityModel.Services.SubCategoryService;
import com.securityModel.dtos.request.SubCategoryRequest;
import com.securityModel.dtos.response.SubCategoryResponse;
import com.securityModel.modele.Category;
import com.securityModel.modele.SubCategory;
import com.securityModel.repository.SubCategoryDao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.securityModel.Services.impl.CategoryServiceIMPL.categoryDaoconst;


@Service
public class SubCategoryServiceIMPL implements SubCategoryService {
    private final SubCategoryDao subCategoryDaoconst;

    public SubCategoryServiceIMPL(SubCategoryDao subCategoryDaoconst) {
        this.subCategoryDaoconst = subCategoryDaoconst;
    }

    @Override
    public SubCategoryResponse createSubCategory(SubCategoryRequest subCategory){
        SubCategory s =SubCategoryResponse.toEntity(subCategory);
        SubCategory savedsubCategory= subCategoryDaoconst.save(s);
        return SubCategoryResponse.fromEntity(savedsubCategory);
    }

    @Override
    public List<SubCategoryResponse> allSubCategory(){
        return subCategoryDaoconst.findAll().stream()
                .map(SubCategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public SubCategoryResponse subCategoryById(Long id) {
        return subCategoryDaoconst.findById(id)
                .map(SubCategoryResponse::fromEntity)
                .orElseThrow(() ->new RuntimeException("Sub Category not found with this id:" +id));    }

    @Override
    public SubCategoryResponse updatesubCategory(SubCategoryRequest subCategoryRequest, Long id) {
        SubCategory subCategory = subCategoryDaoconst.findById(id).orElseThrow(()->
                new RuntimeException("Sub Category not found with this id:" +id));
        if (subCategory != null) {
            SubCategory s = SubCategoryResponse.toEntity(subCategoryRequest);
            s.setId(id);
            s.setName(s.getName() == null ? subCategory.getName() : s.getName());
            SubCategory savedsubCategory = subCategoryDaoconst.save(s);
            return SubCategoryResponse.fromEntity(savedsubCategory);
        }else{
            return null;
        }
    }

    @Override
    public HashMap<String, String> deletesubCategory(Long id) {

        HashMap message =new HashMap<>();
        SubCategory s = subCategoryDaoconst.findById(id).orElse(null);
        if (s != null) {
            try {
                subCategoryDaoconst.deleteById(id);
                message.put("message", "Sub Category deleted successfully");
            }catch (Exception a){
                message.put("message", a.getMessage());
            }
        }else{
            message.put("message", "Sub Category not found: " +id);
        }
        return message;
    }

    @Override
    public SubCategoryResponse createSubCategoryCategory(SubCategoryRequest subCategoryRequest, Long id) {
        // Récupérer la catégorie à partir de l'ID fourni

        Category category = categoryDaoconst.findById(id).orElseThrow(() ->
                new RuntimeException("Catégorie non trouvée avec cet identifiant : " + id));

        // Créer une nouvelle sous-catégorie
        SubCategory subCategory = SubCategoryResponse.toEntity(subCategoryRequest);
        subCategory.setCategory(category); // Associer la catégorie

        // Sauvegarder la sous-catégorie dans la base de données
        SubCategory savedsubCategory = subCategoryDaoconst.save(subCategory);

        // Retourner la réponse
        return SubCategoryResponse.fromEntity(savedsubCategory);
    }
    @Override
    public SubCategoryResponse updateSubCategoryCategory(SubCategoryRequest subCategoryRequest, Long categoryId, Long subCategoryId){
        // Récupérer la sous-catégorie à partir de l'ID fourni
        SubCategory subCategory = subCategoryDaoconst.findById(subCategoryId)
                .orElseThrow(() -> new RuntimeException("Sous-catégorie non trouvée avec cet identifiant : " + subCategoryId));

        if (categoryId!= null) {

            Category c = categoryDaoconst.findById(categoryId).orElse(null);
            if (c != null) {
                subCategoryRequest.setCategory(c);
            } else {
                throw new RuntimeException("categorie not found");
            }
        }
        else{
            subCategoryRequest.setCategory(subCategoryRequest.getCategory()==null? subCategory.getCategory(): subCategoryRequest.getCategory());
                }
        if (subCategory != null) {
            SubCategory subcategory1 = SubCategoryResponse.toEntity(subCategoryRequest);
            subcategory1.setId(subCategoryId);
            subCategory.setName(subCategoryRequest.getName());
            subCategory.setDescription(subCategoryRequest.getDescription());
            SubCategory savedsub = subCategoryDaoconst.save(subcategory1);
            return SubCategoryResponse.fromEntity(savedsub);
        } else {
            throw  new RuntimeException("subcategory not found ");
        }

    }
}

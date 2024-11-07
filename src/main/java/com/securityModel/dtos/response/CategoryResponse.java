package com.securityModel.dtos.response;


import com.securityModel.dtos.request.CategoryRequest;
import com.securityModel.modele.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private List<SubCategoryResponse> subCategories;

    //Création de l'objet CategoryResponse
    public static CategoryResponse fromEntity(Category entity) {
        CategoryResponse categoryResponse = new CategoryResponse();
        //Copie des propriétés de l'entité Category vers CategoryResponse
        BeanUtils.copyProperties(entity, categoryResponse);
        //Vérification de l'existence des sous-catégories (SubCategories)
        if(entity.getSubCategories() != null) {
            //Conversion des sous-catégories
            categoryResponse.setSubCategories(entity.getSubCategories().stream()
                    .map(SubCategoryResponse::fromEntity)
                    .collect(Collectors.toList()));
        }
        return categoryResponse;
    }

    public static Category toEntity(CategoryRequest categoryResponse) {
        Category c = new Category();
        BeanUtils.copyProperties(categoryResponse, c);
        return c;
    }

}

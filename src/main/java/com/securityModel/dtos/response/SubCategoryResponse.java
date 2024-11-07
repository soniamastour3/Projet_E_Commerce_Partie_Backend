package com.securityModel.dtos.response;


import com.securityModel.dtos.request.SubCategoryRequest;
import com.securityModel.modele.Category;
import com.securityModel.modele.SubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private Category category;

    public static SubCategoryResponse fromEntity(SubCategory entity) {
        SubCategoryResponse subCategoryResponse = new SubCategoryResponse();
        BeanUtils.copyProperties(entity, subCategoryResponse);
        return subCategoryResponse;
    }

    public static SubCategory toEntity(SubCategoryRequest subCategoryResponse) {
        SubCategory s = new SubCategory();
        BeanUtils.copyProperties(subCategoryResponse, s);
        return s;
    }

}

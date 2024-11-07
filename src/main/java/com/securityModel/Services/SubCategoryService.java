package com.securityModel.Services;




import com.securityModel.dtos.request.SubCategoryRequest;
import com.securityModel.dtos.response.SubCategoryResponse;

import java.util.HashMap;
import java.util.List;

public interface SubCategoryService {
    SubCategoryResponse createSubCategory(SubCategoryRequest subCategory);
    List<SubCategoryResponse> allSubCategory();
    SubCategoryResponse subCategoryById(Long id);
    SubCategoryResponse updatesubCategory(SubCategoryRequest subCategoryRequest, Long id);
    HashMap<String, String> deletesubCategory(Long id);
    SubCategoryResponse createSubCategoryCategory(SubCategoryRequest subCategoryRequest, Long id);
    SubCategoryResponse updateSubCategoryCategory(SubCategoryRequest subCategoryRequest, Long categoryId, Long subCategoryId);
}

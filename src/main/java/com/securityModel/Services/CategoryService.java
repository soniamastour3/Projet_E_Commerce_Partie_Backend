package com.securityModel.Services;



import com.securityModel.dtos.request.CategoryRequest;
import com.securityModel.dtos.response.CategoryResponse;

import java.util.HashMap;
import java.util.List;

public interface CategoryService {
    CategoryResponse createcategory(CategoryRequest category);
    List<CategoryResponse> allCategory();
    CategoryResponse categoryById(Long id);
    CategoryResponse updatecategory(CategoryRequest categoryRequest, Long id);
    HashMap<String, String> deletecategory(Long id);
}

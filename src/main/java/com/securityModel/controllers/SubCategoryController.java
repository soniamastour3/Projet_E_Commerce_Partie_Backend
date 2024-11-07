package com.securityModel.controllers;



import com.securityModel.Services.SubCategoryService;
import com.securityModel.dtos.request.SubCategoryRequest;
import com.securityModel.dtos.response.SubCategoryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/subCategorys")
public class SubCategoryController {
    public final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping("/save")
    public SubCategoryResponse addSubCategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        return subCategoryService.createSubCategory(subCategoryRequest);
    }

    @GetMapping("/all")
    public List<SubCategoryResponse> AllSubCategory() {
        return subCategoryService.allSubCategory();
    }

    @GetMapping("/getone/{id}")
    public SubCategoryResponse subCategorybyid(@PathVariable Long id) {
        return subCategoryService.subCategoryById(id);
    }

    @PutMapping("/update/{id}")
    public SubCategoryResponse updatesubCategory(@RequestBody SubCategoryRequest subCategoryRequest, @PathVariable Long id) {
        return subCategoryService.updatesubCategory(subCategoryRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    public HashMap<String, String> deletesubCategory(@PathVariable Long id) {
        return subCategoryService.deletesubCategory(id);
    }

    @PostMapping("/save/{id_cat}")
    public SubCategoryResponse addSubCategoryDECategory(@RequestBody SubCategoryRequest subCategoryRequest, @PathVariable("id_cat") Long id) {
        return subCategoryService.createSubCategoryCategory(subCategoryRequest, id);
    }


    @PutMapping(value = {"/updatee/{id_subcat}", "/updatee/{id_subcat}/{id_cat}"})
    public SubCategoryResponse updateSubCategoryDECategory(@RequestBody SubCategoryRequest subCategoryRequest, @PathVariable(value = "id_cat",required = false) Long id_cat, @PathVariable("id_subcat") Long id_subcat) {
        return subCategoryService.updateSubCategoryCategory(subCategoryRequest,id_cat, id_subcat);
    }

}

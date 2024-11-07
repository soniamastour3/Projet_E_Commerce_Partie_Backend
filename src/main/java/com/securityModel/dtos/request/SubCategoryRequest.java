package com.securityModel.dtos.request;


import com.securityModel.modele.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRequest {
    private Long id;
    private String name;
    private String description;
    private Category category;


    public void setCategory(Category category) {
        this.category = category;
    }

}

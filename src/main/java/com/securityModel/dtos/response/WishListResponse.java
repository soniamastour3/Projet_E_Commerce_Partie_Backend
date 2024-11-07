package com.securityModel.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishListResponse {
    private Long id; // The ID of the wishlist
    private List<String> productNames; // List of product names in the wishlist
    private String userName; // Full name of the user associated with the wishlist
}

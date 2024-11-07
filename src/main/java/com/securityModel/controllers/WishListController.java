package com.securityModel.controllers;


import com.securityModel.Services.WishListService;
import com.securityModel.dtos.request.WishListRequest;
import com.securityModel.dtos.response.WishListResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlists")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping("/add")
    public WishListResponse addProductToWishlist(@RequestBody WishListRequest wishListRequest) {
        return wishListService.addProductToWishlist(wishListRequest);
    }

    @DeleteMapping("/delete")
    public String deleteProductFromWishlist(@RequestBody WishListRequest wishListRequest) {
        return wishListService.deleteProductFromWishlist(wishListRequest);
    }

    @GetMapping("/{userId}")
    public WishListResponse getWishlistByUserId(@PathVariable Long userId) {
        return wishListService.getWishlistByIdUser(userId);
    }
}

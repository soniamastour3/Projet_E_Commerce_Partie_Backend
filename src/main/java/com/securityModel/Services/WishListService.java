package com.securityModel.Services;


import com.securityModel.dtos.request.WishListRequest;
import com.securityModel.dtos.response.WishListResponse;

public interface WishListService {

    WishListResponse addProductToWishlist(WishListRequest wishListRequest);
    String deleteProductFromWishlist(WishListRequest wishListRequest);
    WishListResponse getWishlistByIdUser(Long userId);
}

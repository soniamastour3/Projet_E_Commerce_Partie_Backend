package com.securityModel.Services.impl;


import com.securityModel.Services.WishListService;
import com.securityModel.dtos.request.WishListRequest;
import com.securityModel.dtos.response.WishListResponse;
import com.securityModel.modele.Product;
import com.securityModel.modele.User;
import com.securityModel.modele.WishList;
import com.securityModel.repository.ProductDao;
import com.securityModel.repository.UserDao;
import com.securityModel.repository.WishListDao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishListServiceIMPL implements WishListService {

    private final UserDao userDao;
    private final ProductDao productDao;
    private final WishListDao wishListDao;

    public WishListServiceIMPL(UserDao userDao, ProductDao productDao, WishListDao wishListDao) {
        this.userDao = userDao;
        this.productDao = productDao;
        this.wishListDao = wishListDao;
    }

    @Override
    public WishListResponse addProductToWishlist(WishListRequest wishListRequest) {
        Optional<User> userOptional = userDao.findById(wishListRequest.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Product> productOptional = productDao.findById(wishListRequest.getProductId());
        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        User user = userOptional.get();
        Product product = productOptional.get();

        // Fetch the user's wishlist or create a new one if it doesn't exist
        WishList wishList = wishListDao.findByUserId(wishListRequest.getUserId())
                .orElse(new WishList(null, new ArrayList<>(), user));

        // Add the product to the wishlist if it isn't already in it
        if (!wishList.getProducts().contains(product)) {
            wishList.getProducts().add(product);
        }

        // Save the updated wishlist
        WishList savedWishList = wishListDao.save(wishList);

        // Return a response DTO with the updated wishlist details
        return new WishListResponse(
                savedWishList.getId(),
                savedWishList.getProducts().stream().map(Product::getName).collect(Collectors.toList()),
                user.getUsername()
        );
    }

    @Override
    public String deleteProductFromWishlist(WishListRequest wishListRequest) {
        Optional<User> userOptional = userDao.findById(wishListRequest.getUserId());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        Optional<Product> productOptional = productDao.findById(wishListRequest.getProductId());
        if (productOptional.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        User user = userOptional.get();
        Product product = productOptional.get();

        WishList wishList = wishListDao.findByUserId(wishListRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Wishlist not found for user"));

        if (wishList.getProducts().contains(product)) {
            wishList.getProducts().remove(product);
        } else {
            throw new RuntimeException("Product not found in wishlist");
        }

        if (wishList.getProducts().isEmpty()) {
            // Delete the wishlist if it's empty
            wishListDao.delete(wishList);
            return "Product removed, and wishlist deleted as it was empty.";
        } else {
            // Save the updated wishlist
            wishListDao.save(wishList);
            return "Product removed from wishlist successfully.";
        }
    }
    @Override
    public WishListResponse getWishlistByIdUser(Long userId) {
        // Find the user by ID
        User user = userDao.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with this ID: " + userId));

        // Find the wishlist by user ID
        WishList wishList = wishListDao.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for user with ID: " + userId));

        // Map the wishlist entity to a response DTO
        return new WishListResponse(
                wishList.getId(),
                wishList.getProducts().stream().map(Product::getName).collect(Collectors.toList()),
                user.getUsername() + " " + user.getEmail()
        );
    }
}
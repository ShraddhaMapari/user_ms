package team9.user.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import team9.user.controller.BuyerController;
import team9.user.controller.CartController;
import team9.user.dto.Cart;
import team9.user.dto.Product;
import team9.user.dto.Wishlist;
import team9.user.entity.WishListId;
import team9.user.entity.WishlistEntity;
import team9.user.exception.NotPrivilegeBuyerException;
import team9.user.exception.UserException;
import team9.user.exception.WishListEmptyException;
import team9.user.exception.WishlistAlreadyExist;
import team9.user.exception.WishlistNotAvailableException;
import team9.user.repository.CartRepository;
import team9.user.repository.WishListRepository;

@Service
public class WishListService {

	@Value("${productAPIURL}")
	public String productAPIURI;

	@Autowired
	WishListRepository wishListRepository;

	@Autowired
	CartController cartController;

	@Autowired
	BuyerController buyerController;

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	CartRepository cartRepository;

	public void addToWishList(Wishlist wishlist) throws UserException {
		WishlistEntity wishlistEntity = wishListRepository.findByIdBuyerIdAndIdProdId(wishlist.getBuyerId(),
				wishlist.getProdId());
		if (wishlistEntity == null) {
			WishlistEntity newwishlistEntity = new WishlistEntity(
					new WishListId(wishlist.getBuyerId(), wishlist.getProdId()));
			wishListRepository.save(newwishlistEntity);
		} else {
			throw new WishlistAlreadyExist("wishlist.ALREADY_EXISTS");

		}

	}

	public void removeFromWishList(Wishlist wishlist) throws UserException {
		WishlistEntity wishlistEntity = wishListRepository.findByIdBuyerIdAndIdProdId(wishlist.getBuyerId(),
				wishlist.getProdId());
		if (wishlistEntity != null) {
			wishListRepository.deleteById(new WishListId(wishlist.getBuyerId(), wishlist.getProdId()));
		} else {
			throw new WishlistNotAvailableException("wishlist.NOT_AVAILABLE");
		}
	}

}

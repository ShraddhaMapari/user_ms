package team9.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import team9.user.controller.BuyerController;
import team9.user.dto.Cart;
import team9.user.dto.Product;
import team9.user.entity.CartEntity;
import team9.user.entity.CartId;
import team9.user.exception.CartItemNotExistException;
import team9.user.exception.NotPrivilegeBuyerException;
import team9.user.exception.StockNotAvailableException;
import team9.user.exception.UserException;
import team9.user.repository.CartRepository;

@Service
public class CartService {

	@Autowired
	CartRepository cartRepository;

	@Value("${productAPIURL}")
	public String productAPIURI;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	BuyerController buyerController;

	public void addToCart(Cart cart) throws UserException {

		final String baseUrl = productAPIURI + cart.getProdId();
		ResponseEntity<Product> result = restTemplate.getForEntity(baseUrl, Product.class);
		int stock = result.getBody().getStock();

		Optional<CartEntity> cartEntity = cartRepository.findById(new CartId(cart.getBuyerId(), cart.getProdId()));
		if (cartEntity.isPresent()) {
			if (buyerController.isBuyerPrivileged(cart.getBuyerId())) {
				if (cart.getQuantity() + cartEntity.get().getQuantity() <= stock) {
					cart.setQuantity(cart.getQuantity() + cartEntity.get().getQuantity());
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());
					cartRepository.save(cartEntity1);

				} else {
					throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
				}

			} else {
				if (cart.getQuantity() + cartEntity.get().getQuantity() <= stock
						&& cart.getQuantity() + cartEntity.get().getQuantity() <= 10) {
					cart.setQuantity(cart.getQuantity() + cartEntity.get().getQuantity());
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());

					cartRepository.save(cartEntity1);
				}

				else {
					if (cart.getQuantity() + cartEntity.get().getQuantity() > 10) {
						throw new NotPrivilegeBuyerException("wishlist.NOT_PRIVILEGE_BUYER");

					} else {
						throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");

					}
				}

			}

		} 
		
		else {
			if (buyerController.isBuyerPrivileged(cart.getBuyerId())) {
				if (cart.getQuantity() <= stock) {
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());
					cartRepository.save(cartEntity1);

				} else {
					throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
				}

			} else {

				if (cart.getQuantity() <= 10 && cart.getQuantity() <= stock) {
					CartEntity cartEntity1 = new CartEntity(new CartId(cart.getBuyerId(), cart.getProdId()),
							cart.getQuantity());
					cartRepository.save(cartEntity1);

				} else {
					if (cart.getQuantity() > stock) {
						throw new StockNotAvailableException("wishlist.STOCK_NOT_AVAILABLE");
					} else {
						throw new NotPrivilegeBuyerException("wishlist.NOT_PRIVILEGE_BUYER");

					}
				}
			}

		}

	}

	public void removeFromCart(Cart cart) throws UserException {
		
		Optional<CartEntity> cartEntity = cartRepository.findById(new CartId(cart.getBuyerId(), cart.getProdId()));
		if(cartEntity.isPresent()) {
			cartRepository.deleteById(new CartId(cart.getBuyerId(), cart.getProdId()));
			}
		else {
			throw new CartItemNotExistException("cart.ITEM_NOT_IN_CART");
		}
	}
}

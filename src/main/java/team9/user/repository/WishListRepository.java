package team9.user.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import team9.user.entity.WishListId;
import team9.user.entity.WishlistEntity;

public interface WishListRepository extends CrudRepository<WishlistEntity, WishListId>{
	public List<WishlistEntity> findByIdBuyerId(int buyerId);
	
	public WishlistEntity findByIdBuyerIdAndIdProdId(int buyerId,int ProdId);
}

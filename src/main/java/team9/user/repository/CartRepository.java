package team9.user.repository;








import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import team9.user.entity.CartEntity;
import team9.user.entity.CartId;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, CartId>{
	

	
}
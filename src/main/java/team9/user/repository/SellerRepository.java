package team9.user.repository;

import org.springframework.data.repository.CrudRepository;

import team9.user.entity.SellerEntity;


public interface SellerRepository extends CrudRepository<SellerEntity,Integer>{
		public SellerEntity findBySellerId(int Id);

		public SellerEntity findByEmail(String email);

		public SellerEntity findByPhoneNumber(String phoneNumber);
		
}

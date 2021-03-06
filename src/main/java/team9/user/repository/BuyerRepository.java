package team9.user.repository;

import org.springframework.data.repository.CrudRepository;

import team9.user.entity.BuyerEntity;

public interface BuyerRepository extends CrudRepository<BuyerEntity, Integer> {

	public BuyerEntity findByBuyerId(int Id);
	public BuyerEntity findByEmail(String emailId);
	public BuyerEntity findByPhoneNumber(String phoneNumber);
}

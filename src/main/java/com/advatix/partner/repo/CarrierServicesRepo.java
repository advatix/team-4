package com.advatix.partner.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advatix.partner.entity.CarrierServices;
import com.advatix.partner.entity.ShipmentRate;

@Repository
@Transactional
public interface CarrierServicesRepo extends JpaRepository<CarrierServices, Integer>{
	
	List<CarrierServices> findAllByDeliveryDaysLessThanEqual(long deliveryDays);


}

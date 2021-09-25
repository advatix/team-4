package com.advatix.partner.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advatix.partner.entity.ShipmentRate;
import com.advatix.partner.entity.ZoneZipcodeMapping;

@Repository
@Transactional
public interface ShipmentRateRepo extends JpaRepository<ShipmentRate, Integer>{
	
	List<ShipmentRate> findAllByServiceId(long serviceId);
	
	List<ShipmentRate> findAllByZoneCode(String zoneCode);
	
	List<ShipmentRate> findAllByZoneCodeAndServiceIdInAndPkgWeightMinLessThanEqualAndPkgWeightMaxGreaterThanEqualOrderByPrice(String zoneCode,List<Long> serviceIds,double weightMin, double weightMax);


}

package com.advatix.partner.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advatix.partner.entity.ZoneZipcodeMapping;

@Repository
@Transactional
public interface ZoneZipcodeMappingRepo extends JpaRepository<ZoneZipcodeMapping, Integer>{
	
	ZoneZipcodeMapping findByZipCodeBeginLessThanEqualAndZipCodeEndGreaterThanEqual(int zipCodeBegin, int zipCodeEnd);

}

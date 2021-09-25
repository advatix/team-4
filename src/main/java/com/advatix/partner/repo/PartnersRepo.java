package com.advatix.partner.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advatix.partner.entity.Partners;

@Repository
@Transactional
public interface PartnersRepo extends JpaRepository<Partners, Integer>{

}

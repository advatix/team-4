package com.advatix.partner.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advatix.partner.entity.SelectionTypeMaster;

@Repository
@Transactional
public interface SelectionTypeMasterRepo extends JpaRepository<SelectionTypeMaster, Integer>{

}

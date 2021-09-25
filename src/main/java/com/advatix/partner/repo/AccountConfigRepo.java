package com.advatix.partner.repo;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.advatix.partner.entity.AccountConfig;

@Repository
@Transactional
public interface AccountConfigRepo extends JpaRepository<AccountConfig, Integer>{

}

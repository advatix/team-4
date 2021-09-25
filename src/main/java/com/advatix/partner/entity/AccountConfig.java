package com.advatix.partner.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = AccountConfig.TABLE_NAME)
@Data
public class AccountConfig {

	public static final String TABLE_NAME = "account_config";

	@Id
	@Column(name = "Id", columnDefinition = "bigint(10)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String accountId;
	private long selectionType;
	private long defaultDeliveryDays;
	private String selectionMethod;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public long getSelectionType() {
		return selectionType;
	}

	public void setSelectionType(long selectionType) {
		this.selectionType = selectionType;
	}

	public long getDefaultDeliveryDays() {
		return defaultDeliveryDays;
	}

	public void setDefaultDeliveryDays(long defaultDeliveryDays) {
		this.defaultDeliveryDays = defaultDeliveryDays;
	}

}

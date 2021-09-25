package com.advatix.partner.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = CarrierServices.TABLE_NAME)
@Data
public class CarrierServices {

	public static final String TABLE_NAME = "carrier_services";

	@Id
	@Column(name = "Id", columnDefinition = "bigint(10)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String carrierName;
	
	private String serviceName;
	
	private long deliveryDays;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	

	public long getDeliveryDays() {
		return deliveryDays;
	}

	public void setDeliveryDays(long deliveryDays) {
		this.deliveryDays = deliveryDays;
	}

}

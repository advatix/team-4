package com.advatix.partner.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = ShipmentRate.TABLE_NAME)
@Data
public class ShipmentRate {

	public static final String TABLE_NAME = "shipment_rate";

	@Id
	@Column(name = "Id", columnDefinition = "bigint(10)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String shipFromZipCode;
	private String zoneCode;
	private long serviceId;
	private double pkgWeightMin;
	private double pkgWeightMax;
	private double price;
	private java.sql.Timestamp createdOn;
	private long status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getShipFromZipCode() {
		return shipFromZipCode;
	}

	public void setShipFromZipCode(String shipFromZipCode) {
		this.shipFromZipCode = shipFromZipCode;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public double getPkgWeightMin() {
		return pkgWeightMin;
	}

	public void setPkgWeightMin(double pkgWeightMin) {
		this.pkgWeightMin = pkgWeightMin;
	}

	public double getPkgWeightMax() {
		return pkgWeightMax;
	}

	public void setPkgWeightMax(double pkgWeightMax) {
		this.pkgWeightMax = pkgWeightMax;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public java.sql.Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(java.sql.Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

}

package com.advatix.partner.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = ZoneZipcodeMapping.TABLE_NAME)
@Data
public class ZoneZipcodeMapping {

	public static final String TABLE_NAME = "zone_zipcode_mapping";

	@Id
	@Column(name = "Id", columnDefinition = "bigint(10)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String zoneCode;
	private int zipCodeBegin;
	private int zipCodeEnd;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public long getZipCodeBegin() {
		return zipCodeBegin;
	}

	public void setZipCodeBegin(int zipCodeBegin) {
		this.zipCodeBegin = zipCodeBegin;
	}

	public long getZipCodeEnd() {
		return zipCodeEnd;
	}

	public void setZipCodeEnd(int zipCodeEnd) {
		this.zipCodeEnd = zipCodeEnd;
	}

}

package com.advatix.partner.dto;

import lombok.Data;

@Data
public class SelectionRequest {
	
	private int shipFromZipCode; 
	private int shipToZipCode;
	private double pkgWeight;
	private long deliveryDays;
	private String selectionOption;

}

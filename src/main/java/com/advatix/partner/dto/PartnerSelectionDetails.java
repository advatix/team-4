package com.advatix.partner.dto;

import lombok.Data;

@Data
public class PartnerSelectionDetails {
	
	private String carrierName; 
	private String shipMethod;
	private double rate;
	private int deliveryDays;
	

}

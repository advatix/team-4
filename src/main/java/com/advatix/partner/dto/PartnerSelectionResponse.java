package com.advatix.partner.dto;

import java.util.Set;

import lombok.Data;

@Data
public class PartnerSelectionResponse {
	
	
	private int preferedIndex;
	private Set<PartnerSelectionDetails> partnerSelectionList; 
	

}

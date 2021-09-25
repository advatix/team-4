
package com.advatix.partner.commons.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessages {

	public static String ACCOUNT_DETAILS_NOT_FOUND;

	@Value("${error.message.account.details.not.found}")
	public void setLicensePlateNumberAlreadyExistsErrorMessage(String errorMessage) {
		ACCOUNT_DETAILS_NOT_FOUND = errorMessage;
	}

}

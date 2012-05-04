package net.epicyclic.common;

import org.apache.commons.lang.RandomStringUtils;

public class ErrorUtils {

	public static String generateErrorId() {
		return RandomStringUtils.randomAlphanumeric(10);
	}

}

package com.covid.spchacker.cache.copy;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomAuth {

	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); 

	public static String generateNewToken() {
	    byte[] randomBytes = new byte[24];
	    secureRandom.nextBytes(randomBytes);
	    return base64Encoder.encodeToString(randomBytes);
	}
	
	public static String encode(String ip) {
		return Base64.getEncoder().encodeToString(ip.getBytes());
	}
	
	public static String decode(String ip) {
		byte[] decodedBytes = Base64.getDecoder().decode(ip);
		return new String(decodedBytes);
	}
	
}

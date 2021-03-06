package net.bplaced.javacrypto.blockchain.step3;

/*
* Diese Klasse geh�rt zu BlockChain.java
* This class belongs to BlockChain.java
*/

import java.security.MessageDigest;
import com.google.gson.GsonBuilder;

public class StringUtil {
	
	public static String generateSha256(String input){
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getJson(Object o) {
		return new GsonBuilder().setPrettyPrinting().create().toJson(o);
	}
}

package net.bplaced.javacrypto.blockchain.step6;

/*
* Diese Klasse gehört zu BlockChain.java
* This class belongs to BlockChain.java
*/

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class Wallet {

	public PrivateKey privateKey;
    public PublicKey publicKey;
	
    public Wallet() {
        generateKeyPair();
    }
    
    public void generateKeyPair() {
        try {
        	// secp192r1 [NIST P-192, X9.62 prime192v1] (1.2.840.10045.3.1.1)
        	// secp224k1 (1.3.132.0.32)
        	// secp224r1 [NIST P-224] (1.3.132.0.33)
        	// secp256k1 (1.3.132.0.10)
        	// secp256r1 [NIST P-256, X9.62 prime256v1] (1.2.840.10045.3.1.7)
        	KeyPairGenerator keypairGenerator = KeyPairGenerator.getInstance("EC", "SunEC");
    		ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp192r1"); 
        	//ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
    		keypairGenerator.initialize(ecSpec, new SecureRandom());
    		KeyPair keyPair = keypairGenerator.genKeyPair();
        	privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}

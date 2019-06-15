package net.bplaced.javacrypto.blockchain.step8;

/*
* Diese Klasse gehört zu JavaCryptoCoin.java
* This class belongs to JavaCryptoCoin.java
*/

import java.io.Serializable;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet implements Serializable{

	private static final long serialVersionUID = 3090822928966119493L;
	public PrivateKey privateKey;
    public PublicKey publicKey;
    public HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

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
    		// ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp192r1"); 
        	ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
    		keypairGenerator.initialize(ecSpec, new SecureRandom());
    		KeyPair keyPair = keypairGenerator.genKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : JavaCryptoCoin.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.id, UTXO); //add it to our list of unspent transactions.
                total += UTXO.value;
            }
        }
        return total;
    }
        
    public Transaction sendFunds(PublicKey _recipient, float value) {
        if (getBalance() < value) {
            System.out.println("# Das Guthaben ist zu gering um die Transaktion auszuführen, die Transaktion wurde abgebrochen.");
            System.out.println("# Diese Transaktion scheitert: Empfänger:" + _recipient + "\n  Überweisungsbetrag:" + value);
            return null;
        }
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) {
                break;
            }
        }
        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);
        for (TransactionInput input : inputs) {
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }
}

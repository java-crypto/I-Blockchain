package net.bplaced.javacrypto.blockchain.step6;

/*
* Diese Klasse gehört zu BlockChain.java
* This class belongs to BlockChain.java
*/

import java.security.PrivateKey;
import java.security.PublicKey;

public class Transaction {

	public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;
   
    public Transaction(PublicKey from, PublicKey to, float value) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
    }
    
    public boolean processTransaction() {
        if (verifySignature() == false) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }
        return true;
    }
    
    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        signature = StringUtil.generateECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}

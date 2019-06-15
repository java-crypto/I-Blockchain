package net.bplaced.javacrypto.blockchain.step9;

/*
* Diese Klasse gehört zu JavaCryptoCoin.java
* This class belongs to JavaCryptoCoin.java
*/

import java.io.Serializable;
import java.security.PublicKey;

public class TransactionOutput implements Serializable{

	private static final long serialVersionUID = 7902714355455361435L;
	public String id;
	public PublicKey recipient;
	public float value;
	public String parentTransactionId;

	public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
		this.recipient = recipient;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = StringUtil
				.generateSha256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
	}

	public boolean isMine(PublicKey publicKey) {
		return (publicKey.equals(recipient));
	}
}

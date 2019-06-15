package net.bplaced.javacrypto.blockchain.step9;

/*
* Diese Klasse gehört zu JavaCryptoCoin.java
* This class belongs to JavaCryptoCoin.java
*/

import java.io.Serializable;

public class TransactionInput implements Serializable{

	private static final long serialVersionUID = -6643746839251237698L;
	public String transactionOutputId;
    public TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}

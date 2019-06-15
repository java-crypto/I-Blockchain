package net.bplaced.javacrypto.blockchain.step3;

/*
* Diese Klasse gehört zu BlockChain.java
* This class belongs to BlockChain.java
*/

import java.util.Date;

public class Block {
	
	public String hash;
	public String previousHash; 
	private long timeStamp;
	public String data; // hier auf public zur manipulation gesetzt
	

	public Block(String previousHash, String data) {
		this.previousHash = previousHash;
		this.data = data;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	public String calculateHash() {
		String calculatedhash = StringUtil.generateSha256( 
				previousHash +
				Long.toString(timeStamp) +
				data 
				);
		return calculatedhash;
	}
}
